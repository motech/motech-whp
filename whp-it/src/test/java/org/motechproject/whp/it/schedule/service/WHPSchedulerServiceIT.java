package org.motechproject.whp.it.schedule.service;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.model.DayOfWeek;
import org.motechproject.scheduler.MotechSchedulerService;
import org.motechproject.testing.utils.BaseUnitTest;
import org.motechproject.whp.common.event.EventKeys;
import org.motechproject.whp.schedule.model.ScheduleConfiguration;
import org.motechproject.whp.schedule.service.WHPSchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.motechproject.util.DateUtil.nextApplicableWeekDay;
import static org.motechproject.util.DateUtil.now;
import static org.motechproject.whp.schedule.domain.ScheduleType.PROVIDER_ADHERENCE_WINDOW_COMMENCED;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/applicationITContext.xml")
public class WHPSchedulerServiceIT extends BaseUnitTest {

    @Autowired
    WHPSchedulerService whpSchedulerService;

    @Autowired
    MotechSchedulerService motechSchedulerService;

    @Before
    public void setup() {
        Date now = new Date();
        mockCurrentDate(new DateTime(now));
    }

    @Test
    public void shouldScheduleAndRetrieveScheduledJob() {
        DateTime now = now();
        DayOfWeek dayOfWeek = DayOfWeek.getDayOfWeek(now().plusDays(1).getDayOfWeek());
        ScheduleConfiguration scheduleConfiguration = createScheduleConfiguration(30, 10, dayOfWeek);
        whpSchedulerService.scheduleEvent(scheduleConfiguration);

        String subject = EventKeys.ADHERENCE_WINDOW_COMMENCED_EVENT_NAME;
        Date fromDate = now.minusDays(2).toDate();
        Date toDate = now.plusMonths(2).toDate();

        assertEquals(scheduleConfiguration, whpSchedulerService.getReminder(PROVIDER_ADHERENCE_WINDOW_COMMENCED));

        List<Date> timings = motechSchedulerService.getScheduledJobTimings(subject, PROVIDER_ADHERENCE_WINDOW_COMMENCED.name(), fromDate, toDate);
        assertTrue(!timings.isEmpty());

        DateTime nextWeek = nextApplicableWeekDay(now, asList(dayOfWeek));
        Date expectedScheduleDate = nextWeek.withHourOfDay(10).withMinuteOfHour(30).withSecondOfMinute(0).withMillisOfSecond(0).toDate();
        assertEquals(expectedScheduleDate, timings.get(0));
    }

    private ScheduleConfiguration createScheduleConfiguration(int minutes, int hour, DayOfWeek dayOfWeek) {
        ScheduleConfiguration scheduleConfiguration = new ScheduleConfiguration();
        scheduleConfiguration.setMinute(minutes);
        scheduleConfiguration.setHour(hour);
        scheduleConfiguration.setScheduled(false);
        scheduleConfiguration.setDayOfWeek(dayOfWeek);
        scheduleConfiguration.setScheduleType(PROVIDER_ADHERENCE_WINDOW_COMMENCED);
        return scheduleConfiguration;
    }

}
