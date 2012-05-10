package org.motechproject.whp.adherence.domain;

import lombok.Data;
import org.joda.time.LocalDate;
import org.motechproject.model.DayOfWeek;

import static org.joda.time.format.DateTimeFormat.forPattern;

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

    public Adherence(String patientId, String treatmentId, DayOfWeek pillDay, LocalDate pillDate) {
        this.patientId = patientId;
        this.treatmentId = treatmentId;
        this.pillDay = pillDay;
        this.pillDate = pillDate;
    }

    public void status(PillStatus pillStatus) {
        this.pillStatus = pillStatus;
    }

    public String getPatientId() {
        return patientId;
    }
}
