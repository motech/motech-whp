package org.motechproject.whp.providerreminder.service;

import org.motechproject.event.MotechEvent;
import org.motechproject.scheduler.MotechSchedulerService;
import org.motechproject.scheduler.domain.CronSchedulableJob;
import org.motechproject.whp.common.event.EventKeys;
import org.motechproject.whp.providerreminder.configuration.ProviderReminderConfiguration;
import org.motechproject.whp.providerreminder.domain.ProviderReminderType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReminderScheduler {

    private MotechSchedulerService motechSchedulerService;
    private ProviderReminderConfiguration providerReminderConfiguration;

    @Autowired
    public ReminderScheduler(MotechSchedulerService motechSchedulerService, ProviderReminderConfiguration providerReminderConfiguration) {

        this.motechSchedulerService = motechSchedulerService;
        this.providerReminderConfiguration = providerReminderConfiguration;
    }

    public void scheduleJob() {
        MotechEvent motechEvent = new MotechEvent(EventKeys.ADHERENCE_WINDOW_APPROACHING_SUBJECT);
        motechEvent.getParameters().put(MotechSchedulerService.JOB_ID_KEY, ProviderReminderType.ADHERENCE_WINDOW_APPROACHING.name());
        motechSchedulerService.scheduleJob(new CronSchedulableJob(motechEvent, generateCronExpression()));
    }

    private String generateCronExpression() {
        String minutes = providerReminderConfiguration.getMinutes();
        String hour = providerReminderConfiguration.getHour();
        String weekDay = providerReminderConfiguration.getWeekDay();

        return String.format("0 %s %s ? * %s", minutes, hour, weekDay);
    }
}
