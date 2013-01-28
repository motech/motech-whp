package org.motechproject.whp.patient.alerts.scheduler;

import org.motechproject.event.MotechEvent;
import org.motechproject.scheduler.MotechSchedulerService;
import org.motechproject.scheduler.domain.CronSchedulableJob;
import org.motechproject.whp.common.event.EventKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.motechproject.scheduler.MotechSchedulerService.JOB_ID_KEY;

@Component
public class PatientAlertScheduler {

    private MotechSchedulerService motechSchedulerService;

    @Autowired
    public PatientAlertScheduler(MotechSchedulerService motechSchedulerService) {
        this.motechSchedulerService = motechSchedulerService;
    }

    public void scheduleJob(String patientId) {
        MotechEvent motechEvent = new MotechEvent(EventKeys.PATIENT_ALERTS_UPDATE);
        motechEvent.getParameters().put(JOB_ID_KEY, patientId);

        motechSchedulerService.scheduleJob(new CronSchedulableJob(motechEvent, allDaysOfWeekCronExpression()));
    }

    private String allDaysOfWeekCronExpression() {
        return String.format("0 0 0 ? * *");
    }

    public void unscheduleJob(String patientId) {
        motechSchedulerService.unscheduleJob(EventKeys.PATIENT_ALERTS_UPDATE, patientId);
    }
}
