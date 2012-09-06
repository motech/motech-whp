package org.motechproject.whp.patient.service;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.event.MotechEvent;
import org.motechproject.testing.utils.BaseUnitTest;
import org.motechproject.whp.common.event.EventKeys;
import org.motechproject.whp.common.validation.RequestValidator;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.command.UpdateCommandFactory;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Therapy;
import org.motechproject.whp.patient.domain.TherapyRemark;
import org.motechproject.whp.patient.mapper.PatientMapper;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTherapyRemarks;
import org.motechproject.whp.user.builder.ProviderBuilder;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.service.ProviderService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

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

    PatientMapper patientMapper;

    @Before
    public void setUp() {
        initMocks(this);
        patientMapper = new PatientMapper(providerService);
        patientService = new PatientService(allPatients, patientMapper, allTherapyRemarks, updateCommandFactory, requestValidator, providerService);
    }
    @Test
    public void shouldFetchAllRemarksUnderCurrentTherapy(){
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
    public void shouldUpdateAllPatients_WhenEventIsRaised(){
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

    @After
    public void tearDown() {
        verifyNoMoreInteractions(allPatients);
    }
}
