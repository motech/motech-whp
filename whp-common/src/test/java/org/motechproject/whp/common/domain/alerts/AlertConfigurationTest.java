package org.motechproject.whp.common.domain.alerts;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.testing.utils.BaseUnitTest;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.motechproject.model.DayOfWeek.Monday;
import static org.motechproject.model.DayOfWeek.Wednesday;
import static org.motechproject.whp.common.domain.alerts.PatientAlertType.AdherenceMissing;

public class AlertConfigurationTest extends BaseUnitTest{

    @Test
    public void shouldCheckIfAlertShouldBeRunOnCurrentDay() {
        AlertConfiguration alertConfiguration = new AlertConfiguration(AdherenceMissing, new AlertThresholds(1,2,6), asList(Monday, Wednesday));

        mockCurrentDate(new LocalDate(2013, 1, 25)); //friday
        assertFalse(alertConfiguration.shouldRunToday());

        mockCurrentDate(new LocalDate(2013, 1, 23));
        assertTrue(alertConfiguration.shouldRunToday());

        mockCurrentDate(new LocalDate(2013, 1, 21));
        assertTrue(alertConfiguration.shouldRunToday());
    }
}
