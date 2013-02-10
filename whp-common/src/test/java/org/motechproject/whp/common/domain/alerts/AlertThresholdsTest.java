package org.motechproject.whp.common.domain.alerts;

import org.junit.Test;
import org.motechproject.whp.common.service.AlertsPropertiesValues;

import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;

public class AlertThresholdsTest {

    AlertsPropertiesValues alertsPropertiesValues;

    @Test
    public void shouldReturnAlertThresholdForGivenValue() {
        List<String> adherenceMissingWeeks = asList("4", "8", "12");
        List<String> adherenceMissingWeeksColors = asList("yellow", "pink", "red");
        alertsPropertiesValues = new AlertsPropertiesValues();
        alertsPropertiesValues.setAdherenceMissingWeeks(adherenceMissingWeeks);
        alertsPropertiesValues.setAdherenceMissingSeverityColors(adherenceMissingWeeksColors);
        AlertThresholds alertThresholds = new AlertThresholds(alertsPropertiesValues.getAdherenceMissingWeeks(), alertsPropertiesValues.getAdherenceMissingSeverityColors());

        assertEquals(new AlertThreshold(0, 0, ""), alertThresholds.getThreshold(2));
        assertEquals(new AlertThreshold(4, 1, "yellow"), alertThresholds.getThreshold(4));
        assertEquals(new AlertThreshold(8, 2, "pink"), alertThresholds.getThreshold(9));
        assertEquals(new AlertThreshold(12, 3, "red"), alertThresholds.getThreshold(14));
    }

}
