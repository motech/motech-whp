package org.motechproject.whp.adherence.audit.domain;

import org.junit.Test;
import org.motechproject.whp.adherence.domain.PillStatus;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AdherenceAuditLogTest {

    @Test
    public void shouldGetPillStatusName() {
        AdherenceAuditLog adherenceAuditLog = new AdherenceAuditLog();
        assertNull(adherenceAuditLog.getPillStatusName());

        adherenceAuditLog.setPillStatus(PillStatus.NotTaken);

        assertEquals(PillStatus.NotTaken.name(), adherenceAuditLog.getPillStatusName());
    }
}
