package org.motechproject.whp.adherence.it.audit.repository;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.audit.domain.AdherenceAuditLog;
import org.motechproject.whp.adherence.audit.domain.AuditLog;
import org.motechproject.whp.adherence.audit.domain.DailyAdherenceAuditLog;
import org.motechproject.whp.adherence.audit.repository.AllAdherenceAuditLogs;
import org.motechproject.whp.adherence.audit.repository.AllDailyAdherenceAuditLogs;
import org.motechproject.whp.adherence.audit.repository.AllWeeklyAdherenceAuditLogs;
import org.motechproject.whp.adherence.domain.PillStatus;
import org.motechproject.whp.common.util.SpringIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.joda.time.DateTimeZone.UTC;
import static org.motechproject.whp.adherence.domain.AdherenceSource.IVR;
import static org.motechproject.whp.adherence.domain.AdherenceSource.WEB;

@ContextConfiguration(locations = "classpath*:/applicationWHPAdherenceContext.xml")
public class AllAdherenceAuditLogsIT extends SpringIntegrationTest {

    @Autowired
    AllWeeklyAdherenceAuditLogs allWeeklyAdherenceAuditLogs;
    @Autowired
    AllDailyAdherenceAuditLogs allDailyAdherenceAuditLogs;

    @Autowired
    AllAdherenceAuditLogs allAdherenceAuditLogs;

    AuditLog weeklyAuditLog1 = new AuditLog(DateUtil.now(), 2, "remark1", IVR.name(), "patient1", "tbId1", "providerId1", "providerId1");
    AuditLog weeklyAuditLog2 = new AuditLog(DateUtil.now(), 3, "remark1", WEB.name(), "patient2", "tbId2", "providerId2", "providerId2");
    AuditLog weeklyAuditLog3 = new AuditLog(DateUtil.now(), 2, "remark1", IVR.name(), "patient1", "tbId1", "providerId1", "providerId1");

    //set up daily audit logs
    DailyAdherenceAuditLog dailyAuditLog1 = new DailyAdherenceAuditLog("patient1", "tbId1", DateUtil.today(), PillStatus.Taken, "cmfAdmin", WEB.name(), DateUtil.now());
    DailyAdherenceAuditLog dailyAuditLog2 = new DailyAdherenceAuditLog("patient2", "tbId2", DateUtil.today(), PillStatus.NotTaken, "cmfAdmin", WEB.name(), DateUtil.now());
    DailyAdherenceAuditLog dailyAuditLog3 = new DailyAdherenceAuditLog("patient1", "tbId1", DateUtil.today(), PillStatus.Unknown, "cmfAdmin", WEB.name(), DateUtil.now());

    @Test
    public void shouldGetDailyAndWeeklyAdherenceAuditLogs() {
        setUpWeeklyAuditLogs();

        setUpDailyAuditLogs();

        List<AdherenceAuditLog> adherenceAuditLogList = allAdherenceAuditLogs.findLogsAsOf(DateUtil.now(), 0, 10);
        assertThat(adherenceAuditLogList.size(), is(6));
        assertThat(adherenceAuditLogList, hasItems(getLog(weeklyAuditLog1), getLog(weeklyAuditLog2), getLog(weeklyAuditLog3)));
        assertThat(adherenceAuditLogList, hasItems(getLog(dailyAuditLog1),getLog(dailyAuditLog2),getLog(dailyAuditLog3)));

    }

    private AdherenceAuditLog getLog(DailyAdherenceAuditLog dailyAuditLog) {
        AdherenceAuditLog expectedAdherenceAuditLog = new AdherenceAuditLog(dailyAuditLog.getPatientId(), dailyAuditLog.getTbId(), dailyAuditLog.getCreationTime().toDateTime(UTC), new LocalDate().toDateTimeAtStartOfDay(UTC), dailyAuditLog.getUser(), null, dailyAuditLog.getPillStatus(),dailyAuditLog.getSourceOfChange() );
        return expectedAdherenceAuditLog;
    }

    private AdherenceAuditLog getLog(AuditLog weeklyAuditLog) {
        AdherenceAuditLog expectedAdherenceAuditLog = new AdherenceAuditLog(weeklyAuditLog.getPatientId(), weeklyAuditLog.getTbId(), weeklyAuditLog.getCreationTime().toDateTime(UTC), weeklyAuditLog.getCreationTime().toDateTime(DateTimeZone.UTC), weeklyAuditLog.getUser(), weeklyAuditLog.getNumberOfDosesTaken(), null, weeklyAuditLog.getSourceOfChange());
        return expectedAdherenceAuditLog;
    }

    private void setUpDailyAuditLogs() {
        allDailyAdherenceAuditLogs.add(dailyAuditLog1);
        allDailyAdherenceAuditLogs.add(dailyAuditLog2);
        allDailyAdherenceAuditLogs.add(dailyAuditLog3);
    }

    private void setUpWeeklyAuditLogs() {
        allWeeklyAdherenceAuditLogs.add(weeklyAuditLog1);
        allWeeklyAdherenceAuditLogs.add(weeklyAuditLog2);
        allWeeklyAdherenceAuditLogs.add(weeklyAuditLog3);
    }

    @Before
    @After
    public void setUpAndTearDown() {
        allWeeklyAdherenceAuditLogs.removeAll();
        allDailyAdherenceAuditLogs.removeAll();
    }
}
