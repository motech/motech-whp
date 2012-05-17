package org.motechproject.whp.adherence.domain;

import lombok.Data;
import org.joda.time.LocalDate;
import org.motechproject.export.annotation.ReportValue;
import org.motechproject.model.DayOfWeek;

import java.util.Map;

@Data
public class Adherence {

    private String patientId;
    private String treatmentId;

    private DayOfWeek pillDay;

    private LocalDate pillDate;

    private PillStatus pillStatus = PillStatus.Unknown;

    private Map<String, Object> meta;

    public Adherence() {
    }

    public Adherence(LocalDate logDate) {
        this.pillDate = logDate;
    }

    public Adherence(String patientId, String treatmentId, DayOfWeek pillDay, LocalDate pillDate, PillStatus pillStatus, Map<String, Object> meta) {
        this.patientId = patientId;
        this.treatmentId = treatmentId;
        this.pillDay = pillDay;
        this.pillDate = pillDate;
        this.pillStatus = pillStatus;
        this.meta = meta;
    }

    @ReportValue(index = 0)
    public String getPatientId() {
        return patientId;
    }

    @ReportValue(index = 1)
    public String getTbId() {
        return (String) meta.get(AdherenceConstants.TB_ID);
    }

    @ReportValue(index = 2, column = "Adherence date")
    public String pillDate() {
        return pillDate.toString("dd/MM/yyyy");
    }

    @ReportValue(index = 3, column = "Adherence value")
    public String pillStatus() {
        return pillStatus.name();
    }
}
