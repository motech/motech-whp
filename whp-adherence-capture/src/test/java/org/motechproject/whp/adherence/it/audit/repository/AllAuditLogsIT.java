package org.motechproject.whp.adherence.it.audit.repository;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.audit.domain.WeeklyAdherenceAuditLog;
import org.motechproject.whp.adherence.audit.repository.AllWeeklyAdherenceAuditLogs;
import org.motechproject.whp.common.util.SpringIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

@ContextConfiguration(locations = "classpath*:/applicationWHPAdherenceContext.xml")
public class AllAuditLogsIT extends SpringIntegrationTest {

    @Autowired
    AllWeeklyAdherenceAuditLogs allWeeklyAdherenceAuditLogs;

    @Test
    public void shouldLogAuditWithCreationTime() {
        List<WeeklyAdherenceAuditLog> weeklyAdherenceAuditLogs = allWeeklyAdherenceAuditLogs.getAll();
        assertEquals(0, weeklyAdherenceAuditLogs.size());

        WeeklyAdherenceAuditLog weeklyAdherenceAuditLog = new WeeklyAdherenceAuditLog()
                .withNumberOfDosesTaken(1)
                .withProviderId("raj")
                .withRemark("dose taken")
                .withUser("raj")
                .sourceOfChange("WEB")
                .withPatientId("aha0100009")
                .withTbId("elevenDigit");

        DateTime creationTime = weeklyAdherenceAuditLog.getCreationTime();
        assertNotNull(creationTime);

        allWeeklyAdherenceAuditLogs.add(weeklyAdherenceAuditLog);

        weeklyAdherenceAuditLogs = allWeeklyAdherenceAuditLogs.getAll();
        assertEquals(creationTime, weeklyAdherenceAuditLogs.get(0).getCreationTime());
        assertTrue(weeklyAdherenceAuditLog.equals(weeklyAdherenceAuditLogs.get(0)));
    }


    @Test
    public void shouldFindByTbIdsChronologically() throws Exception {
        DateTime now = DateUtil.now();

        List<String> tbIds = asList("tbId1", "tbId2");

        WeeklyAdherenceAuditLog weeklyAdherenceAuditLog1 = getLogFor(tbIds.get(0), "dose taken", now.minusMonths(1));
        WeeklyAdherenceAuditLog weeklyAdherenceAuditLog2 = getLogFor(tbIds.get(1), "dose taken", now);
        WeeklyAdherenceAuditLog weeklyAdherenceAuditLog3 = getLogFor(tbIds.get(0), "dose taken", now.plusMonths(1));
        WeeklyAdherenceAuditLog weeklyAdherenceAuditLogForTbIdNotInMatchCriteria = getLogFor("tbId3", "dose taken", now.minusMonths(1));

        allWeeklyAdherenceAuditLogs.add(weeklyAdherenceAuditLog1);
        allWeeklyAdherenceAuditLogs.add(weeklyAdherenceAuditLog2);
        allWeeklyAdherenceAuditLogs.add(weeklyAdherenceAuditLog3);
        allWeeklyAdherenceAuditLogs.add(weeklyAdherenceAuditLogForTbIdNotInMatchCriteria);

        List<WeeklyAdherenceAuditLog> result = allWeeklyAdherenceAuditLogs.findByTbIdsWithRemarks(tbIds);

        assertThat(result, is(asList(weeklyAdherenceAuditLog3, weeklyAdherenceAuditLog2, weeklyAdherenceAuditLog1)));
    }

    @Test
    public void shouldFindAuditsOnlyWithRemarks() {
        String tbId = "tbId1";
        DateTime now = DateUtil.now();

        WeeklyAdherenceAuditLog weeklyAdherenceAuditLog = getLogFor(tbId, "dose taken", now.minusMonths(1));
        WeeklyAdherenceAuditLog weeklyAdherenceAuditLogWithBlankRemarkValue = getLogFor(tbId, "", now);
        WeeklyAdherenceAuditLog weeklyAdherenceAuditLogWithNullRemarkValue = getLogFor(tbId, null, now);

        allWeeklyAdherenceAuditLogs.add(weeklyAdherenceAuditLog);
        allWeeklyAdherenceAuditLogs.add(weeklyAdherenceAuditLogWithBlankRemarkValue);
        allWeeklyAdherenceAuditLogs.add(weeklyAdherenceAuditLogWithNullRemarkValue);

        List<WeeklyAdherenceAuditLog> result = allWeeklyAdherenceAuditLogs.findByTbIdsWithRemarks(asList(tbId));

        assertThat(result, is(asList(weeklyAdherenceAuditLog)));

    }

    private WeeklyAdherenceAuditLog getLogFor(String tbId, String remark, DateTime creationTime) {
        WeeklyAdherenceAuditLog weeklyAdherenceAuditLog =new WeeklyAdherenceAuditLog()
                .withNumberOfDosesTaken(1)
                .withProviderId("raj")
                .withRemark(remark)
                .withPatientId("patient1")
                .withTbId(tbId);
        weeklyAdherenceAuditLog.setCreationTime(creationTime);

        return weeklyAdherenceAuditLog;
    }

    @After
    public void tearDown(){
        super.after();
        markForDeletion(allWeeklyAdherenceAuditLogs.getAll().toArray());
    }
}
