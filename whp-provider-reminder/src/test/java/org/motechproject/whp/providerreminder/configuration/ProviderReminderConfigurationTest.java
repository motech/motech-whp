package org.motechproject.whp.providerreminder.configuration;

import org.joda.time.LocalDateTime;
import org.junit.Test;
import org.motechproject.model.DayOfWeek;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.motechproject.whp.providerreminder.domain.ProviderReminderType.ADHERENCE_WINDOW_APPROACHING;

public class ProviderReminderConfigurationTest {

    @Test
    public void shouldCreateProviderReminderConfigurationFromDate() {
        Date tuesday = new LocalDateTime(2012, 12, 11, 10, 30).toDate();

        ProviderReminderConfiguration tuesdayReminderConfiguration = new ProviderReminderConfiguration(ADHERENCE_WINDOW_APPROACHING, tuesday);

        assertEquals(ADHERENCE_WINDOW_APPROACHING, tuesdayReminderConfiguration.getReminderType());
        assertEquals(DayOfWeek.Tuesday, tuesdayReminderConfiguration.getDayOfWeek());
        assertEquals(10, tuesdayReminderConfiguration.getHour());
        assertEquals(30, tuesdayReminderConfiguration.getMinute());
    }
}
