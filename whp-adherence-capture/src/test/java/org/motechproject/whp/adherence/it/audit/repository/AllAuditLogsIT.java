package org.motechproject.whp.adherence.it.audit.repository;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.audit.domain.AuditLog;
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
        List<AuditLog> auditLogs = allWeeklyAdherenceAuditLogs.getAll();
        assertEquals(0, auditLogs.size());

        AuditLog auditLog = new AuditLog()
                .withNumberOfDosesTaken(1)
                .withProviderId("raj")
                .withRemark("dose taken")
                .withUser("raj")
                .sourceOfChange("WEB")
                .withPatientId("aha0100009")
                .withTbId("elevenDigit");

        DateTime creationTime = auditLog.getCreationTime();
        assertNotNull(creationTime);

        allWeeklyAdherenceAuditLogs.add(auditLog);

        auditLogs = allWeeklyAdherenceAuditLogs.getAll();
        assertEquals(creationTime, auditLogs.get(0).getCreationTime());
        assertTrue(auditLog.equals(auditLogs.get(0)));
    }


    @Test
    public void shouldFindByTbIdsChronologically() throws Exception {
        DateTime now = DateUtil.now();

        List<String> tbIds = asList("tbId1", "tbId2");

        AuditLog auditLog1 = getLogFor(tbIds.get(0), "dose taken", now.minusMonths(1));
        AuditLog auditLog2 = getLogFor(tbIds.get(1), "dose taken", now);
        AuditLog auditLog3 = getLogFor(tbIds.get(0), "dose taken", now.plusMonths(1));
        AuditLog auditLogForTbIdNotInMatchCriteria = getLogFor("tbId3", "dose taken", now.minusMonths(1));

        allWeeklyAdherenceAuditLogs.add(auditLog1);
        allWeeklyAdherenceAuditLogs.add(auditLog2);
        allWeeklyAdherenceAuditLogs.add(auditLog3);
        allWeeklyAdherenceAuditLogs.add(auditLogForTbIdNotInMatchCriteria);

        List<AuditLog> result = allWeeklyAdherenceAuditLogs.findByTbIdsWithRemarks(tbIds);

        assertThat(result, is(asList(auditLog3, auditLog2, auditLog1)));
    }

    @Test
    public void shouldFindAuditsOnlyWithRemarks() {
        String tbId = "tbId1";
        DateTime now = DateUtil.now();

        AuditLog auditLog = getLogFor(tbId, "dose taken", now.minusMonths(1));
        AuditLog auditLogWithBlankRemarkValue = getLogFor(tbId, "", now);
        AuditLog auditLogWithNullRemarkValue = getLogFor(tbId, null, now);

        allWeeklyAdherenceAuditLogs.add(auditLog);
        allWeeklyAdherenceAuditLogs.add(auditLogWithBlankRemarkValue);
        allWeeklyAdherenceAuditLogs.add(auditLogWithNullRemarkValue);

        List<AuditLog> result = allWeeklyAdherenceAuditLogs.findByTbIdsWithRemarks(asList(tbId));

        assertThat(result, is(asList(auditLog)));

    }

    private AuditLog getLogFor(String tbId, String remark, DateTime creationTime) {
        AuditLog auditLog=new AuditLog()
                .withNumberOfDosesTaken(1)
                .withProviderId("raj")
                .withRemark(remark)
                .withPatientId("patient1")
                .withTbId(tbId);
        auditLog.setCreationTime(creationTime);

        return auditLog;
    }

    @After
    public void tearDown(){
        super.after();
        markForDeletion(allWeeklyAdherenceAuditLogs.getAll().toArray());
    }
}
