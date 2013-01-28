package org.motechproject.whp.patient.alerts.scheduler;


import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.scheduler.MotechSchedulerService;
import org.motechproject.testing.utils.BaseUnitTest;
import org.motechproject.whp.common.event.EventKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.motechproject.util.DateUtil.now;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/applicationPatientContext.xml")

public class PatientAlertSchedulerIT extends BaseUnitTest {

    @Autowired
    PatientAlertScheduler patientAlertScheduler;

    @Autowired
    MotechSchedulerService motechSchedulerService;

    @Before
    public void setup() {
        Date now = new Date();
        mockCurrentDate(new DateTime(now));
    }

    @Test
    public void shouldSchedulePatientAlertJob() {
        DateTime now = now();
        String patientId = "patientId";
        patientAlertScheduler.scheduleJob(patientId);
        Date fromDate = now.minusDays(2).toDate();
        Date toDate = now.plusMonths(2).toDate();

        List<Date> timings = motechSchedulerService.getScheduledJobTimings(EventKeys.PATIENT_ALERTS_UPDATE, patientId, fromDate, toDate);
        assertTrue(!timings.isEmpty());

        Date expectedScheduleDate = now.plusDays(1).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0).toDate();
        assertEquals(expectedScheduleDate, timings.get(0));
    }

    @Test
    public void shouldUnSchedulePatientAlertJob() {
        DateTime now = now();
        String patientId = "patientId";
        patientAlertScheduler.scheduleJob(patientId);

        Date fromDate = now.minusDays(2).toDate();
        Date toDate = now.plusMonths(2).toDate();

        List<Date> timings = motechSchedulerService.getScheduledJobTimings(EventKeys.PATIENT_ALERTS_UPDATE, patientId, fromDate, toDate);
        assertTrue(!timings.isEmpty());

        patientAlertScheduler.unscheduleJob(patientId);

        List<Date> newTimings;
        try{
            newTimings = motechSchedulerService.getScheduledJobTimings(EventKeys.PATIENT_ALERTS_UPDATE, patientId, fromDate, toDate);
        } catch (NullPointerException e){
            return;
        }

        assertTrue(newTimings.isEmpty());
    }
}
