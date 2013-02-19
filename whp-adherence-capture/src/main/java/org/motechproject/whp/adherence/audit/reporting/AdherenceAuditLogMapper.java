package org.motechproject.whp.adherence.audit.reporting;

import org.motechproject.whp.adherence.audit.domain.AdherenceAuditLog;
import org.motechproject.whp.common.util.WHPDateUtil;
import org.motechproject.whp.reports.contract.adherence.AdherenceAuditLogDTO;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class AdherenceAuditLogMapper {

    public AdherenceAuditLogDTO map(AdherenceAuditLog adherenceAuditLog) {
        AdherenceAuditLogDTO adherenceAuditLogDTO = new AdherenceAuditLogDTO();
        adherenceAuditLogDTO.setPatientId(adherenceAuditLog.getPatientId());
        adherenceAuditLogDTO.setProviderId(adherenceAuditLog.getProviderId());
        adherenceAuditLogDTO.setTbId(adherenceAuditLog.getTbId());
        adherenceAuditLogDTO.setCreationTime(new Timestamp(adherenceAuditLog.getCreationTime().getMillis()));
        adherenceAuditLogDTO.setDoseDate(WHPDateUtil.toSqlDate(adherenceAuditLog.getDoseDate()));
        adherenceAuditLogDTO.setUserId(adherenceAuditLog.getUserId());
        adherenceAuditLogDTO.setNumberOfDosesTaken(adherenceAuditLog.getNumberOfDosesTaken());
        adherenceAuditLogDTO.setPillStatus(adherenceAuditLog.getPillStatus().name());
        adherenceAuditLogDTO.setChannel(adherenceAuditLog.getSourceOfChange());
        return adherenceAuditLogDTO;
    }
}

