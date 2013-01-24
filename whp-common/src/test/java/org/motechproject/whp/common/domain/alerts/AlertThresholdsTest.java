package org.motechproject.whp.common.domain.alerts;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class AlertThresholdsTest {

    @Test
    public void shouldReturnAlertThresholdForGivenValue() {
        AlertThresholds alertThresholds = new AlertThresholds(4, 8, 12);

        assertEquals(new AlertThreshold(0, 0), alertThresholds.getThreshold(2));
        assertEquals(new AlertThreshold(4, 1), alertThresholds.getThreshold(4));
        assertEquals(new AlertThreshold(8, 2), alertThresholds.getThreshold(9));
        assertEquals(new AlertThreshold(12, 3), alertThresholds.getThreshold(14));
    }

}
