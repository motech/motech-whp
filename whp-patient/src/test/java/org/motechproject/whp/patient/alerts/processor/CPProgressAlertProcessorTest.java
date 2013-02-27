package org.motechproject.whp.patient.alerts.processor;

import org.junit.Test;
import org.motechproject.whp.patient.domain.Patient;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CPProgressAlertProcessorTest {
    @Test
    public void shouldReturnCpProgressPercentage(){
        Patient patient = mock(Patient.class);
        when(patient.getCPProgressPercentage()).thenReturn((double) 40);

        CPProgressAlertProcessor cpProgressAlertProcessor = new CPProgressAlertProcessor();
        cpProgressAlertProcessor.process(patient);

        verify(patient).getCPProgressPercentage();
    }
}
