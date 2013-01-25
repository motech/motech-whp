package org.motechproject.whp.patient.alerts.processor;

import org.junit.Test;
import org.motechproject.whp.patient.domain.Patient;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class AdherenceMissingAlertProcessorTest {

    @Test
    public void shouldUpdateAdherenceMissingAlertOnPatient() {
        Patient patient = mock(Patient.class);
        int weeksElapsedSinceLastDose = 5;
        when(patient.getWeeksElapsedSinceLastDose()).thenReturn(weeksElapsedSinceLastDose);

        AdherenceMissingAlertProcessor adherenceMissingAlertProcessor = new AdherenceMissingAlertProcessor();
        assertEquals(weeksElapsedSinceLastDose, adherenceMissingAlertProcessor.process(patient));

        verify(patient).getWeeksElapsedSinceLastDose();
    }
}
