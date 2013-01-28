package org.motechproject.whp.patient.service;


import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.motechproject.event.MotechEvent;
import org.motechproject.testing.utils.BaseUnitTest;
import org.motechproject.whp.common.event.EventKeys;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.common.exception.WHPRuntimeException;
import org.motechproject.whp.common.repository.AllDistricts;
import org.motechproject.whp.common.validation.RequestValidator;
import org.motechproject.whp.patient.alerts.service.PatientAlertService;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.builder.PatientRequestBuilder;
import org.motechproject.whp.patient.command.UpdateCommandFactory;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Therapy;
import org.motechproject.whp.patient.domain.TherapyRemark;
import org.motechproject.whp.patient.mapper.PatientMapper;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTherapyRemarks;
import org.motechproject.whp.user.builder.ProviderBuilder;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.domain.ProviderIds;
import org.motechproject.whp.user.service.ProviderService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.common.domain.TreatmentWeekInstance.currentAdherenceCaptureWeek;

public class PatientServiceTest extends BaseUnitTest {
    PatientService patientService;
    @Mock
    AllPatients allPatients;

    @Mock
    AllTherapyRemarks allTherapyRemarks;
    @Mock
    UpdateCommandFactory updateCommandFactory;
    @Mock
    RequestValidator requestValidator;
    @Mock
    ProviderService providerService;
    @Mock
    private AllDistricts allDistricts;
    @Mock
    private PatientAlertService patientAlertService;
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    PatientMapper patientMapper;

    @Before
    public void setUp() {
        initMocks(this);
        patientMapper = new PatientMapper(providerService);
        patientService = new PatientService(allPatients, patientMapper, allTherapyRemarks, providerService, allDistricts, patientAlertService);
    }

    @Test
    public void shouldFetchAllRemarksUnderCurrentTherapy() {
        String therapyId = "therapyId";
        Patient patient = new Patient();
        Therapy currentTherapy = new Therapy();
        currentTherapy.setUid(therapyId);
        patient.setCurrentTherapy(currentTherapy);
        List<TherapyRemark> therapyRemarks = mock(List.class);

        when(allTherapyRemarks.findByTherapyId(therapyId)).thenReturn(therapyRemarks);

        assertThat(patientService.getCmfAdminRemarks(patient), is(therapyRemarks));
    }

    @Test
    public void shouldUpdateAllPatients_WhenEventIsRaised() {
        String providerId = "providerId";
        String newDistrict = "newDistrict";

        Map<String, Object> params = new HashMap<>();
        params.put("0", providerId);
        MotechEvent motechEvent = new MotechEvent(EventKeys.PROVIDER_DISTRICT_CHANGE, params);

        Patient patient1 = new PatientBuilder().withDefaults().withPatientId("patient1").withProviderId(providerId).build();
        Patient patient2 = new PatientBuilder().withDefaults().withPatientId("patient2").withProviderId(providerId).build();

        List<Patient> patients = Arrays.asList(patient1, patient2);
        Provider provider = new ProviderBuilder().withProviderId(providerId).withDistrict(newDistrict).build();

        when(providerService.findByProviderId(providerId)).thenReturn(provider);
        when(allPatients.getAllWithActiveTreatmentFor(providerId)).thenReturn(patients);

        patientService.handleProviderDistrictChange(motechEvent);

        assertThat(patient1.getCurrentTreatment().getProviderDistrict(), is(newDistrict));
        assertThat(patient2.getCurrentTreatment().getProviderDistrict(), is(newDistrict));
        verify(allPatients).update(patient1);
        verify(allPatients).update(patient2);
        verify(allPatients).getAllWithActiveTreatmentFor(providerId);
    }

    @Test
    public void shouldThrowErrorWhenWrongDistrictIsUsedToCreatePatient() {
        expectedException.expect(WHPRuntimeException.class);
        expectedException.expectMessage(WHPErrorCode.INVALID_DISTRICT.getMessage());
        String district = "myOwnDistrict";
        when(allDistricts.findByName(district)).thenReturn(null);

        patientService.createPatient(new PatientRequestBuilder().withDefaults().withAddressDistrict(district).build());
    }

    @Test
    public void shouldReturnActivePatientsGivenTheProvidersList() {
        ProviderIds providersList = new ProviderIds(asList("providerId1", "providerId2"));
        ProviderIds providersWithActivePatients = new ProviderIds(asList("providerId1"));

        when(allPatients.providersWithActivePatients(providersList)).thenReturn(providersWithActivePatients);

        ProviderIds actualProviderIds = patientService.providersWithActivePatients(providersList);

        assertEquals(providersWithActivePatients, actualProviderIds);
        verify(allPatients).providersWithActivePatients(providersList);

    }

    @Test
    public void shouldReturnProvidersWithActivePatients() {
        ProviderIds providersWithActivePatients = new ProviderIds(asList("providerId1"));

        when(allPatients.providersWithActivePatients()).thenReturn(providersWithActivePatients);

        ProviderIds actualProviderIds = patientService.providersWithActivePatients();

        assertEquals(providersWithActivePatients, actualProviderIds);
        verify(allPatients).providersWithActivePatients();
    }

    @Test
    public void shouldReturnProvidersWithPendingAdherence() {
        ProviderIds expectedProviderIds = new ProviderIds(asList("provider1"));
        LocalDate asOfDate = currentAdherenceCaptureWeek().startDate();
        when(allPatients.findAllProvidersWithoutAdherenceAsOf(asOfDate)).thenReturn(expectedProviderIds);

        ProviderIds providerIds = patientService.getAllProvidersWithPendingAdherence(asOfDate);

        assertEquals(expectedProviderIds, providerIds);
        verify(allPatients).findAllProvidersWithoutAdherenceAsOf(asOfDate);
    }

    @Test
    public void shouldUpdatePatientAlertsWhenPatientIsUpdated() {
        Patient patient = mock(Patient.class);
        patientService.update(patient);

        verify(patientAlertService).processAllAlerts(patient);
        verify(allPatients).update(patient);
    }

    @Test
    public void shouldUpdatePatientAlertsBasedOnAlertConfiguration() {
        Patient patient = mock(Patient.class);
        patientService.updateBasedOnAlertConfiguration(patient);

        verify(patientAlertService).processAlertsBasedOnConfiguration(patient);
        verify(allPatients).update(patient);
    }


    @After
    public void tearDown() {
        verifyNoMoreInteractions(allPatients);
        verifyNoMoreInteractions(patientAlertService);
    }
}
