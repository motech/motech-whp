package org.motechproject.whp.it.provider.reminder;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kubek2k.springockito.annotations.ReplaceWithMock;
import org.kubek2k.springockito.annotations.SpringockitoContextLoader;
import org.motechproject.model.DayOfWeek;
import org.motechproject.scheduler.MotechSchedulerService;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.providerreminder.configuration.ProviderReminderProperties;
import org.motechproject.whp.providerreminder.domain.ProviderReminderType;
import org.motechproject.whp.providerreminder.domain.ReminderScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = SpringockitoContextLoader.class, locations = "classpath*:/applicationProviderReminderContext.xml")
public class ReminderSchedulerIT {

    @Autowired
    ReminderScheduler reminderScheduler;

    @Autowired
    MotechSchedulerService motechSchedulerService;

    @Autowired
    @ReplaceWithMock
    ProviderReminderProperties providerReminderProperties;

    @Before
    public void setUp() {
        initMocks(this);
        when(providerReminderProperties.getWeekDay()).thenReturn("SUN");
        when(providerReminderProperties.getHour()).thenReturn("10");
        when(providerReminderProperties.getMinutes()).thenReturn("30");
    }

    @Test
    public void shouldScheduleAJob() {
        reminderScheduler.scheduleJob();

        String subject = ProviderReminderType.ADHERENCE_WINDOW_APPROACHING.name();
        String jobId = ProviderReminderType.ADHERENCE_WINDOW_APPROACHING.name();
        Date fromDate = new LocalDate(new Date()).minusDays(2).toDate();
        Date toDate = new LocalDate(new Date()).plusMonths(2).toDate();

        List<Date> timings = motechSchedulerService.getScheduledJobTimings(subject, jobId, fromDate, toDate);

        assertTrue(!timings.isEmpty());

        DateTime nextSunday = DateUtil.nextApplicableWeekDay(new DateTime(new Date().getTime()), asList(DayOfWeek.Sunday));
        Date expectedScheduleDate = nextSunday.withHourOfDay(10).withMinuteOfHour(30).withSecondOfMinute(0).withMillisOfSecond(0).toDate();

        assertEquals(expectedScheduleDate, timings.get(0));
    }
}
