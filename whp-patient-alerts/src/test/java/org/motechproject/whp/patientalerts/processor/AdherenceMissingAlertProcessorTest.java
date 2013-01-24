package org.motechproject.whp.patientalerts.processor;

import org.junit.Test;
import org.motechproject.whp.patient.domain.Patient;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AdherenceMissingAlertProcessorTest {

    @Test
    public void shouldUpdateAdherenceMissingAlertOnPatient() {
        Patient patient = mock(Patient.class);
        AdherenceMissingAlertProcessor adherenceMissingAlertProcessor = new AdherenceMissingAlertProcessor();
        adherenceMissingAlertProcessor.process(patient);

        verify(patient).updateAdherenceMissingAlert();
    }
}
