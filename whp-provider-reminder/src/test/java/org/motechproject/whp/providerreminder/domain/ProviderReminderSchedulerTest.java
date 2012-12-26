package org.motechproject.whp.providerreminder.domain;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.model.DayOfWeek;
import org.motechproject.scheduler.MotechSchedulerService;
import org.motechproject.scheduler.domain.CronSchedulableJob;
import org.motechproject.testing.utils.BaseUnitTest;
import org.motechproject.whp.providerreminder.model.ProviderReminderConfiguration;
import org.motechproject.whp.providerreminder.repository.AllProviderReminderConfigurations;
import org.motechproject.whp.providerreminder.service.ProviderReminderScheduler;

import java.util.ArrayList;
import java.util.Date;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.util.DateUtil.today;
import static org.motechproject.whp.common.event.EventKeys.ADHERENCE_WINDOW_APPROACHING_EVENT_NAME;
import static org.motechproject.whp.providerreminder.domain.ProviderReminderType.ADHERENCE_WINDOW_APPROACHING;

public class ProviderReminderSchedulerTest extends BaseUnitTest {
    @Mock
    private MotechSchedulerService motechSchedulerService;
    @Mock
    private AllProviderReminderConfigurations allProviderReminderConfigurations;

    private ProviderReminderScheduler providerReminderScheduler;

    @Before
    public void setUp() {
        initMocks(this);
        providerReminderScheduler = new ProviderReminderScheduler(motechSchedulerService, allProviderReminderConfigurations);
    }

    @Test
    public void shouldScheduleAJob() {
        DayOfWeek dayOfWeek = DayOfWeek.Sunday;
        int minutes = 30;
        int hour = 10;
        ProviderReminderConfiguration providerReminderConfiguration = createProviderReminderConfiguration(minutes, hour, dayOfWeek);

        providerReminderScheduler.scheduleReminder(providerReminderConfiguration);

        ArgumentCaptor<CronSchedulableJob> captor = ArgumentCaptor.forClass(CronSchedulableJob.class);
        verify(motechSchedulerService).scheduleJob(captor.capture());
        CronSchedulableJob job = captor.getValue();

        assertEquals(ADHERENCE_WINDOW_APPROACHING_EVENT_NAME, job.getMotechEvent().getSubject());
        assertEquals(ADHERENCE_WINDOW_APPROACHING.name(), job.getMotechEvent().getParameters().get(MotechSchedulerService.JOB_ID_KEY));
    }

    @Test
    public void shouldPersistReminderConfigurationUponScheduling() {
        ProviderReminderConfiguration currentConfiguration = createProviderReminderConfiguration(1, 1, DayOfWeek.Monday);

        when(allProviderReminderConfigurations.withType(ADHERENCE_WINDOW_APPROACHING)).thenReturn(currentConfiguration);
        providerReminderScheduler.scheduleReminder(currentConfiguration);
        assertEquals(currentConfiguration, providerReminderScheduler.configuration(ADHERENCE_WINDOW_APPROACHING));
        verify(allProviderReminderConfigurations).saveOrUpdate(currentConfiguration);
    }

    @Test
    public void shouldReturnNextScheduleForAJob() {
        String subject = ADHERENCE_WINDOW_APPROACHING_EVENT_NAME;
        String jobId = ADHERENCE_WINDOW_APPROACHING.name();

        mockCurrentDate(today());

        Date expectedNextFireTime = today().plusDays(2).toDate();

        when(motechSchedulerService.getScheduledJobTimings(eq(subject), eq(jobId), any(Date.class), any(Date.class))).thenReturn(asList(expectedNextFireTime));
        assertEquals(new ProviderReminderConfiguration(ADHERENCE_WINDOW_APPROACHING, expectedNextFireTime), providerReminderScheduler.getReminder(ADHERENCE_WINDOW_APPROACHING));

        when(motechSchedulerService.getScheduledJobTimings(eq(subject), eq(jobId), any(Date.class), any(Date.class))).thenReturn(new ArrayList<Date>());
        assertNull(providerReminderScheduler.getReminder(ADHERENCE_WINDOW_APPROACHING));
    }

    private ProviderReminderConfiguration createProviderReminderConfiguration(int minutes, int hour, DayOfWeek dayOfWeek) {
        ProviderReminderConfiguration providerReminderConfiguration = new ProviderReminderConfiguration();
        providerReminderConfiguration.setMinute(minutes);
        providerReminderConfiguration.setHour(hour);
        providerReminderConfiguration.setDayOfWeek(dayOfWeek);
        providerReminderConfiguration.setReminderType(ADHERENCE_WINDOW_APPROACHING);
        return providerReminderConfiguration;
    }
}
