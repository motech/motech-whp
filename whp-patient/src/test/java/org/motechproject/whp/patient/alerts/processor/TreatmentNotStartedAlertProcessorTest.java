package org.motechproject.whp.patient.alerts.processor;

import org.junit.Test;
import org.motechproject.whp.patient.domain.Patient;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.motechproject.whp.common.domain.alerts.PatientAlertType.TreatmentNotStarted;

public class TreatmentNotStartedAlertProcessorTest {

    @Test
    public void shouldReturnTreatmentNotStartedDaysForPatient() {
        Patient patient = mock(Patient.class);
        int therapyNotStartedSince = 5;
        when(patient.isCurrentTreatmentPaused()).thenReturn(false);
        when(patient.getDaysSinceTherapyHasNotStarted()).thenReturn(therapyNotStartedSince);

        TreatmentNotStartedAlertProcessor treatmentNotStartedAlertProcessor = new TreatmentNotStartedAlertProcessor();
        assertEquals(therapyNotStartedSince, treatmentNotStartedAlertProcessor.process(patient));

        verify(patient).getDaysSinceTherapyHasNotStarted();
    }

    @Test
    public void shouldReturnAlertValueAsZeroForPausedPatients() {
        Patient patient = mock(Patient.class);
        when(patient.isCurrentTreatmentPaused()).thenReturn(true);
        when(patient.getDaysSinceTherapyHasNotStarted()).thenReturn(5);

        TreatmentNotStartedAlertProcessor treatmentNotStartedAlertProcessor = new TreatmentNotStartedAlertProcessor();
        assertEquals(0, treatmentNotStartedAlertProcessor.process(patient));
    }

    @Test
    public void shouldReturnAlertTypeAsTreatmentNotStarted() {
        assertEquals(TreatmentNotStarted, new TreatmentNotStartedAlertProcessor().alertType());
    }
}
