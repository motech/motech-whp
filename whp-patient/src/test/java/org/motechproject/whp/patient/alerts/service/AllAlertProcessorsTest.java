package org.motechproject.whp.patient.alerts.service;

import org.junit.Test;
import org.motechproject.testing.utils.BaseUnitTest;
import org.motechproject.whp.common.domain.alerts.AllAlertConfigurations;
import org.motechproject.whp.patient.alerts.processor.AlertProcessor;
import org.motechproject.whp.patient.domain.Patient;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.motechproject.whp.common.domain.alerts.PatientAlertType.AdherenceMissing;
import static org.motechproject.whp.common.domain.alerts.PatientAlertType.CumulativeMissedDoses;

public class AllAlertProcessorsTest extends BaseUnitTest {

    @Test
    public void shouldRunAlertProcessorsBasedOnAlertConfiguration() {
        Patient patient = mock(Patient.class);

        Set<AlertProcessor> alertProcessorSet = new HashSet();
        AlertProcessor alertProcessor1 = mock(AlertProcessor.class);
        AlertProcessor alertProcessor2 = mock(AlertProcessor.class);

        double adherenceMissingAlertValue = 5;
        int severityForAdherenceMissingAlert = 2;
        when(alertProcessor1.alertType()).thenReturn(AdherenceMissing);
        when(alertProcessor1.process(patient)).thenReturn(adherenceMissingAlertValue);
        when(alertProcessor2.alertType()).thenReturn(CumulativeMissedDoses);

        alertProcessorSet.add(alertProcessor1);
        alertProcessorSet.add(alertProcessor2);

        AllAlertConfigurations alertConfigurations = mock(AllAlertConfigurations.class);
        when(alertConfigurations.shouldRunToday(AdherenceMissing)).thenReturn(true);
        when(alertConfigurations.getAlertSeverityFor(AdherenceMissing, adherenceMissingAlertValue)).thenReturn(severityForAdherenceMissingAlert);
        when(alertConfigurations.shouldRunToday(CumulativeMissedDoses)).thenReturn(false);

        AllAlertProcessors allAlertProcessors = new AllAlertProcessors(alertProcessorSet, alertConfigurations);

        allAlertProcessors.processBasedOnAlertConfiguration(patient);

        verify(alertProcessor1).process(patient);
        verify(alertProcessor2, never()).process(patient);
        verify(patient).updatePatientAlert(AdherenceMissing, adherenceMissingAlertValue, severityForAdherenceMissingAlert);
    }

    @Test
    public void shouldRunAllAlertProcessors() {
        Patient patient = mock(Patient.class);

        Set<AlertProcessor> alertProcessorSet = new HashSet();
        AlertProcessor alertProcessor1 = mock(AlertProcessor.class);
        AlertProcessor alertProcessor2 = mock(AlertProcessor.class);

        double adherenceMissingAlertValue = 5;
        int severityForAdherenceMissingAlert = 2;
        int severityForCumulativeMissedDoseAlert = 2;
        double cumulativeMissedDoseValue = 3;

        when(alertProcessor1.alertType()).thenReturn(AdherenceMissing);
        when(alertProcessor1.process(patient)).thenReturn(adherenceMissingAlertValue);
        when(alertProcessor2.alertType()).thenReturn(CumulativeMissedDoses);
        when(alertProcessor2.process(patient)).thenReturn(cumulativeMissedDoseValue);
        alertProcessorSet.add(alertProcessor1);
        alertProcessorSet.add(alertProcessor2);

        AllAlertConfigurations alertConfigurations = mock(AllAlertConfigurations.class);
        when(alertConfigurations.getAlertSeverityFor(AdherenceMissing, adherenceMissingAlertValue)).thenReturn(severityForAdherenceMissingAlert);
        when(alertConfigurations.getAlertSeverityFor(CumulativeMissedDoses, cumulativeMissedDoseValue)).thenReturn(severityForCumulativeMissedDoseAlert);

        AllAlertProcessors allAlertProcessors = new AllAlertProcessors(alertProcessorSet, alertConfigurations);

        allAlertProcessors.processAll(patient);

        verify(alertProcessor1).process(patient);
        verify(alertProcessor2).process(patient);
        verify(patient).updatePatientAlert(AdherenceMissing, adherenceMissingAlertValue, severityForAdherenceMissingAlert);
        verify(patient).updatePatientAlert(CumulativeMissedDoses, cumulativeMissedDoseValue, severityForCumulativeMissedDoseAlert);

    }
}
