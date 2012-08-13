package org.motechproject.whp.ivr.audit.repository;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.audit.domain.AuditLog;
import org.motechproject.whp.common.util.SpringIntegrationTest;
import org.motechproject.whp.ivr.audit.domain.FlashingRequestLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@ContextConfiguration(locations = "classpath*:/applicationIVRContext.xml")
public class AllFlashingRequestLogsTest extends SpringIntegrationTest {

    @Autowired
    AllFlashingRequestLogs allFlashingRequestLogs;

    @Test
    public void shouldLogAuditWithCreationTime() {
        List<FlashingRequestLog> flashingRequestLogs = allFlashingRequestLogs.getAll();
        assertEquals(0, flashingRequestLogs.size());

        FlashingRequestLog flashingRequestLog = new FlashingRequestLog("mobileNumber", DateUtil.now());
        flashingRequestLog.setProviderId("providerId");

        DateTime creationTime = flashingRequestLog.getCreationTime();

        allFlashingRequestLogs.add(flashingRequestLog);

        flashingRequestLogs = allFlashingRequestLogs.getAll();
        assertEquals(creationTime, flashingRequestLogs.get(0).getCreationTime());
        assertEquals(flashingRequestLog, flashingRequestLogs.get(0));
    }

    @After
    public void tearDown(){
        super.after();
        markForDeletion(allFlashingRequestLogs.getAll().toArray());
    }
}
