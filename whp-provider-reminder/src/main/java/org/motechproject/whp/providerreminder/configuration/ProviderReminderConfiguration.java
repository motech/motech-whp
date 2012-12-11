package org.motechproject.whp.providerreminder.configuration;

import lombok.Data;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.providerreminder.domain.ProviderReminderType;

@Data
public class ProviderReminderConfiguration {
    private DayOfWeek dayOfWeek;
    private String hour;
    private String minutes;
    private ProviderReminderType reminderType;
}
