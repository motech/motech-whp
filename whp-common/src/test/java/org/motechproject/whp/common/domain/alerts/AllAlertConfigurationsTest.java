package org.motechproject.whp.common.domain.alerts;

import org.junit.Test;
import org.motechproject.whp.common.service.AlertsPropertiesValues;

import java.util.HashMap;
import java.util.List;
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
        List<String> adherenceMissingWeeks = asList("1", "2", "6");
        AlertsPropertiesValues alertsPropertiesValues = new AlertsPropertiesValues();
        alertsPropertiesValues.setAdherenceMissingWeeks(adherenceMissingWeeks);

        AlertThresholds alertThresholds = new AlertThresholds(alertsPropertiesValues.getAdherenceMissingWeeks());
        AlertConfiguration alertConfiguration = new AlertConfiguration(AdherenceMissing, alertThresholds, asList(Wednesday));
        Map<PatientAlertType, AlertConfiguration> alertConfigurationMap = new HashMap<>();
        alertConfigurationMap.put(AdherenceMissing, alertConfiguration);
        AllAlertConfigurations allAlertConfigurations = new AllAlertConfigurations(alertConfigurationMap);

        assertEquals(alertThresholds.getThreshold(1).getAlertSeverity(), allAlertConfigurations.getAlertSeverityFor(AdherenceMissing, 1));
        assertEquals(alertThresholds.getThreshold(2).getAlertSeverity(), allAlertConfigurations.getAlertSeverityFor(AdherenceMissing, 2));
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
        List<String> adherenceMissingWeeks = asList("1", "2", "6");
        String cumulativeMissedDoses = "10";
        String treatmentNotStartedDays = "10";
        AlertsPropertiesValues alertsPropertiesValues = new AlertsPropertiesValues();
        alertsPropertiesValues.setAdherenceMissingWeeks(adherenceMissingWeeks);
        alertsPropertiesValues.setCumulativeMissedDoses(cumulativeMissedDoses);
        alertsPropertiesValues.setTreatmentNotStartedDays(treatmentNotStartedDays);

        AllAlertConfigurations configurations = new AllAlertConfigurations(alertsPropertiesValues);
        Map<PatientAlertType, AlertConfiguration> alertConfigurationMap = configurations.alertConfigurationMap;
        assertEquals((Integer) 1, alertConfigurationMap.get(AdherenceMissing).getAlertThresholds().getThreshold(1).getThreshold());
        assertEquals((Integer)10, alertConfigurationMap.get(CumulativeMissedDoses).getAlertThresholds().getThreshold(10).getThreshold());
        assertEquals((Integer)10, alertConfigurationMap.get(TreatmentNotStarted).getAlertThresholds().getThreshold(10).getThreshold());
    }
}
