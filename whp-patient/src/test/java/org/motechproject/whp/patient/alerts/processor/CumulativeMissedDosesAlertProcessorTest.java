package org.motechproject.whp.patient.alerts.processor;

import org.junit.Test;
import org.motechproject.whp.patient.domain.Patient;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class CumulativeMissedDosesAlertProcessorTest {

    @Test
    public void shouldReturnCumulativeMissedDosesForPatient() {
        Patient patient = mock(Patient.class);
        int cumulativeMissedDoses = 5;
        when(patient.isCurrentTreatmentPaused()).thenReturn(false);
        when(patient.cumulativeMissedDoses()).thenReturn(cumulativeMissedDoses);

        CumulativeMissedDosesAlertProcessor cumulativeMissedDosesAlertProcessor = new CumulativeMissedDosesAlertProcessor();
        assertEquals(cumulativeMissedDoses, cumulativeMissedDosesAlertProcessor.process(patient));

        verify(patient).cumulativeMissedDoses();
    }

    @Test
    public void shouldReturnAlertValueAsZeroForPausedPatients() {
        Patient patient = mock(Patient.class);
        when(patient.isCurrentTreatmentPaused()).thenReturn(true);
        when(patient.cumulativeMissedDoses()).thenReturn(5);

        CumulativeMissedDosesAlertProcessor cumulativeMissedDosesAlertProcessor = new CumulativeMissedDosesAlertProcessor();
        assertEquals(0, cumulativeMissedDosesAlertProcessor.process(patient));
    }
}
