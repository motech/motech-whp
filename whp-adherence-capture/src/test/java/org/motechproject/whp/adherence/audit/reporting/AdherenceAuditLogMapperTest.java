package org.motechproject.whp.adherence.audit.reporting;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.audit.domain.AuditLog;
import org.motechproject.whp.adherence.audit.domain.DailyAdherenceAuditLog;
import org.motechproject.whp.adherence.domain.PillStatus;
import org.motechproject.whp.common.util.WHPDateUtil;
import org.motechproject.whp.reports.contract.adherence.AdherenceAuditLogDTO;
import org.motechproject.whp.reports.contract.enums.YesNo;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class AdherenceAuditLogMapperTest {

    private AdherenceAuditLogMapper adherenceAuditLogMapper;

    private DateTime now = DateUtil.now();

    @Before
    public void setUp() throws Exception {
        adherenceAuditLogMapper = new AdherenceAuditLogMapper();
    }


    @Test
    public void shouldMapAuditLogToAdherenceAuditLogDTO() {

        AuditLog auditLog = new AuditLog(now, 2, "remarks", "channel", "patientId", "tbId", "providerId", "user");

        AdherenceAuditLogDTO adherenceAuditLogDTO = adherenceAuditLogMapper.mapFromAuditLog(auditLog);

        assertThat(adherenceAuditLogDTO.getChannel(), is(auditLog.getSourceOfChange()));
        assertThat(adherenceAuditLogDTO.getTbId(), is(auditLog.getTbId()));
        assertThat(new DateTime(adherenceAuditLogDTO.getCreationTime()), is(auditLog.getCreationTime()));
        assertThat(adherenceAuditLogDTO.getPatientId(), is(auditLog.getPatientId()));
        assertThat(adherenceAuditLogDTO.getProviderId(), is(auditLog.getProviderId()));
        assertThat(adherenceAuditLogDTO.getUserId(), is(auditLog.getUser()));
        assertThat(adherenceAuditLogDTO.getIsGivenByProvider(), is(YesNo.Yes.code()));
    }


    @Test
    public void shouldMapDailyAdherenceAuditLogToAdherenceAuditLogDTO() {

        DailyAdherenceAuditLog dailyAdherenceAuditLog = new DailyAdherenceAuditLog("patientId", "tbId",now.toLocalDate(), PillStatus.Taken,"user","channel", now ,"providerId");

        AdherenceAuditLogDTO adherenceAuditLogDTO = adherenceAuditLogMapper.mapFromDailyAdherenceAuditLog(dailyAdherenceAuditLog);

        assertThat(adherenceAuditLogDTO.getChannel(), is(dailyAdherenceAuditLog.getSourceOfChange()));
        assertThat(adherenceAuditLogDTO.getTbId(), is(dailyAdherenceAuditLog.getTbId()));
        assertThat(new DateTime(adherenceAuditLogDTO.getCreationTime()), is(dailyAdherenceAuditLog.getCreationTime()));
        assertThat(adherenceAuditLogDTO.getPatientId(), is(dailyAdherenceAuditLog.getPatientId()));
        assertThat(adherenceAuditLogDTO.getProviderId(), is(dailyAdherenceAuditLog.getProviderId()));
        assertThat(adherenceAuditLogDTO.getUserId(), is(dailyAdherenceAuditLog.getUser()));
        assertThat(adherenceAuditLogDTO.getPillStatus(), is(dailyAdherenceAuditLog.getPillStatus().name()));
        assertThat(adherenceAuditLogDTO.getDoseDate(), is(WHPDateUtil.toSqlDate(dailyAdherenceAuditLog.getPillDate())));
        assertThat(adherenceAuditLogDTO.getIsGivenByProvider(), is(YesNo.No.code()));
    }
}



