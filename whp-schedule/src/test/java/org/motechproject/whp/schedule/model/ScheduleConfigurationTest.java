package org.motechproject.whp.schedule.model;

import org.joda.time.LocalDateTime;
import org.junit.Test;
import org.motechproject.model.DayOfWeek;

import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.motechproject.whp.schedule.domain.ScheduleType.PROVIDER_ADHERENCE_NOT_REPORTED;
import static org.motechproject.whp.schedule.domain.ScheduleType.PROVIDER_ADHERENCE_WINDOW_COMMENCED;

public class ScheduleConfigurationTest {

    @Test
    public void testIdIsSameAsType() {
        ScheduleConfiguration reminder = new ScheduleConfiguration(PROVIDER_ADHERENCE_WINDOW_COMMENCED, null);
        assertEquals(PROVIDER_ADHERENCE_WINDOW_COMMENCED.name(), reminder.getId());
    }

    @Test
    public void testReminderType() {
        Date tuesday = new LocalDateTime(2012, 12, 11, 10, 30).toDate();
        ScheduleConfiguration reminder;

        reminder = new ScheduleConfiguration(PROVIDER_ADHERENCE_WINDOW_COMMENCED, tuesday);
        assertEquals(PROVIDER_ADHERENCE_WINDOW_COMMENCED, reminder.getScheduleType());
        reminder = new ScheduleConfiguration(PROVIDER_ADHERENCE_NOT_REPORTED, tuesday);
        assertEquals(PROVIDER_ADHERENCE_NOT_REPORTED, reminder.getScheduleType());
    }

    @Test
    public void testDayOfWeek() {
        Date tuesday = new LocalDateTime(2012, 12, 11, 10, 30).toDate();
        Date wednesday = new LocalDateTime(2012, 12, 12, 10, 30).toDate();
        ScheduleConfiguration reminder;

        reminder = new ScheduleConfiguration(PROVIDER_ADHERENCE_WINDOW_COMMENCED, tuesday);
        assertEquals(Arrays.asList(DayOfWeek.Tuesday), reminder.getDayOfWeek());
        reminder = new ScheduleConfiguration(PROVIDER_ADHERENCE_WINDOW_COMMENCED, wednesday);
        assertEquals(Arrays.asList(DayOfWeek.Wednesday), reminder.getDayOfWeek());
    }

    @Test
    public void testHourOfDay() {
        Date eleven = new LocalDateTime(2012, 12, 11, 11, 0).toDate();
        Date twelve = new LocalDateTime(2012, 12, 11, 12, 0).toDate();
        ScheduleConfiguration reminder;

        reminder = new ScheduleConfiguration(PROVIDER_ADHERENCE_WINDOW_COMMENCED, eleven);
        assertEquals(11, reminder.getHour());
        reminder = new ScheduleConfiguration(PROVIDER_ADHERENCE_WINDOW_COMMENCED, twelve);
        assertEquals(12, reminder.getHour());
    }

    @Test
    public void testMinuteOfHour() {
        Date ten = new LocalDateTime(2012, 12, 11, 11, 10).toDate();
        Date thirty = new LocalDateTime(2012, 12, 11, 11, 30).toDate();
        ScheduleConfiguration reminder;

        reminder = new ScheduleConfiguration(PROVIDER_ADHERENCE_WINDOW_COMMENCED, ten);
        assertEquals(10, reminder.getMinute());
        reminder = new ScheduleConfiguration(PROVIDER_ADHERENCE_WINDOW_COMMENCED, thirty);
        assertEquals(30, reminder.getMinute());
    }

    @Test
    public void testCronExpression() {
        Date tuesday = new LocalDateTime(2012, 12, 11, 11, 30).toDate();
        Date wednesday = new LocalDateTime(2012, 12, 12, 11, 10).toDate();
        ScheduleConfiguration reminder;

        reminder = new ScheduleConfiguration(PROVIDER_ADHERENCE_WINDOW_COMMENCED, tuesday);
        assertEquals("0 30 11 ? * TUE", reminder.generateCronExpression());
        reminder = new ScheduleConfiguration(PROVIDER_ADHERENCE_WINDOW_COMMENCED, wednesday);
        assertEquals("0 10 11 ? * WED", reminder.generateCronExpression());
    }
}
