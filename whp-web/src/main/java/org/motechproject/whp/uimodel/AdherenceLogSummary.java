package org.motechproject.whp.uimodel;

import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.motechproject.export.annotation.ExportValue;
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

    @ExportValue(index = 0)
    public String getPatientId() {
        return patientId;
    }

    @ExportValue(index = 1)
    public String getTbId() {
        return tbId;
    }

    @ExportValue(index = 2, column = "Log Creation Date")
    public String getCreationTime() {
        return creationTime;
    }

    @ExportValue(index = 3, column = "Adherence Date")
    public String getDoseDate() {
        return doseDate;
    }

    @ExportValue(index = 4, column = "User Id")
    public String getUserId() {
        return userId;
    }

    @ExportValue(index = 5, column = "Adherence value for week (submitted by provider)")
    public Integer getNumberOfDosesTaken() {
        return numberOfDosesTaken;
    }

    @ExportValue(index = 6, column = "Adherence value for day (submitted by admin)")
    public PillStatus getPillStatus() {
        return pillStatus;
    }

    @ExportValue(index = 7, column = "Source of change")
    public String getSourceOfChange() {
        return sourceOfChange;
    }
}
