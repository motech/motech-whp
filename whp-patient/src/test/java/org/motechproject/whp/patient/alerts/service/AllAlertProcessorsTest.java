package org.motechproject.whp.patient.alerts.service;

import org.junit.Test;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.alerts.processor.AlertProcessor;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AllAlertProcessorsTest {

    @Test
    public void shouldApplyAllAlertProcessors() {
        Patient patient = mock(Patient.class);

        Set<AlertProcessor> alertProcessorSet = new HashSet();
        AlertProcessor alertProcessor1 = mock(AlertProcessor.class);
        AlertProcessor alertProcessor2 = mock(AlertProcessor.class);
        alertProcessorSet.add(alertProcessor1);
        alertProcessorSet.add(alertProcessor2);

        AllAlertProcessors allAlertProcessors = new AllAlertProcessors(alertProcessorSet);

        allAlertProcessors.process(patient);

        verify(alertProcessor1).process(patient);
        verify(alertProcessor2).process(patient);
    }
}
