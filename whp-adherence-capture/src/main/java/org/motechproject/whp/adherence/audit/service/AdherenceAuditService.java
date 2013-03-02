package org.motechproject.whp.adherence.audit.service;

import org.motechproject.whp.adherence.audit.contract.AuditParams;
import org.motechproject.whp.adherence.audit.domain.AdherenceAuditLog;
import org.motechproject.whp.adherence.audit.domain.AuditLog;
import org.motechproject.whp.adherence.audit.domain.DailyAdherenceAuditLog;
import org.motechproject.whp.adherence.audit.reporting.AdherenceAuditLogReportingService;
import org.motechproject.whp.adherence.audit.repository.AllAdherenceAuditLogs;
import org.motechproject.whp.adherence.audit.repository.AllDailyAdherenceAuditLogs;
import org.motechproject.whp.adherence.audit.repository.AllWeeklyAdherenceAuditLogs;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.domain.WeeklyAdherenceSummary;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Treatment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdherenceAuditService {

    private AllWeeklyAdherenceAuditLogs allWeeklyAdherenceAuditLogs;
    private AllDailyAdherenceAuditLogs allDailyAdherenceAuditLogs;
    private AllAdherenceAuditLogs allAdherenceAuditLogs;
    private AdherenceAuditLogReportingService adherenceAuditLogReportingService;

    @Autowired
    public AdherenceAuditService(AllWeeklyAdherenceAuditLogs allWeeklyAdherenceAuditLogs, AllDailyAdherenceAuditLogs allDailyAdherenceAuditLogs, AllAdherenceAuditLogs allAdherenceAuditLogs, AdherenceAuditLogReportingService adherenceAuditLogReportingService) {
        this.allWeeklyAdherenceAuditLogs = allWeeklyAdherenceAuditLogs;
        this.allDailyAdherenceAuditLogs = allDailyAdherenceAuditLogs;
        this.allAdherenceAuditLogs = allAdherenceAuditLogs;
        this.adherenceAuditLogReportingService = adherenceAuditLogReportingService;
    }

    public void auditWeeklyAdherence(Patient patient, WeeklyAdherenceSummary weeklyAdherenceSummary, AuditParams auditParams) {
        Treatment currentTreatment = patient.getCurrentTherapy().getCurrentTreatment();
        AuditLog auditLog = new AuditLog()
                .withNumberOfDosesTaken(weeklyAdherenceSummary.getDosesTaken())
                .withProviderId(currentTreatment.getProviderId())
                .withRemark(auditParams.getRemarks() == null ? auditParams.getRemarks() : auditParams.getRemarks().trim())
                .withUser(auditParams.getUser())
                .sourceOfChange(auditParams.getSourceOfChange().name())
                .withPatientId(weeklyAdherenceSummary.getPatientId())
                .withTbId(currentTreatment.getTbId());
        allWeeklyAdherenceAuditLogs.add(auditLog);
        adherenceAuditLogReportingService.reportAuditLog(auditLog);
    }

    public List<AuditLog> fetchWeeklyAuditLogs() {
        return allWeeklyAdherenceAuditLogs.getAll();
    }

    public void auditDailyAdherence(Patient patient, List<Adherence> adherenceData, AuditParams auditParams) {
        for (Adherence adherence : adherenceData) {
            DailyAdherenceAuditLog auditLog = new DailyAdherenceAuditLog();
            auditLog.setPatientId(patient.getPatientId());
            auditLog.setTbId(adherence.getTbId());
            auditLog.setPillDate(adherence.getPillDate());
            auditLog.setPillStatus(adherence.getPillStatus());
            auditLog.setSourceOfChange(auditParams.getSourceOfChange().name());
            auditLog.setUser(auditParams.getUser());
            auditLog.setProviderId(adherence.getProviderId());
            allDailyAdherenceAuditLogs.add(auditLog);
            adherenceAuditLogReportingService.reportDailyAdherenceAuditLog(auditLog);
        }
    }

    public List<DailyAdherenceAuditLog> fetchDailyAuditLogs() {
        return allDailyAdherenceAuditLogs.getAll();
    }

    public List<AdherenceAuditLog> fetchAllAuditLogs(int pageNumber) {
        return allAdherenceAuditLogs.allLogs(pageNumber - 1, 10000);
    }
}
