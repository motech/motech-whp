package org.motechproject.whp.patient.alerts.processor;

import org.junit.Test;
import org.motechproject.whp.patient.domain.Patient;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.motechproject.whp.common.domain.alerts.PatientAlertType.AdherenceMissing;

public class AdherenceMissingAlertProcessorTest {

    @Test
    public void shouldReturnMissingWeeksOfAdherenceForPatient() {
        Patient patient = mock(Patient.class);
        int weeksElapsedSinceLastDose = 5;
        when(patient.isCurrentTreatmentPaused()).thenReturn(false);
        when(patient.getWeeksElapsedSinceLastDose()).thenReturn(weeksElapsedSinceLastDose);

        AdherenceMissingAlertProcessor adherenceMissingAlertProcessor = new AdherenceMissingAlertProcessor();
        assertEquals(weeksElapsedSinceLastDose, adherenceMissingAlertProcessor.process(patient));

        verify(patient).getWeeksElapsedSinceLastDose();
    }

    @Test
    public void shouldReturnAlertValueAsZeroForPausedPatients() {
        Patient patient = mock(Patient.class);
        when(patient.isCurrentTreatmentPaused()).thenReturn(true);
        when(patient.getWeeksElapsedSinceLastDose()).thenReturn(5);

        AdherenceMissingAlertProcessor adherenceMissingAlertProcessor = new AdherenceMissingAlertProcessor();
        assertEquals(0, adherenceMissingAlertProcessor.process(patient));
    }

    @Test
    public void shouldReturnAlertTypeAsAdherenceMissing() {
        assertEquals(AdherenceMissing, new AdherenceMissingAlertProcessor().alertType());
    }
}
