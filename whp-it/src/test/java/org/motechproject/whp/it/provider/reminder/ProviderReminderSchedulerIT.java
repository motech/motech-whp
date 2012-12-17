package org.motechproject.whp.it.provider.reminder;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.model.DayOfWeek;
import org.motechproject.scheduler.MotechSchedulerService;
import org.motechproject.testing.utils.BaseUnitTest;
import org.motechproject.whp.common.event.EventKeys;
import org.motechproject.whp.providerreminder.model.ProviderReminderConfiguration;
import org.motechproject.whp.providerreminder.service.ProviderReminderScheduler;
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
import static org.motechproject.whp.providerreminder.domain.ProviderReminderType.ADHERENCE_WINDOW_APPROACHING;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/applicationProviderReminderContext.xml")
public class ProviderReminderSchedulerIT extends BaseUnitTest {

    @Autowired
    ProviderReminderScheduler providerReminderScheduler;

    @Autowired
    MotechSchedulerService motechSchedulerService;

    @Before
    public void setup() {
        Date now = new Date();
        mockCurrentDate(new DateTime(now));
    }

    @Test
    public void shouldScheduleAndRetrieveProviderReminderScheduleJob() {
        DateTime now = now();
        ProviderReminderConfiguration providerReminderConfiguration = createProviderReminderConfiguration(30, 10, DayOfWeek.Sunday);
        providerReminderScheduler.scheduleReminder(providerReminderConfiguration);

        String subject = EventKeys.ADHERENCE_WINDOW_APPROACHING_EVENT_NAME;
        Date fromDate = now.minusDays(2).toDate();
        Date toDate = now.plusMonths(2).toDate();

        assertEquals(providerReminderConfiguration, providerReminderScheduler.getReminder(ADHERENCE_WINDOW_APPROACHING));

        List<Date> timings = motechSchedulerService.getScheduledJobTimings(subject, ADHERENCE_WINDOW_APPROACHING.name(), fromDate, toDate);
        assertTrue(!timings.isEmpty());

        DateTime nextSunday = nextApplicableWeekDay(now, asList(DayOfWeek.Sunday));
        Date expectedScheduleDate = nextSunday.withHourOfDay(10).withMinuteOfHour(30).withSecondOfMinute(0).withMillisOfSecond(0).toDate();
        assertEquals(expectedScheduleDate, timings.get(0));
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
