package org.motechproject.whp.adherenceapi.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.adherence.domain.AdherenceSummaryByProvider;
import org.motechproject.whp.adherence.service.AdherenceDataService;
import org.motechproject.whp.adherenceapi.domain.Dosage;
import org.motechproject.whp.adherenceapi.domain.TreatmentProvider;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.motechproject.whp.user.service.ProviderService;

import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class AdherenceServiceTest {

    private AdherenceService adherenceService;
    @Mock
    private PatientService patientService;
    @Mock
    private ReportingPublisherService reportingPublisherService;

    @Mock
    private AdherenceDataService adherenceDataService;

    @Mock
    private ProviderService providerService;

    @Before
    public void setUp() {
        initMocks(this);
        adherenceService = new AdherenceService(patientService, adherenceDataService);
    }

    @Test
    public void shouldSummarizeAdherenceSubmittedByProvider() {
        String providerId = "providerid";
        List<Patient> patientsForProvider = asList(patients("1234"), patients("5678"));

        when(patientService.getAllWithActiveTreatmentForProvider(providerId)).thenReturn(patientsForProvider);

        AdherenceSummaryByProvider expectedAdherenceSummary = new AdherenceSummaryByProvider(providerId, patientsForProvider);
        when(adherenceDataService.getAdherenceSummary(providerId)).thenReturn(expectedAdherenceSummary);

        assertEquals(expectedAdherenceSummary, adherenceService.adherenceSummary(providerId));
    }

    @Test
    public void shouldReturnTreatmentCategoryInfoGivenPatientId() {
        String patientId = "1234";
        Patient govtCategoryPatient = patients(patientId);

        when(patientService.findByPatientId(patientId)).thenReturn(govtCategoryPatient);

        Dosage dosage = adherenceService.dosageForPatient(patientId);

        assertEquals(TreatmentProvider.GOVERNMENT, dosage.getTreatmentProvider());
        assertEquals("0", dosage.getValidRangeFrom());
        assertEquals("3", dosage.getValidRangeTo());
        verify(patientService).findByPatientId(patientId);
    }

    @Test
    public void shouldReturnTreatmentCategoryInfoAsNullForInvalidPatientId() {
        String patientId = "invalidPatient";

        when(patientService.findByPatientId(patientId)).thenReturn(null);

        Dosage dosage = adherenceService.dosageForPatient(patientId);

        assertNull(dosage);
        verify(patientService).findByPatientId(patientId);
    }

    private Patient patients(String patientId) {
        return new PatientBuilder().withDefaults().withPatientId(patientId).build();
    }
}
