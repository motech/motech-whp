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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class AdherenceServiceTest {

    private AdherenceService adherenceService;
    @Mock
    private AdherenceDataService adherenceDataService;

    @Before
    public void setUp() {
        initMocks(this);
        adherenceService = new AdherenceService(adherenceDataService);
    }

    @Test
    public void shouldSummarizeAdherenceSubmittedByProvider() {
        String providerId = "providerid";

        AdherenceSummaryByProvider expectedAdherenceSummary = mock(AdherenceSummaryByProvider.class);
        when(adherenceDataService.getAdherenceSummary(providerId)).thenReturn(expectedAdherenceSummary);

        assertEquals(expectedAdherenceSummary, adherenceService.adherenceSummary(providerId));
    }

    @Test
    public void shouldReturnTreatmentCategoryInfoGivenPatientId() {
        String patientId = "1234";
        Patient govtCategoryPatient = patients(patientId);

        Dosage dosage = adherenceService.dosageForPatient(govtCategoryPatient);

        assertEquals(TreatmentProvider.GOVERNMENT, dosage.getTreatmentProvider());
        assertEquals("0", dosage.getValidRangeFrom());
        assertEquals("3", dosage.getValidRangeTo());
    }

    @Test
    public void shouldReturnTreatmentCategoryInfoAsNullForInvalidPatientId() {
        Dosage dosage = adherenceService.dosageForPatient(null);
        assertNull(dosage);
    }

    private Patient patients(String patientId) {
        return new PatientBuilder().withDefaults().withPatientId(patientId).build();
    }
}
