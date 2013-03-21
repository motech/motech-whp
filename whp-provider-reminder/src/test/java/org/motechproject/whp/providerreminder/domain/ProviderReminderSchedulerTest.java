package org.motechproject.whp.providerreminder.domain;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.model.DayOfWeek;
import org.motechproject.scheduler.MotechSchedulerService;
import org.motechproject.scheduler.domain.CronSchedulableJob;
import org.motechproject.testing.utils.BaseUnitTest;
import org.motechproject.whp.common.event.EventKeys;
import org.motechproject.whp.providerreminder.model.ScheduleConfiguration;
import org.motechproject.whp.providerreminder.repository.AllScheduleConfigurations;
import org.motechproject.whp.providerreminder.service.WHPSchedulerService;

import java.util.ArrayList;
import java.util.Date;

import static java.util.Arrays.asList;
import static junit.framework.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.util.DateUtil.today;
import static org.motechproject.whp.common.event.EventKeys.ADHERENCE_WINDOW_COMMENCED_EVENT_NAME;
import static org.motechproject.whp.providerreminder.domain.ScheduleType.ADHERENCE_WINDOW_COMMENCED;

public class ProviderReminderSchedulerTest extends BaseUnitTest {
    @Mock
    private MotechSchedulerService motechSchedulerService;
    @Mock
    private AllScheduleConfigurations allScheduleConfigurations;

    private WHPSchedulerService WHPSchedulerService;

    @Before
    public void setUp() {
        initMocks(this);
        WHPSchedulerService = new WHPSchedulerService(motechSchedulerService, allScheduleConfigurations);
    }

    @Test
    public void shouldScheduleAJob() {
        DayOfWeek dayOfWeek = DayOfWeek.Sunday;
        int minutes = 30;
        int hour = 10;
        ScheduleConfiguration scheduleConfiguration = createProviderReminderConfiguration(minutes, hour, dayOfWeek);

        WHPSchedulerService.scheduleReminder(scheduleConfiguration);

        ArgumentCaptor<CronSchedulableJob> captor = ArgumentCaptor.forClass(CronSchedulableJob.class);
        verify(motechSchedulerService).scheduleJob(captor.capture());
        CronSchedulableJob job = captor.getValue();

        assertEquals(ADHERENCE_WINDOW_COMMENCED_EVENT_NAME, job.getMotechEvent().getSubject());
        assertEquals(ADHERENCE_WINDOW_COMMENCED.name(), job.getMotechEvent().getParameters().get(MotechSchedulerService.JOB_ID_KEY));
    }

    @Test
    public void shouldPersistReminderConfigurationUponScheduling() {
        ScheduleConfiguration currentConfiguration = createProviderReminderConfiguration(1, 1, DayOfWeek.Monday);

        when(allScheduleConfigurations.withType(ADHERENCE_WINDOW_COMMENCED)).thenReturn(currentConfiguration);

        WHPSchedulerService.scheduleReminder(currentConfiguration);
        assertEquals(currentConfiguration, WHPSchedulerService.configuration(ADHERENCE_WINDOW_COMMENCED));
        verify(allScheduleConfigurations).saveOrUpdate(currentConfiguration);
    }

    @Test
    public void shouldMarkConfigurationAsScheduledUponScheduling() {
        ScheduleConfiguration currentConfiguration = createProviderReminderConfiguration(1, 1, DayOfWeek.Monday);

        when(allScheduleConfigurations.withType(ADHERENCE_WINDOW_COMMENCED)).thenReturn(currentConfiguration);
        WHPSchedulerService.scheduleReminder(currentConfiguration);

        ArgumentCaptor<ScheduleConfiguration> captor = ArgumentCaptor.forClass(ScheduleConfiguration.class);
        verify(allScheduleConfigurations).saveOrUpdate(captor.capture());
        assertTrue(captor.getValue().isScheduled());
    }

    @Test
    public void shouldUnScheduleReminder() {
        ScheduleConfiguration scheduleConfiguration = createProviderReminderConfiguration(1, 1, DayOfWeek.Monday);
        WHPSchedulerService.unScheduleReminder(scheduleConfiguration);
        verify(motechSchedulerService).unscheduleJob(EventKeys.ADHERENCE_WINDOW_COMMENCED_EVENT_NAME, ADHERENCE_WINDOW_COMMENCED.name());
    }

    @Test
    public void shouldMarkConfigurationAsUnscheduledUponUnSchedulingReminder() {
        ScheduleConfiguration scheduleConfiguration = createProviderReminderConfiguration(1, 1, DayOfWeek.Monday);
        WHPSchedulerService.unScheduleReminder(scheduleConfiguration);

        ArgumentCaptor<ScheduleConfiguration> captor = ArgumentCaptor.forClass(ScheduleConfiguration.class);
        verify(allScheduleConfigurations).saveOrUpdate(captor.capture());
        assertFalse(captor.getValue().isScheduled());
    }

    @Test
    public void shouldReturnNextScheduleForAJob() {
        String subject = ADHERENCE_WINDOW_COMMENCED_EVENT_NAME;
        String jobId = ADHERENCE_WINDOW_COMMENCED.name();

        mockCurrentDate(today());
        Date expectedNextFireTime = today().plusDays(2).toDate();
        ScheduleConfiguration expectedConfiguration = new ScheduleConfiguration(ADHERENCE_WINDOW_COMMENCED, expectedNextFireTime);

        when(allScheduleConfigurations.withType(ADHERENCE_WINDOW_COMMENCED)).thenReturn(expectedConfiguration);
        when(motechSchedulerService.getScheduledJobTimings(eq(subject), eq(jobId), any(Date.class), any(Date.class))).thenReturn(asList(expectedNextFireTime));
        assertEquals(expectedConfiguration, WHPSchedulerService.getReminder(ADHERENCE_WINDOW_COMMENCED));

        when(motechSchedulerService.getScheduledJobTimings(eq(subject), eq(jobId), any(Date.class), any(Date.class))).thenReturn(new ArrayList<Date>());
        assertNull(WHPSchedulerService.getReminder(ADHERENCE_WINDOW_COMMENCED));
    }

    private ScheduleConfiguration createProviderReminderConfiguration(int minutes, int hour, DayOfWeek dayOfWeek) {
        ScheduleConfiguration scheduleConfiguration = new ScheduleConfiguration();
        scheduleConfiguration.setMinute(minutes);
        scheduleConfiguration.setHour(hour);
        scheduleConfiguration.setDayOfWeek(dayOfWeek);
        scheduleConfiguration.setReminderType(ADHERENCE_WINDOW_COMMENCED);
        return scheduleConfiguration;
    }
}
