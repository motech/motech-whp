package org.motechproject.whp.adherence.audit.reporting;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.audit.domain.AdherenceAuditLog;
import org.motechproject.whp.reports.contract.adherence.AdherenceAuditLogDTO;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.motechproject.whp.adherence.domain.PillStatus.Taken;

public class AdherenceAuditLogMapperTest {

    private AdherenceAuditLogMapper adherenceAuditLogMapper;

    private DateTime now = DateUtil.now();

    @Before
    public void setUp() throws Exception {
        adherenceAuditLogMapper = new AdherenceAuditLogMapper();
    }


    @Test
    public void shouldMapAdherenceAuditLogToAdherenceAuditLogDTO() {

        AdherenceAuditLog adherenceAuditLog = new AdherenceAuditLog("patientId", "providerId", "tbId", now, now, "userId", 1, Taken, "channel");

        AdherenceAuditLogDTO adherenceAuditLogDTO = adherenceAuditLogMapper.map(adherenceAuditLog);

        assertThat(adherenceAuditLogDTO.getChannel(), is(adherenceAuditLog.getSourceOfChange()));
        assertThat(adherenceAuditLogDTO.getDoseDate(), is(adherenceAuditLog.getDoseDate().toDate()));
        assertThat(new DateTime(adherenceAuditLogDTO.getCreationTime()), is(adherenceAuditLog.getDoseDate()));
        assertThat(adherenceAuditLogDTO.getPatientId(), is(adherenceAuditLog.getPatientId()));
        assertThat(adherenceAuditLogDTO.getProviderId(), is(adherenceAuditLog.getProviderId()));
    }
}



