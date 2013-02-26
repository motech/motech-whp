package org.motechproject.whp.refdata.seed;

import org.motechproject.deliverytools.seed.Seed;
import org.motechproject.whp.adherence.audit.domain.AdherenceAuditLog;
import org.motechproject.whp.adherence.audit.service.AdherenceAuditService;
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.motechproject.whp.reports.contract.adherence.AdherenceAuditLogDTO;
import org.motechproject.whp.reports.contract.enums.YesNo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.motechproject.whp.common.util.WHPDateUtil.toSqlDate;
import static org.motechproject.whp.common.util.WHPDateUtil.toSqlTimestamp;

@Service
public class AdherenceAuditLogSeed {

    private final AdherenceAuditService adherenceAuditService;
    private final ReportingPublisherService reportingPublisherService;

    @Autowired
    public AdherenceAuditLogSeed(AdherenceAuditService adherenceAuditService, ReportingPublisherService reportingPublisherService) {
        this.adherenceAuditService = adherenceAuditService;
        this.reportingPublisherService = reportingPublisherService;
    }

    @Seed(priority = 0, version = "5.0")
    public void seed() {
        List<AdherenceAuditLog> adherenceAuditLogList;
        int pageNumber = 1;
        do{
            adherenceAuditLogList = adherenceAuditService.fetchAllAuditLogs(pageNumber++);
            reportAuditLogs(adherenceAuditLogList);
        } while(auditLogListIsNotEmpty(adherenceAuditLogList));

    }

    private boolean auditLogListIsNotEmpty(List<AdherenceAuditLog> adherenceAuditLogList) {
        return adherenceAuditLogList != null && adherenceAuditLogList.size() != 0;
    }

    private void reportAuditLogs(List<AdherenceAuditLog> adherenceAuditLogList) {
        for(AdherenceAuditLog adherenceAuditLog : adherenceAuditLogList){
            AdherenceAuditLogDTO adherenceAuditLogDTO = mapToReportingRequest(adherenceAuditLog);
            reportingPublisherService.reportAdherenceAuditLog(adherenceAuditLogDTO);
        }
    }

    public AdherenceAuditLogDTO mapToReportingRequest(AdherenceAuditLog adherenceAuditLog) {
        AdherenceAuditLogDTO adherenceAuditLogDTO = new AdherenceAuditLogDTO();
        adherenceAuditLogDTO.setCreationTime(toSqlTimestamp(adherenceAuditLog.getCreationTime()));
        adherenceAuditLogDTO.setDoseDate(toSqlDate(adherenceAuditLog.getDoseDate()));
        adherenceAuditLogDTO.setNumberOfDosesTaken(adherenceAuditLog.getNumberOfDosesTaken());
        adherenceAuditLogDTO.setPatientId(adherenceAuditLog.getPatientId());
        adherenceAuditLogDTO.setPillStatus(adherenceAuditLog.getPillStatusName());
        adherenceAuditLogDTO.setProviderId(adherenceAuditLog.getProviderId());
        adherenceAuditLogDTO.setTbId(adherenceAuditLog.getTbId());
        adherenceAuditLogDTO.setUserId(adherenceAuditLog.getUserId());
        adherenceAuditLogDTO.setChannel(adherenceAuditLog.getSourceOfChange());
        adherenceAuditLogDTO.setIsGivenByProvider(isGivenByProvider(adherenceAuditLog));

        return adherenceAuditLogDTO;
    }

    private String isGivenByProvider(AdherenceAuditLog adherenceAuditLog) {
        return adherenceAuditLog.getProviderId().equals(adherenceAuditLog.getUserId()) ?
                YesNo.Yes.code() : YesNo.No.code();
    }
}
