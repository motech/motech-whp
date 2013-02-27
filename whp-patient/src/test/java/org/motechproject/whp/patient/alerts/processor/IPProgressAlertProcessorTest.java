package org.motechproject.whp.patient.alerts.processor;

import org.junit.Test;
import org.motechproject.whp.patient.domain.Patient;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class IPProgressAlertProcessorTest {
    @Test
    public void shouldReturnIpProgressPercentage(){
        Patient patient = mock(Patient.class);
        when(patient.getIPProgressPercentage()).thenReturn((double) 40);

        IPProgressAlertProcessor ipProgressAlertProcessor = new IPProgressAlertProcessor();
        ipProgressAlertProcessor.process(patient);

        verify(patient).getIPProgressPercentage();
    }
}
