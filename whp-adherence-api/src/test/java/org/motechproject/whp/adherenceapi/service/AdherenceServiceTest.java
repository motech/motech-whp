package org.motechproject.whp.adherenceapi.service;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.adherenceapi.domain.AdherenceSummary;
import org.motechproject.whp.adherenceapi.domain.TreatmentCategoryInfo;
import org.motechproject.whp.adherenceapi.domain.TreatmentCategoryType;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.motechproject.whp.user.service.ProviderService;

import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.util.DateUtil.today;
import static org.motechproject.whp.common.domain.TreatmentWeekInstance.week;

public class AdherenceServiceTest {

    private AdherenceService adherenceService;

    @Mock
    private WHPAdherenceService whpAdherenceService;
    @Mock
    private PatientService patientService;
    @Mock
    private ReportingPublisherService reportingPublisherService;
    @Mock
    private AdherenceCaptureRequestBuilder adherenceCaptureRequestBuilder;

    @Mock
    private ProviderService providerService;

    @Before
    public void setUp() {
        initMocks(this);
        adherenceService = new AdherenceService(whpAdherenceService, patientService);
    }

    @Test
    public void shouldSummarizeAdherenceSubmittedByProvider() {
        String providerId = "providerid";
        LocalDate today = today();
        List<String> patientsWithAdherence = asList("1234", "5678");
        List<Patient> patientsForProvider = asList(patients("1234"), patients("5678"));

        when(patientService.getAllWithActiveTreatmentForProvider(providerId)).thenReturn(patientsForProvider);
        when(whpAdherenceService.patientsWithAdherence(providerId, week(today))).thenReturn(patientsWithAdherence);

        assertEquals(
                new AdherenceSummary(patientsWithAdherence, patientsForProvider),
                adherenceService.adherenceSummary(providerId, today)
        );
    }

    @Test
    public void shouldReturnTrueForValidDoseCount() {
        String patientId = "1234";
        String doseTakenCount = "3";

        Patient govtCategoryPatient = new PatientBuilder().withDefaults().build();
        when(patientService.findByPatientId(patientId)).thenReturn(govtCategoryPatient);

        assertTrue(adherenceService.validateDosage(patientId, doseTakenCount));

        verify(patientService).findByPatientId(patientId);
    }

    @Test
    public void shouldReturnFailureResponseForInValidDoseCount() {
        String patientId = "1234";
        String inValidDoseCount = "7";

        Patient govtCategoryPatient = new PatientBuilder().withDefaults().build();
        when(patientService.findByPatientId(patientId)).thenReturn(govtCategoryPatient);

        assertFalse(adherenceService.validateDosage(patientId, inValidDoseCount));

        verify(patientService).findByPatientId(patientId);
    }

    @Test
    public void shouldReturnFailureResponseForDoseCountOfInvalidPatient() {
        String patientId = "1234";
        String validDoseCount = "3";

        when(patientService.findByPatientId(patientId)).thenReturn(null);

        assertFalse(adherenceService.validateDosage(patientId, validDoseCount));

        verify(patientService).findByPatientId(patientId);
    }

    @Test
    public void shouldReturnTreatmentCategoryInfoGivenPatientId() {
        String patientId = "1234";
        Patient govtCategoryPatient = patients(patientId);

        when(patientService.findByPatientId(patientId)).thenReturn(govtCategoryPatient);

        TreatmentCategoryInfo treatmentCategoryInfo = adherenceService.getTreatmentCategoryInformation(patientId);

        assertEquals(TreatmentCategoryType.GOVERNMENT, treatmentCategoryInfo.getTreatmentCategoryType());
        assertEquals("0", treatmentCategoryInfo.getValidRangeFrom());
        assertEquals("3", treatmentCategoryInfo.getValidRangeTo());
        verify(patientService).findByPatientId(patientId);
    }

    @Test
    public void shouldReturnTreatmentCategoryInfoAsNullForInvalidPatientId() {
        String patientId = "invalidPatient";

        when(patientService.findByPatientId(patientId)).thenReturn(null);

        TreatmentCategoryInfo treatmentCategoryInfo = adherenceService.getTreatmentCategoryInformation(patientId);

        assertNull(treatmentCategoryInfo);
        verify(patientService).findByPatientId(patientId);
    }

    private Patient patients(String patientId) {
        return new PatientBuilder().withDefaults().withPatientId(patientId).build();
    }
}
