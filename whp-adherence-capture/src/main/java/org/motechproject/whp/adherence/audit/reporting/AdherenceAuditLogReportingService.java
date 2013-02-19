package org.motechproject.whp.adherence.audit.reporting;

import org.motechproject.whp.adherence.audit.domain.AdherenceAuditLog;
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.motechproject.whp.reports.contract.adherence.AdherenceAuditLogDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdherenceAuditLogReportingService {

    private ReportingPublisherService reportingPublisherService;
    private AdherenceAuditLogMapper adherenceAuditLogMapper;

    @Autowired
    public AdherenceAuditLogReportingService(ReportingPublisherService reportingPublisherService, AdherenceAuditLogMapper adherenceAuditLogMapper) {
        this.reportingPublisherService = reportingPublisherService;
        this.adherenceAuditLogMapper = adherenceAuditLogMapper;
    }

    public void reportAdherenceAuditLog(AdherenceAuditLog adherenceAuditLog) {
       AdherenceAuditLogDTO adherenceAuditLogDTO = adherenceAuditLogMapper.map(adherenceAuditLog);
       reportingPublisherService.reportAdherenceAuditLog(adherenceAuditLogDTO);
    }
}
