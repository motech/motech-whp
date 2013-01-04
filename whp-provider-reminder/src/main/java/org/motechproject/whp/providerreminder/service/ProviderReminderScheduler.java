package org.motechproject.whp.providerreminder.service;

import org.motechproject.event.MotechEvent;
import org.motechproject.scheduler.MotechSchedulerService;
import org.motechproject.scheduler.domain.CronSchedulableJob;
import org.motechproject.whp.providerreminder.domain.ProviderReminderTimings;
import org.motechproject.whp.providerreminder.domain.ProviderReminderType;
import org.motechproject.whp.providerreminder.model.ProviderReminderConfiguration;
import org.motechproject.whp.providerreminder.repository.AllProviderReminderConfigurations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.motechproject.scheduler.MotechSchedulerService.JOB_ID_KEY;
import static org.motechproject.util.DateUtil.now;

@Component
public class ProviderReminderScheduler {

    private MotechSchedulerService motechSchedulerService;
    private AllProviderReminderConfigurations allProviderReminderConfigurations;

    @Autowired
    public ProviderReminderScheduler(MotechSchedulerService motechSchedulerService, AllProviderReminderConfigurations allProviderReminderConfigurations) {
        this.motechSchedulerService = motechSchedulerService;
        this.allProviderReminderConfigurations = allProviderReminderConfigurations;
    }

    public void scheduleReminder(ProviderReminderConfiguration reminderConfiguration) {
        schedule(reminderConfiguration);
        reminderConfiguration.setScheduled(true);
        allProviderReminderConfigurations.saveOrUpdate(reminderConfiguration);
    }

    public ProviderReminderConfiguration getReminder(ProviderReminderType jobType) {
        List<Date> scheduledJobTimings = getScheduleJobTimings(jobType, new ProviderReminderTimings(now()));
        if (scheduledJobTimings.isEmpty()) {
            return null;
        } else {
            ProviderReminderConfiguration configuration = allProviderReminderConfigurations.withType(jobType);
            configuration.updateDateTimeValues(scheduledJobTimings.get(0));
            return configuration;
        }
    }

    public ProviderReminderConfiguration configuration(ProviderReminderType reminderType) {
        return allProviderReminderConfigurations.withType(reminderType);
    }

    public void unScheduleReminder(ProviderReminderConfiguration configuration) {
        configuration.setScheduled(false);
        motechSchedulerService.unscheduleJob(configuration.getReminderType().getEventSubject(), configuration.getReminderType().name());
        allProviderReminderConfigurations.saveOrUpdate(configuration);
    }

    private void schedule(ProviderReminderConfiguration reminderConfiguration) {
        ProviderReminderType reminderType = reminderConfiguration.getReminderType();
        MotechEvent motechEvent = providerReminderEvent(reminderType);
        motechSchedulerService.scheduleJob(new CronSchedulableJob(motechEvent, reminderConfiguration.generateCronExpression()));
    }

    private MotechEvent providerReminderEvent(ProviderReminderType reminderType) {
        MotechEvent motechEvent = new MotechEvent(reminderType.getEventSubject());
        motechEvent.getParameters().put(JOB_ID_KEY, reminderType.name());
        return motechEvent;
    }

    private List<Date> getScheduleJobTimings(ProviderReminderType jobType, ProviderReminderTimings timings) {
        try {
            return motechSchedulerService.getScheduledJobTimings(jobType.getEventSubject(), jobType.name(), timings.getStartDate(), timings.getEndDate());
        } catch (Exception ignored) {
            return emptyList();
        }
    }
}
