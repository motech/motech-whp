package org.motechproject.whp.providerreminder.domain;

import org.joda.time.DateTime;
import org.junit.Test;

import java.util.Date;

import static junit.framework.Assert.assertEquals;
import static org.motechproject.model.DayOfWeek.Wednesday;
import static org.motechproject.util.DateUtil.newDateTime;

public class ProviderReminderTimingsTest {

    @Test
    public void testStartDateWithScheduledDayOfWeekGreaterThanCurrentDayOfWeek() {
        int scheduledDayOfWeek = Wednesday.getValue();
        DateTime tuesday = newDateTime(2012, 12, 25);
        Date startDate = new ProviderReminderTimings(tuesday).getStartDate();
        assertEquals(scheduledDayOfWeek, new DateTime(startDate).getDayOfWeek());
    }

    @Test
    public void testStartDateWithScheduledTimeLessThanCurrentTimeOnSameDayOfWeek() {

    }

    @Test
    public void testStartDateWithScheduledTimeGreaterThanCurrentTimeOnSameDayOfWeek() {

    }

    @Test
    public void testStartDateWithScheduledTimeEqualToCurrentTimeOnSameDayOfWeek() {

    }

    @Test
    public void testStartDateWithScheduledDayOfWeekLessThanCurrentDayOfWeek() {
        int scheduledDayOfWeek = Wednesday.getValue();
        DateTime tuesday = newDateTime(2012, 12, 25);
        Date startDate = new ProviderReminderTimings(tuesday).getStartDate();
        assertEquals(scheduledDayOfWeek, new DateTime(startDate).getDayOfWeek());
    }

    @Test
    public void testEndDate() {

    }
}
