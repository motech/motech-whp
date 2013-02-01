package org.motechproject.whp.common.domain.alerts;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.testing.utils.BaseUnitTest;
import org.motechproject.whp.common.service.AlertsPropertiesValues;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.motechproject.model.DayOfWeek.Monday;
import static org.motechproject.model.DayOfWeek.Wednesday;
import static org.motechproject.whp.common.domain.alerts.PatientAlertType.AdherenceMissing;

public class AlertConfigurationTest extends BaseUnitTest{

    AlertsPropertiesValues alertsPropertiesValues;

    @Test
    public void shouldCheckIfAlertShouldBeRunOnCurrentDay() {
        List<String> adherenceMissingWeeks = asList("1", "2", "6");
        alertsPropertiesValues = new AlertsPropertiesValues();
        alertsPropertiesValues.setAdherenceMissingWeeks(adherenceMissingWeeks);

        AlertConfiguration alertConfiguration = new AlertConfiguration(AdherenceMissing, new AlertThresholds(alertsPropertiesValues.getAdherenceMissingWeeks()), asList(Monday, Wednesday));

        mockCurrentDate(new LocalDate(2013, 1, 25)); //friday
        assertFalse(alertConfiguration.shouldRunToday());

        mockCurrentDate(new LocalDate(2013, 1, 23));
        assertTrue(alertConfiguration.shouldRunToday());

        mockCurrentDate(new LocalDate(2013, 1, 21));
        assertTrue(alertConfiguration.shouldRunToday());
    }
}
