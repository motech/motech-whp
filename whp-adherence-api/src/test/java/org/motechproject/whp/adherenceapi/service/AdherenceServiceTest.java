package org.motechproject.whp.adherenceapi.service;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.adherenceapi.request.AdherenceCaptureFlashingResponse;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.service.PatientService;

import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
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
                new AdherenceCaptureFlashingResponse(patientsWithAdherence, asList("1234", "5678")),
                adherenceService.adherenceSubmissionInformation(providerId, today)
        );
    }

    private Patient patients(String patientId) {
        return new PatientBuilder().withDefaults().withPatientId(patientId).build();
    }
}
