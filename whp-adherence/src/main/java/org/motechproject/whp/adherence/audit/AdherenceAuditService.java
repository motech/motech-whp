package org.motechproject.whp.adherence.audit;

import org.motechproject.whp.adherence.domain.WeeklyAdherence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdherenceAuditService {

    private AllAuditLogs allAuditLogs;

    @Autowired
    public AdherenceAuditService(AllAuditLogs allAuditLogs) {
        this.allAuditLogs = allAuditLogs;
    }

    public void log(String sourceOfChange, WeeklyAdherence weeklyAdherence) {
        AuditLog auditLog = new AuditLog()
                .numberOfDosesTaken(weeklyAdherence.numberOfDosesTaken())
                .remark(weeklyAdherence.getRemark())
                .sourceOfChange(sourceOfChange)
                .patientId(weeklyAdherence.getPatientId())
                .tbId(weeklyAdherence.getTbId());

        allAuditLogs.add(auditLog);
    }

    public List<AuditLog> fetchAuditLogs() {
        return allAuditLogs.getAll();
    }
}
