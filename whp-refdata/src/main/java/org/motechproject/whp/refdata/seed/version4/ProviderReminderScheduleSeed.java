package org.motechproject.whp.refdata.seed.version4;

import org.motechproject.deliverytools.seed.Seed;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.providerreminder.domain.ProviderReminderType;
import org.motechproject.whp.providerreminder.model.ProviderReminderConfiguration;
import org.motechproject.whp.providerreminder.service.ProviderReminderScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProviderReminderScheduleSeed {

    private ProviderReminderScheduler reminderScheduler;

    @Autowired
    public ProviderReminderScheduleSeed(ProviderReminderScheduler reminderScheduler) {
        this.reminderScheduler = reminderScheduler;
    }

    @Seed(priority = 0, version = "4.1")
    public void loadAdherenceWindowCommencedSeed() {
        ProviderReminderConfiguration adherenceWindowCommencedEvent = new ProviderReminderConfiguration();
        adherenceWindowCommencedEvent.setDayOfWeek(DayOfWeek.Saturday);
        adherenceWindowCommencedEvent.setReminderType(ProviderReminderType.ADHERENCE_WINDOW_COMMENCED);
        adherenceWindowCommencedEvent.setHour(14);
        adherenceWindowCommencedEvent.setMinute(30);

        reminderScheduler.scheduleReminder(adherenceWindowCommencedEvent);
    }

    @Seed(priority = 0, version = "4.1")
    public void loadAdherenceNotReportedSeed() {
        ProviderReminderConfiguration adherenceWindowCommencedEvent = new ProviderReminderConfiguration();
        adherenceWindowCommencedEvent.setDayOfWeek(DayOfWeek.Tuesday);
        adherenceWindowCommencedEvent.setReminderType(ProviderReminderType.ADHERENCE_NOT_REPORTED);
        adherenceWindowCommencedEvent.setHour(10);
        adherenceWindowCommencedEvent.setMinute(30);

        reminderScheduler.scheduleReminder(adherenceWindowCommencedEvent);
    }
}
