package org.motechproject.whp.adherence.audit.reporting;

import org.motechproject.whp.adherence.audit.domain.AuditLog;
import org.motechproject.whp.adherence.audit.domain.DailyAdherenceAuditLog;
import org.motechproject.whp.reports.contract.adherence.AdherenceAuditLogDTO;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

import static org.motechproject.whp.common.util.WHPDateUtil.toSqlDate;

@Component
public class AdherenceAuditLogMapper {

    public AdherenceAuditLogDTO mapFromAuditLog(AuditLog auditLog) {
        AdherenceAuditLogDTO adherenceAuditLogDTO = new AdherenceAuditLogDTO();
        adherenceAuditLogDTO.setPatientId(auditLog.getPatientId());
        adherenceAuditLogDTO.setProviderId(auditLog.getProviderId());
        adherenceAuditLogDTO.setTbId(auditLog.getTbId());
        adherenceAuditLogDTO.setCreationTime(new Timestamp(auditLog.getCreationTime().getMillis()));

        adherenceAuditLogDTO.setDoseDate(null);

        adherenceAuditLogDTO.setUserId(auditLog.getUser());
        adherenceAuditLogDTO.setNumberOfDosesTaken(auditLog.getNumberOfDosesTaken());

        adherenceAuditLogDTO.setPillStatus(null);

        adherenceAuditLogDTO.setChannel(auditLog.getSourceOfChange());

        return adherenceAuditLogDTO;
    }

    public AdherenceAuditLogDTO mapFromDailyAdherenceAuditLog(DailyAdherenceAuditLog dailyAdherenceAuditLog) {
        AdherenceAuditLogDTO adherenceAuditLogDTO = new AdherenceAuditLogDTO();
        adherenceAuditLogDTO.setPatientId(dailyAdherenceAuditLog.getPatientId());
        adherenceAuditLogDTO.setProviderId(dailyAdherenceAuditLog.getProviderId());
        adherenceAuditLogDTO.setTbId(dailyAdherenceAuditLog.getTbId());
        adherenceAuditLogDTO.setCreationTime(new Timestamp(dailyAdherenceAuditLog.getCreationTime().getMillis()));
        adherenceAuditLogDTO.setDoseDate(toSqlDate(dailyAdherenceAuditLog.getPillDate()));
        adherenceAuditLogDTO.setUserId(dailyAdherenceAuditLog.getUser());

        adherenceAuditLogDTO.setNumberOfDosesTaken(null);

        adherenceAuditLogDTO.setPillStatus(dailyAdherenceAuditLog.getPillStatus().name());
        adherenceAuditLogDTO.setChannel(dailyAdherenceAuditLog.getSourceOfChange());

        return adherenceAuditLogDTO;
    }
}

