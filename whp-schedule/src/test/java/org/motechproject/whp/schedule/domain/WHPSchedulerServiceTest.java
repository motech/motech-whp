package org.motechproject.whp.schedule.domain;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.scheduler.MotechSchedulerService.JOB_ID_KEY;
import static org.motechproject.util.DateUtil.today;
import static org.motechproject.whp.common.event.EventKeys.ADHERENCE_WINDOW_COMMENCED_EVENT_NAME;
import static org.motechproject.whp.common.event.EventKeys.SCHEDULE_CONFIGURATION_MESSAGE_ID;
import static org.motechproject.whp.schedule.domain.ScheduleType.PROVIDER_ADHERENCE_WINDOW_COMMENCED;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.event.EventRelay;
import org.motechproject.event.MotechEvent;
import org.motechproject.model.DayOfWeek;
import org.motechproject.scheduler.MotechSchedulerService;
import org.motechproject.scheduler.domain.CronSchedulableJob;
import org.motechproject.testing.utils.BaseUnitTest;
import org.motechproject.whp.common.event.EventKeys;
import org.motechproject.whp.schedule.model.ScheduleConfiguration;
import org.motechproject.whp.schedule.repository.AllScheduleConfigurations;
import org.motechproject.whp.schedule.service.WHPSchedulerService;

public class WHPSchedulerServiceTest extends BaseUnitTest {
    @Mock
    private MotechSchedulerService motechSchedulerService;
    @Mock
    private AllScheduleConfigurations allScheduleConfigurations;
    @Mock
    private EventRelay eventRelay;

    private WHPSchedulerService whpSchedulerService;

    @Before
    public void setUp() {
        initMocks(this);
        whpSchedulerService = new WHPSchedulerService(motechSchedulerService, allScheduleConfigurations, eventRelay);
    }

    @Test
    public void shouldScheduleAJob() {
        DayOfWeek dayOfWeek = DayOfWeek.Sunday;
        int minutes = 30;
        int hour = 10;
        ScheduleConfiguration scheduleConfiguration = createProviderReminderConfiguration(minutes, hour, dayOfWeek, "messageId");

        whpSchedulerService.scheduleEvent(scheduleConfiguration);

        ArgumentCaptor<CronSchedulableJob> captor = ArgumentCaptor.forClass(CronSchedulableJob.class);
        verify(motechSchedulerService).scheduleJob(captor.capture());
        CronSchedulableJob job = captor.getValue();

        assertEquals(ADHERENCE_WINDOW_COMMENCED_EVENT_NAME, job.getMotechEvent().getSubject());
        assertEquals(PROVIDER_ADHERENCE_WINDOW_COMMENCED.name(), job.getMotechEvent().getParameters().get(JOB_ID_KEY));
        assertEquals(scheduleConfiguration.getMessageId(), job.getMotechEvent().getParameters().get(SCHEDULE_CONFIGURATION_MESSAGE_ID));
    }

    @Test
    public void shouldPersistReminderConfigurationUponScheduling() {
        ScheduleConfiguration currentConfiguration = createProviderReminderConfiguration(1, 1, DayOfWeek.Monday, "messageId");

        when(allScheduleConfigurations.withType(PROVIDER_ADHERENCE_WINDOW_COMMENCED)).thenReturn(currentConfiguration);

        whpSchedulerService.scheduleEvent(currentConfiguration);
        assertEquals(currentConfiguration, whpSchedulerService.configuration(PROVIDER_ADHERENCE_WINDOW_COMMENCED));
        verify(allScheduleConfigurations).saveOrUpdate(currentConfiguration);
    }

    @Test
    public void shouldMarkConfigurationAsScheduledUponScheduling() {
        ScheduleConfiguration currentConfiguration = createProviderReminderConfiguration(1, 1, DayOfWeek.Monday, "messageId");

        when(allScheduleConfigurations.withType(PROVIDER_ADHERENCE_WINDOW_COMMENCED)).thenReturn(currentConfiguration);
        whpSchedulerService.scheduleEvent(currentConfiguration);

        ArgumentCaptor<ScheduleConfiguration> captor = ArgumentCaptor.forClass(ScheduleConfiguration.class);
        verify(allScheduleConfigurations).saveOrUpdate(captor.capture());
        assertTrue(captor.getValue().isScheduled());
    }

    @Test
    public void shouldUnScheduleReminder() {
        ScheduleConfiguration scheduleConfiguration = createProviderReminderConfiguration(1, 1, DayOfWeek.Monday, "messageId");
        whpSchedulerService.unScheduleReminder(scheduleConfiguration);
        verify(motechSchedulerService).unscheduleJob(EventKeys.ADHERENCE_WINDOW_COMMENCED_EVENT_NAME, PROVIDER_ADHERENCE_WINDOW_COMMENCED.name());
    }

    @Test
    public void shouldMarkConfigurationAsUnscheduledUponUnSchedulingReminder() {
        ScheduleConfiguration scheduleConfiguration = createProviderReminderConfiguration(1, 1, DayOfWeek.Monday, "messageId");
        whpSchedulerService.unScheduleReminder(scheduleConfiguration);

        ArgumentCaptor<ScheduleConfiguration> captor = ArgumentCaptor.forClass(ScheduleConfiguration.class);
        verify(allScheduleConfigurations).saveOrUpdate(captor.capture());
        assertFalse(captor.getValue().isScheduled());
    }

    @Test
    public void shouldReturnNextScheduleForAJob() {
        String subject = ADHERENCE_WINDOW_COMMENCED_EVENT_NAME;
        String jobId = PROVIDER_ADHERENCE_WINDOW_COMMENCED.name();

        mockCurrentDate(today());
        Date expectedNextFireTime = today().plusDays(2).toDate();
        ScheduleConfiguration expectedConfiguration = new ScheduleConfiguration(PROVIDER_ADHERENCE_WINDOW_COMMENCED, expectedNextFireTime);

        when(allScheduleConfigurations.withType(PROVIDER_ADHERENCE_WINDOW_COMMENCED)).thenReturn(expectedConfiguration);
        when(motechSchedulerService.getScheduledJobTimings(eq(subject), eq(jobId), any(Date.class), any(Date.class))).thenReturn(asList(expectedNextFireTime));
        assertEquals(expectedConfiguration, whpSchedulerService.getReminder(PROVIDER_ADHERENCE_WINDOW_COMMENCED));

        when(motechSchedulerService.getScheduledJobTimings(eq(subject), eq(jobId), any(Date.class), any(Date.class))).thenReturn(new ArrayList<Date>());
        assertNull(whpSchedulerService.getReminder(PROVIDER_ADHERENCE_WINDOW_COMMENCED));
    }

    @Test
    public void shouldSendMotechEventForGivenScheduleTypeAndMessageId() {
        HashMap<String, Object> parameters = new HashMap<>();
        ScheduleType patientIvrAlert = ScheduleType.PATIENT_IVR_ALERT;
        parameters.put(EventKeys.SCHEDULE_CONFIGURATION_MESSAGE_ID, "message");
        parameters.put(JOB_ID_KEY, patientIvrAlert.name());
        MotechEvent expectedMotechEvent = new MotechEvent(EventKeys.PATIENT_IVR_ALERT_EVENT_NAME, parameters);

        whpSchedulerService.execute(patientIvrAlert, "message");

        verify(eventRelay).sendEventMessage(expectedMotechEvent);
    }

    private ScheduleConfiguration createProviderReminderConfiguration(int minutes, int hour, DayOfWeek dayOfWeek, String messageId) {
        ScheduleConfiguration scheduleConfiguration = new ScheduleConfiguration();
        scheduleConfiguration.setMinute(minutes);
        scheduleConfiguration.setHour(hour);
        scheduleConfiguration.setDayOfWeek(Arrays.asList(dayOfWeek));
        scheduleConfiguration.setScheduleType(PROVIDER_ADHERENCE_WINDOW_COMMENCED);
        scheduleConfiguration.setMessageId(messageId);
        return scheduleConfiguration;
    }
}
