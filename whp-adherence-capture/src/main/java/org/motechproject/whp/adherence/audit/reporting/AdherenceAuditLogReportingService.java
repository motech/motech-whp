package org.motechproject.whp.adherence.audit.reporting;

import org.motechproject.whp.adherence.audit.domain.AuditLog;
import org.motechproject.whp.adherence.audit.domain.DailyAdherenceAuditLog;
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.motechproject.whp.reports.contract.adherence.AdherenceAuditLogDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdherenceAuditLogReportingService {

    private ReportingPublisherService reportingPublisherService;
    private AdherenceAuditLogMapper adherenceAuditLogMapper;

    @Autowired
    public AdherenceAuditLogReportingService(ReportingPublisherService reportingPublisherService, AdherenceAuditLogMapper adherenceAuditLogMapper) {
        this.reportingPublisherService = reportingPublisherService;
        this.adherenceAuditLogMapper = adherenceAuditLogMapper;
    }

    public void reportAuditLog(AuditLog auditLog) {
       AdherenceAuditLogDTO adherenceAuditLogDTO = adherenceAuditLogMapper.mapFromAuditLog(auditLog);
       reportingPublisherService.reportAdherenceAuditLog(adherenceAuditLogDTO);
    }
    public void removeAuditLogs(List<AuditLog> auditLogs){
        List<AdherenceAuditLogDTO> adherenceAuditLogDTOs = new ArrayList<>();
        for(AuditLog auditLog : auditLogs){
            AdherenceAuditLogDTO adherenceAuditLogDTO = adherenceAuditLogMapper.mapFromAuditLog(auditLog);
            adherenceAuditLogDTOs.add(adherenceAuditLogDTO);
        }
        reportingPublisherService.reportAdherenceAuditLogDelete(adherenceAuditLogDTOs);
    }

    public void reportDailyAdherenceAuditLog(DailyAdherenceAuditLog dailyAdherenceAuditLog) {
        AdherenceAuditLogDTO adherenceAuditLogDTO = adherenceAuditLogMapper.mapFromDailyAdherenceAuditLog(dailyAdherenceAuditLog);
        reportingPublisherService.reportAdherenceAuditLog(adherenceAuditLogDTO);
    }

    public void removeDailyAdherenceAuditLogs(List<DailyAdherenceAuditLog> dailyAdherenceAuditLogs){
        List<AdherenceAuditLogDTO> adherenceAuditLogDTOs = new ArrayList<>();
        for(DailyAdherenceAuditLog dailyAdherenceAuditLog : dailyAdherenceAuditLogs){
            AdherenceAuditLogDTO adherenceAuditLogDTO = adherenceAuditLogMapper.mapFromDailyAdherenceAuditLog(dailyAdherenceAuditLog);
            adherenceAuditLogDTOs.add(adherenceAuditLogDTO);
        }
        reportingPublisherService.reportDailyAdherenceAuditLogsDelete(adherenceAuditLogDTOs);
    }
}
