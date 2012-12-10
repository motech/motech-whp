package org.motechproject.whp.providerreminder.domain;

import org.motechproject.event.MotechEvent;
import org.motechproject.scheduler.MotechSchedulerService;
import org.motechproject.scheduler.domain.CronSchedulableJob;
import org.motechproject.whp.providerreminder.configuration.ProviderReminderProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReminderScheduler {

    private MotechSchedulerService motechSchedulerService;
    private ProviderReminderProperties providerReminderProperties;

    @Autowired
    public ReminderScheduler(MotechSchedulerService motechSchedulerService, ProviderReminderProperties providerReminderProperties) {

        this.motechSchedulerService = motechSchedulerService;
        this.providerReminderProperties = providerReminderProperties;
    }

    public void scheduleJob() {
        MotechEvent motechEvent = new MotechEvent(ProviderReminderType.ADHERENCE_WINDOW_APPROACHING.name());
        motechEvent.getParameters().put(MotechSchedulerService.JOB_ID_KEY, ProviderReminderType.ADHERENCE_WINDOW_APPROACHING.name());
        motechSchedulerService.scheduleJob(new CronSchedulableJob(motechEvent, generateCronExpression()));
    }

    private String generateCronExpression() {
        String minutes = providerReminderProperties.getMinutes();
        String hour = providerReminderProperties.getHour();
        String weekDay = providerReminderProperties.getWeekDay();

        return String.format("0 %s %s ? * %s", minutes, hour, weekDay);
    }
}
