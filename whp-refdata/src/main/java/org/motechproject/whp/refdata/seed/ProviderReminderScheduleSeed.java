package org.motechproject.whp.refdata.seed;

import org.motechproject.deliverytools.seed.Seed;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.containermapping.domain.AdminContainerMapping;
import org.motechproject.whp.containermapping.domain.ContainerRange;
import org.motechproject.whp.containermapping.repository.AllAdminContainerMappings;
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
    public void load() {
        ProviderReminderConfiguration adherenceWindowApproachingEvent = new ProviderReminderConfiguration();
        adherenceWindowApproachingEvent.setDayOfWeek(DayOfWeek.Saturday);
        adherenceWindowApproachingEvent.setReminderType(ProviderReminderType.ADHERENCE_WINDOW_APPROACHING);
        adherenceWindowApproachingEvent.setHour(14);
        adherenceWindowApproachingEvent.setMinute(30);

        reminderScheduler.scheduleReminder(adherenceWindowApproachingEvent);
    }
}
