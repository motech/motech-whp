package org.motechproject.whp.providerreminder.domain;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.scheduler.MotechSchedulerService;
import org.motechproject.scheduler.domain.CronSchedulableJob;
import org.motechproject.whp.providerreminder.configuration.ProviderReminderProperties;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ReminderSchedulerTest {
    @Mock
    private MotechSchedulerService motechSchedulerService;
    @Mock
    private ProviderReminderProperties providerReminderProperties;
    private ReminderScheduler reminderScheduler;

    @Before
    public void setUp() {
        initMocks(this);
        reminderScheduler = new ReminderScheduler(motechSchedulerService, providerReminderProperties);
    }

    @Test
    public void shouldScheduleAJob() {
        when(providerReminderProperties.getWeekDay()).thenReturn("some_week_day");
        when(providerReminderProperties.getHour()).thenReturn("some_hour");
        when(providerReminderProperties.getMinutes()).thenReturn("some_minutes");

        reminderScheduler.scheduleJob();

        ArgumentCaptor<CronSchedulableJob> captor = ArgumentCaptor.forClass(CronSchedulableJob.class);
        verify(motechSchedulerService).scheduleJob(captor.capture());
        CronSchedulableJob job = captor.getValue();

        assertEquals(ProviderReminderType.ADHERENCE_WINDOW_APPROACHING.name(), job.getMotechEvent().getSubject());
        assertEquals(ProviderReminderType.ADHERENCE_WINDOW_APPROACHING.name(), job.getMotechEvent().getParameters().get(MotechSchedulerService.JOB_ID_KEY));
        assertEquals("0 some_minutes some_hour ? * some_week_day", job.getCronExpression());
    }
}
