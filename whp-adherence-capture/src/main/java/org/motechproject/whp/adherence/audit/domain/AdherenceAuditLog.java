package org.motechproject.whp.adherence.audit.domain;

import lombok.Data;
import org.joda.time.DateTime;
import org.motechproject.whp.adherence.domain.PillStatus;

@Data
public class AdherenceAuditLog {

    private String patientId;
    private String providerId;
    private String tbId;
    private DateTime creationTime;
    private DateTime doseDate;
    private String userId;
    private Integer numberOfDosesTaken;
    private PillStatus pillStatus;
    private String sourceOfChange;

    public AdherenceAuditLog() {
    }

    public AdherenceAuditLog(String patientId, String providerId, String tbId, DateTime creationTime, DateTime doseDate, String userId, Integer numberOfDosesTaken, PillStatus pillStatus, String sourceOfChange) {
        this.patientId = patientId;
        this.providerId = providerId;
        this.tbId = tbId;
        this.creationTime = creationTime;
        this.doseDate = doseDate;
        this.userId = userId;
        this.numberOfDosesTaken = numberOfDosesTaken;
        this.pillStatus = pillStatus;
        this.sourceOfChange = sourceOfChange;
    }
}
