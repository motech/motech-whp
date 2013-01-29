package org.motechproject.whp.common.domain.alerts;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.motechproject.model.DayOfWeek.Wednesday;
import static org.motechproject.whp.common.domain.alerts.PatientAlertType.AdherenceMissing;
import static org.motechproject.whp.common.domain.alerts.PatientAlertType.CumulativeMissedDoses;
import static org.motechproject.whp.common.domain.alerts.PatientAlertType.TreatmentNotStarted;

public class AllAlertConfigurationsTest {

    @Test
    public void shouldReturnAlertThresholdSeverity() {
        AlertThresholds alertThresholds = new AlertThresholds(1, 2, 6);
        AlertConfiguration alertConfiguration = new AlertConfiguration(AdherenceMissing, alertThresholds, asList(Wednesday));
        Map<PatientAlertType, AlertConfiguration> alertConfigurationMap = new HashMap<>();
        alertConfigurationMap.put(AdherenceMissing, alertConfiguration);
        AllAlertConfigurations allAlertConfigurations = new AllAlertConfigurations(alertConfigurationMap);

        assertEquals(alertThresholds.getThreshold(2).getAlertSeverity(), allAlertConfigurations.getAlertSeverityFor(AdherenceMissing, 2));
        assertEquals(alertThresholds.getThreshold(4).getAlertSeverity(), allAlertConfigurations.getAlertSeverityFor(AdherenceMissing, 4));
        assertEquals(alertThresholds.getThreshold(6).getAlertSeverity(), allAlertConfigurations.getAlertSeverityFor(AdherenceMissing, 6));
    }

    @Test
    public void shouldReturnIfAlertShouldBeRunToday() {
        AlertConfiguration alertConfiguration = mock(AlertConfiguration.class);

        Map<PatientAlertType, AlertConfiguration> alertConfigurationMap = new HashMap<>();
        alertConfigurationMap.put(AdherenceMissing, alertConfiguration);
        AllAlertConfigurations allAlertConfigurations = new AllAlertConfigurations(alertConfigurationMap);

        when(alertConfiguration.shouldRunToday()).thenReturn(true);
        assertTrue(allAlertConfigurations.shouldRunToday(AdherenceMissing));

        when(alertConfiguration.shouldRunToday()).thenReturn(false);
        assertFalse(allAlertConfigurations.shouldRunToday(AdherenceMissing));

        verify(alertConfiguration, times(2)).shouldRunToday();
    }

    @Test
    public void shouldReturnAlertConfigurations() {
        AllAlertConfigurations configurations = new AllAlertConfigurations();
        Map<PatientAlertType, AlertConfiguration> alertConfigurationMap = configurations.alertConfigurationMap;
        assertEquals(1, alertConfigurationMap.get(AdherenceMissing).getAlertThresholds().getThreshold(1).getThreshold());
        assertEquals(10, alertConfigurationMap.get(CumulativeMissedDoses).getAlertThresholds().getThreshold(10).getThreshold());
        assertEquals(10, alertConfigurationMap.get(TreatmentNotStarted).getAlertThresholds().getThreshold(10).getThreshold());
    }
}
