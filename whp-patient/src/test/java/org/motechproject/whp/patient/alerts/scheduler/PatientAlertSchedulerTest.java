package org.motechproject.whp.patient.alerts.scheduler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.scheduler.MotechSchedulerService;
import org.motechproject.scheduler.domain.CronSchedulableJob;
import org.motechproject.whp.common.event.EventKeys;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.common.event.EventKeys.PATIENT_ALERTS_UPDATE;

public class PatientAlertSchedulerTest {
    @Mock
    private MotechSchedulerService motechSchedulerService;

    PatientAlertScheduler patientAlertScheduler;

    @Before
    public void setUp() {
        initMocks(this);
        patientAlertScheduler = new PatientAlertScheduler(motechSchedulerService);
    }

    @Test
    public void shouldScheduleAJob() {
        String patientId = "patientId";
        patientAlertScheduler.scheduleJob(patientId);

        ArgumentCaptor<CronSchedulableJob> captor = ArgumentCaptor.forClass(CronSchedulableJob.class);
        verify(motechSchedulerService).scheduleJob(captor.capture());
        CronSchedulableJob job = captor.getValue();

        assertEquals(PATIENT_ALERTS_UPDATE, job.getMotechEvent().getSubject());
        assertEquals(patientId, job.getMotechEvent().getParameters().get(MotechSchedulerService.JOB_ID_KEY));
    }


    @Test
    public void shouldUnScheduleJob() {
        String patientId = "patientId";
        patientAlertScheduler.unscheduleJob(patientId);

        verify(motechSchedulerService).unscheduleJob(EventKeys.PATIENT_ALERTS_UPDATE, patientId);
    }

}
