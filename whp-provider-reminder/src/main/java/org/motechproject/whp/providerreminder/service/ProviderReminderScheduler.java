package org.motechproject.whp.providerreminder.service;

import org.joda.time.DateTime;
import org.motechproject.event.MotechEvent;
import org.motechproject.scheduler.MotechSchedulerService;
import org.motechproject.scheduler.domain.CronSchedulableJob;
import org.motechproject.whp.providerreminder.domain.ProviderReminderType;
import org.motechproject.whp.providerreminder.model.ProviderReminderConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.motechproject.scheduler.MotechSchedulerService.JOB_ID_KEY;
import static org.motechproject.util.DateUtil.now;

@Component
public class ProviderReminderScheduler {

    private MotechSchedulerService motechSchedulerService;

    @Autowired
    public ProviderReminderScheduler(MotechSchedulerService motechSchedulerService) {
        this.motechSchedulerService = motechSchedulerService;
    }

    public void scheduleReminder(ProviderReminderConfiguration providerReminderConfiguration) {
        MotechEvent motechEvent = new MotechEvent(providerReminderConfiguration.getReminderType().getEventSubject());
        motechEvent.getParameters().put(JOB_ID_KEY, providerReminderConfiguration.getReminderType().name());
        motechSchedulerService.scheduleJob(new CronSchedulableJob(motechEvent, generateCronExpression(providerReminderConfiguration)));
    }

    public ProviderReminderConfiguration getReminder(ProviderReminderType jobType) {
        DateTime now = now();
        Date today = now.toDate();
        Date nextWeek = now.toLocalDate().plusWeeks(1).toDate();
        List<Date> scheduledJobTimings = new ArrayList<>();
        try {
            scheduledJobTimings = motechSchedulerService.getScheduledJobTimings(jobType.getEventSubject(), jobType.name(), today, nextWeek);
        } catch (Exception e) {
            // no schedule found
        }
        if (scheduledJobTimings.isEmpty()) {
            return null;
        }
        return new ProviderReminderConfiguration(jobType, scheduledJobTimings.get(0));
    }

    private String generateCronExpression(ProviderReminderConfiguration providerReminderConfiguration) {
        int minutes = providerReminderConfiguration.getMinute();
        int hour = providerReminderConfiguration.getHour();
        String weekDay = providerReminderConfiguration.getDayOfWeek().getShortName();
        return String.format("0 %s %s ? * %s", minutes, hour, weekDay);
    }
}
