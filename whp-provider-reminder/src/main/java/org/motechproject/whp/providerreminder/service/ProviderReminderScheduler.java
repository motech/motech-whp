package org.motechproject.whp.providerreminder.service;

import org.motechproject.event.MotechEvent;
import org.motechproject.scheduler.MotechSchedulerService;
import org.motechproject.scheduler.domain.CronSchedulableJob;
import org.motechproject.whp.common.event.EventKeys;
import org.motechproject.whp.providerreminder.configuration.ProviderReminderConfiguration;
import org.motechproject.whp.providerreminder.domain.ProviderReminderType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

import static org.motechproject.util.DateUtil.today;
import static org.motechproject.whp.providerreminder.domain.ProviderReminderType.ADHERENCE_WINDOW_APPROACHING;

@Component
public class ProviderReminderScheduler {
    private MotechSchedulerService motechSchedulerService;

    @Autowired
    public ProviderReminderScheduler(MotechSchedulerService motechSchedulerService) {
        this.motechSchedulerService = motechSchedulerService;
    }

    public void scheduleJob(ProviderReminderConfiguration providerReminderConfiguration) {
        MotechEvent motechEvent = new MotechEvent(providerReminderConfiguration.getReminderType().getEventSubject());
        motechEvent.getParameters().put(MotechSchedulerService.JOB_ID_KEY, providerReminderConfiguration.getReminderType().name());
        motechSchedulerService.scheduleJob(new CronSchedulableJob(motechEvent, generateCronExpression(providerReminderConfiguration)));
    }

    public Date getNextFireTime(ProviderReminderType jobType) {
        Date today = today().toDate();
        Date nextWeek = today().plusWeeks(1).toDate();
        List<Date> scheduledJobTimings = motechSchedulerService.getScheduledJobTimings(jobType.getEventSubject(), jobType.name(), today, nextWeek);

        if(scheduledJobTimings.isEmpty()){
            return null;
        }

        return scheduledJobTimings.get(0);
    }

    private String generateCronExpression(ProviderReminderConfiguration providerReminderConfiguration) {
        String minutes = providerReminderConfiguration.getMinutes();
        String hour = providerReminderConfiguration.getHour();
        String weekDay = providerReminderConfiguration.getDayOfWeek().getShortName();

        return String.format("0 %s %s ? * %s", minutes, hour, weekDay);
    }
}
