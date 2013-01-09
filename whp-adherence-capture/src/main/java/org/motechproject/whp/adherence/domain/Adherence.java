package org.motechproject.whp.adherence.domain;

import lombok.Data;
import org.joda.time.LocalDate;
import org.motechproject.export.annotation.ExportValue;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.common.util.WHPDate;

import java.util.Date;

@Data
public class Adherence {

    private String patientId;

    private String treatmentId;

    private DayOfWeek pillDay;

    private LocalDate pillDate;

    private PillStatus pillStatus = PillStatus.Unknown;

    private String tbId;

    private String providerId;

    private String district;

    public Adherence() {
    }

    public Adherence(LocalDate logDate) {
        this.pillDate = logDate;
    }

    public Adherence(String patientId,
                     String treatmentId,
                     DayOfWeek pillDay,
                     LocalDate pillDate,
                     PillStatus pillStatus,
                     String tbId,
                     String providerId) {

        this.patientId = patientId;
        this.treatmentId = treatmentId;
        this.pillDay = pillDay;
        this.pillDate = pillDate;
        this.pillStatus = pillStatus;
        this.tbId = tbId;
        this.providerId = providerId;
    }

    @ExportValue(index = 0)
    public String getPatientId() {
        return patientId;
    }

    @ExportValue(index = 1)
    public String getTbId() {
        return tbId;
    }

    @ExportValue(index = 2, column = "Adherence date", format = WHPDate.DATE_FORMAT)
    public Date pillDate() {
        return pillDate.toDate();
    }

    @ExportValue(index = 3, column = "Adherence value")
    public String pillStatus() {
        return pillStatus.name();
    }
}
