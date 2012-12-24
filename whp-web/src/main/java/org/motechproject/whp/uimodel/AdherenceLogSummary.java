package org.motechproject.whp.uimodel;

import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.motechproject.export.annotation.ExportValue;
import org.motechproject.whp.adherence.domain.PillStatus;
import org.motechproject.whp.common.util.WHPDate;

import java.util.Date;

@Setter
@EqualsAndHashCode
public class AdherenceLogSummary {

    private String patientId;
    private String tbId;
    private String creationDate;
    private String creationTime;
    private String doseDate;
    private String userId;
    private Integer numberOfDosesTaken;
    private PillStatus pillStatus;
    private String sourceOfChange;
    private String providerId;

    public AdherenceLogSummary(String patientId, String tbId, String creationDate, String creationTime, String doseDate, String userId, Integer numberOfDosesTaken, PillStatus pillStatus, String sourceOfChange, String providerId) {
        this.patientId = patientId;
        this.tbId = tbId;
        this.creationDate = creationDate;
        this.creationTime = creationTime;
        this.doseDate = doseDate;
        this.userId = userId;
        this.numberOfDosesTaken = numberOfDosesTaken;
        this.pillStatus = pillStatus;
        this.sourceOfChange = sourceOfChange;
        this.providerId = providerId;
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

    @ExportValue(index = 2)
    public String getProviderId() {
        return providerId;
    }

    @ExportValue(index = 3, column = "Log Creation Date")
    public Date getCreationDate() {
        if(creationDate  == null){
            return null;
        }
        return new WHPDate(creationDate).date().toDate();
    }

    @ExportValue(index = 4, column = "Log Creation Time")
    public String getCreationTime() {
        return creationTime;
    }

    @ExportValue(index = 5, column = "Adherence Date")
    public Date getDoseDate() {
        if(doseDate  == null){
            return null;
        }
        return new WHPDate(doseDate).date().toDate();
    }

    @ExportValue(index = 6, column = "User Id")
    public String getUserId() {
        return userId;
    }

    @ExportValue(index = 7, column = "Adherence value for week (submitted by provider)")
    public Integer getNumberOfDosesTaken() {
        return numberOfDosesTaken;
    }

    @ExportValue(index = 8, column = "Adherence value for day (submitted by admin)")
    public PillStatus getPillStatus() {
        return pillStatus;
    }

    @ExportValue(index = 9, column = "Source of change")
    public String getSourceOfChange() {
        return sourceOfChange;
    }
}
