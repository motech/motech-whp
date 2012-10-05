package org.motechproject.whp.uimodel;

import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.motechproject.whp.adherence.domain.PillStatus;

@Setter
@EqualsAndHashCode
public class AdherenceLogSummary {

    private String patientId;
    private String tbId;
    private String creationTime;
    private String doseDate;
    private String userId;
    private Integer numberOfDosesTaken;
    private PillStatus pillStatus;
    private String sourceOfChange;

    public AdherenceLogSummary(String patientId, String tbId, String creationTime, String doseDate, String userId, Integer numberOfDosesTaken, PillStatus pillStatus, String sourceOfChange) {
        this.patientId = patientId;
        this.tbId = tbId;
        this.creationTime = creationTime;
        this.doseDate = doseDate;
        this.userId = userId;
        this.numberOfDosesTaken = numberOfDosesTaken;
        this.pillStatus = pillStatus;
        this.sourceOfChange = sourceOfChange;
    }

    public AdherenceLogSummary() {

    }
}
