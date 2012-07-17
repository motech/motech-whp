package org.motechproject.whp.adherence.audit.repository;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Test;
import org.motechproject.whp.adherence.audit.domain.AuditLog;
import org.motechproject.whp.adherence.audit.repository.AllAuditLogs;
import org.motechproject.whp.common.util.SpringIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.junit.Assert.*;

@ContextConfiguration(locations = "classpath*:/applicationWHPAdherenceContext.xml")
public class AllAuditLogsTest extends SpringIntegrationTest {

    @Autowired
    AllAuditLogs allAuditLogs;

    @Test
    public void shouldLogAuditWithCreationTime() {
        List<AuditLog> auditLogs = allAuditLogs.getAll();
        assertEquals(0, auditLogs.size());

        AuditLog auditLog = new AuditLog()
                .numberOfDosesTaken(1)
                .providerId("raj")
                .remark("dose taken")
                .user("raj")
                .sourceOfChange("WEB")
                .patientId("aha0100009")
                .tbId("elevenDigit");

        DateTime creationTime = auditLog.getCreationTime();
        assertNotNull(creationTime);

        allAuditLogs.add(auditLog);

        auditLogs = allAuditLogs.getAll();
        assertEquals(creationTime, auditLogs.get(0).getCreationTime());
        assertTrue(auditLog.equals(auditLogs.get(0)));
    }

    @After
    public void tearDown(){
        super.after();
        markForDeletion(allAuditLogs.getAll().toArray());
    }
}
