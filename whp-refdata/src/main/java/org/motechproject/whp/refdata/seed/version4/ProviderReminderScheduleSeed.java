package org.motechproject.whp.refdata.seed.version4;

import org.motechproject.deliverytools.seed.Seed;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.schedule.domain.ScheduleType;
import org.motechproject.whp.schedule.model.ScheduleConfiguration;
import org.motechproject.whp.schedule.service.WHPSchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProviderReminderScheduleSeed {

    private WHPSchedulerService reminderSchedulerService;

    @Autowired
    public ProviderReminderScheduleSeed(WHPSchedulerService reminderSchedulerService) {
        this.reminderSchedulerService = reminderSchedulerService;
    }

    @Seed(priority = 0, version = "4.1")
    public void loadAdherenceWindowCommencedSeed() {
        ScheduleConfiguration adherenceWindowCommencedEvent = new ScheduleConfiguration();
        adherenceWindowCommencedEvent.setDayOfWeek(DayOfWeek.Saturday);
        adherenceWindowCommencedEvent.setScheduleType(ScheduleType.PROVIDER_ADHERENCE_WINDOW_COMMENCED);
        adherenceWindowCommencedEvent.setHour(14);
        adherenceWindowCommencedEvent.setMinute(30);

        reminderSchedulerService.scheduleEvent(adherenceWindowCommencedEvent);
    }

    @Seed(priority = 0, version = "4.1")
    public void loadAdherenceNotReportedSeed() {
        ScheduleConfiguration adherenceWindowCommencedEvent = new ScheduleConfiguration();
        adherenceWindowCommencedEvent.setDayOfWeek(DayOfWeek.Tuesday);
        adherenceWindowCommencedEvent.setScheduleType(ScheduleType.PROVIDER_ADHERENCE_NOT_REPORTED);
        adherenceWindowCommencedEvent.setHour(10);
        adherenceWindowCommencedEvent.setMinute(30);

        reminderSchedulerService.scheduleEvent(adherenceWindowCommencedEvent);
    }
}
