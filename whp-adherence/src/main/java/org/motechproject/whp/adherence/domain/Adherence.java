package org.motechproject.whp.adherence.domain;

import lombok.Data;
import org.joda.time.LocalDate;
import org.motechproject.model.DayOfWeek;
import org.motechproject.reports.annotation.ReportValue;

@Data
public class Adherence {

    private String patientId;
    private String treatmentId;
    private String tbId;
    private String providerId;

    private DayOfWeek pillDay;

    private LocalDate pillDate;

    private PillStatus pillStatus = PillStatus.Unknown;

    public Adherence() {
    }

    public Adherence(LocalDate logDate) {
        this.pillDate = logDate;
    }

    public Adherence(String patientId, String treatmentId, DayOfWeek pillDay, LocalDate pillDate, PillStatus pillStatus, String tbId, String providerId) {
        this.patientId = patientId;
        this.treatmentId = treatmentId;
        this.pillDay = pillDay;
        this.pillDate = pillDate;
        this.pillStatus = pillStatus;
        this.tbId = tbId;
        this.providerId = providerId;
    }

    @ReportValue
    public String getPatientId() {
        return patientId;
    }

    @ReportValue
    public String getTbId() {
        return tbId;
    }

    @ReportValue
    public String pillDate() {
        return pillDate.toString("dd/MM/yyyy");
    }

    @ReportValue
    public String pillStatus() {
        return pillStatus.name();
    }
}
