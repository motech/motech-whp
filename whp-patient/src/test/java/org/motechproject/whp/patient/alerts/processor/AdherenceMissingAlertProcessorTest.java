package org.motechproject.whp.patient.alerts.processor;

import org.junit.Test;
import org.motechproject.whp.common.domain.alerts.AlertConfiguration;
import org.motechproject.whp.common.domain.alerts.AlertThreshold;
import org.motechproject.whp.common.domain.alerts.PatientAlertType;
import org.motechproject.whp.patient.domain.Patient;

import static org.mockito.Mockito.*;

public class AdherenceMissingAlertProcessorTest {

    @Test
    public void shouldUpdateAdherenceMissingAlertOnPatient() {
        Patient patient = mock(Patient.class);
        when(patient.getWeeksElapsedSinceLastDose()).thenReturn(5);
        AlertConfiguration alertConfiguration = mock(AlertConfiguration.class);
        when(alertConfiguration.getThresholdFor(PatientAlertType.AdherenceMissing, 5)).thenReturn(new AlertThreshold(4, 1));

        AdherenceMissingAlertProcessor adherenceMissingAlertProcessor = new AdherenceMissingAlertProcessor(alertConfiguration);
        adherenceMissingAlertProcessor.process(patient);

        verify(patient).getWeeksElapsedSinceLastDose();
        verify(patient).updatePatientAlert(PatientAlertType.AdherenceMissing, 5, 1);
    }
}
