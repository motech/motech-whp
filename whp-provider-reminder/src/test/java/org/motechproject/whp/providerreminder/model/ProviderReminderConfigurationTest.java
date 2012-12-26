package org.motechproject.whp.providerreminder.model;

import org.joda.time.LocalDateTime;
import org.junit.Test;
import org.motechproject.model.DayOfWeek;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.motechproject.whp.providerreminder.domain.ProviderReminderType.ADHERENCE_NOT_REPORTED;
import static org.motechproject.whp.providerreminder.domain.ProviderReminderType.ADHERENCE_WINDOW_APPROACHING;

public class ProviderReminderConfigurationTest {

    @Test
    public void testIdIsSameAsType() {
        ProviderReminderConfiguration reminder = new ProviderReminderConfiguration(ADHERENCE_WINDOW_APPROACHING, null);
        assertEquals(ADHERENCE_WINDOW_APPROACHING.name(), reminder.getId());
    }

    @Test
    public void testReminderType() {
        Date tuesday = new LocalDateTime(2012, 12, 11, 10, 30).toDate();
        ProviderReminderConfiguration reminder;

        reminder = new ProviderReminderConfiguration(ADHERENCE_WINDOW_APPROACHING, tuesday);
        assertEquals(ADHERENCE_WINDOW_APPROACHING, reminder.getReminderType());
        reminder = new ProviderReminderConfiguration(ADHERENCE_NOT_REPORTED, tuesday);
        assertEquals(ADHERENCE_NOT_REPORTED, reminder.getReminderType());
    }

    @Test
    public void testDayOfWeek() {
        Date tuesday = new LocalDateTime(2012, 12, 11, 10, 30).toDate();
        Date wednesday = new LocalDateTime(2012, 12, 12, 10, 30).toDate();
        ProviderReminderConfiguration reminder;

        reminder = new ProviderReminderConfiguration(ADHERENCE_WINDOW_APPROACHING, tuesday);
        assertEquals(DayOfWeek.Tuesday, reminder.getDayOfWeek());
        reminder = new ProviderReminderConfiguration(ADHERENCE_WINDOW_APPROACHING, wednesday);
        assertEquals(DayOfWeek.Wednesday, reminder.getDayOfWeek());
    }

    @Test
    public void testHourOfDay() {
        Date eleven = new LocalDateTime(2012, 12, 11, 11, 0).toDate();
        Date twelve = new LocalDateTime(2012, 12, 11, 12, 0).toDate();
        ProviderReminderConfiguration reminder;

        reminder = new ProviderReminderConfiguration(ADHERENCE_WINDOW_APPROACHING, eleven);
        assertEquals(11, reminder.getHour());
        reminder = new ProviderReminderConfiguration(ADHERENCE_WINDOW_APPROACHING, twelve);
        assertEquals(12, reminder.getHour());
    }

    @Test
    public void testMinuteOfHour() {
        Date ten = new LocalDateTime(2012, 12, 11, 11, 10).toDate();
        Date thirty = new LocalDateTime(2012, 12, 11, 11, 30).toDate();
        ProviderReminderConfiguration reminder;

        reminder = new ProviderReminderConfiguration(ADHERENCE_WINDOW_APPROACHING, ten);
        assertEquals(10, reminder.getMinute());
        reminder = new ProviderReminderConfiguration(ADHERENCE_WINDOW_APPROACHING, thirty);
        assertEquals(30, reminder.getMinute());
    }

    @Test
    public void testCronExpression() {
        Date tuesday = new LocalDateTime(2012, 12, 11, 11, 30).toDate();
        Date wednesday = new LocalDateTime(2012, 12, 12, 11, 10).toDate();
        ProviderReminderConfiguration reminder;

        reminder = new ProviderReminderConfiguration(ADHERENCE_WINDOW_APPROACHING, tuesday);
        assertEquals("0 30 11 ? * TUE", reminder.generateCronExpression());
        reminder = new ProviderReminderConfiguration(ADHERENCE_WINDOW_APPROACHING, wednesday);
        assertEquals("0 10 11 ? * WED", reminder.generateCronExpression());
    }
}
