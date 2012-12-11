package org.motechproject.whp.providerreminder.configuration;

import lombok.Data;
import org.joda.time.LocalDateTime;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.providerreminder.domain.ProviderReminderType;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class ProviderReminderConfiguration {
    @NotNull
    private DayOfWeek dayOfWeek;
    @NotNull
    private int hour;
    @NotNull
    private int minute;
    @NotNull
    private ProviderReminderType reminderType;

    public ProviderReminderConfiguration() {
    }

    public ProviderReminderConfiguration(ProviderReminderType reminderType, Date date) {
        LocalDateTime localDateTime = new LocalDateTime(date);
        this.reminderType = reminderType;
        this.dayOfWeek = DayOfWeek.getDayOfWeek(localDateTime.getDayOfWeek());
        this.hour = localDateTime.getHourOfDay();
        this.minute = localDateTime.getMinuteOfHour();
    }
}
