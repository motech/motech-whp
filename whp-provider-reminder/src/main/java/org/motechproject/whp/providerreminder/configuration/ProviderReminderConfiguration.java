package org.motechproject.whp.providerreminder.configuration;

import lombok.Data;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.providerreminder.domain.ProviderReminderType;

import java.util.Date;

@Data
public class ProviderReminderConfiguration {
    private DayOfWeek dayOfWeek;
    private int hour;
    private int minutes;
    private ProviderReminderType reminderType;


    public ProviderReminderConfiguration() {
    }

    public ProviderReminderConfiguration(ProviderReminderType reminderType, Date date) {
        this.reminderType = reminderType;
        LocalDateTime localDateTime = new LocalDateTime(date);
        dayOfWeek = DayOfWeek.getDayOfWeek(localDateTime.getDayOfWeek());
        hour = localDateTime.getHourOfDay();
        minutes = localDateTime.getMinuteOfHour();
    }
}
