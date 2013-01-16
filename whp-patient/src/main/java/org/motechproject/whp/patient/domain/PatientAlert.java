package org.motechproject.whp.patient.domain;

import lombok.Data;
import org.joda.time.LocalDate;

@Data
public class PatientAlert {
    private int threshold;
    private int value;
    private LocalDate alertDate;

    public PatientAlert() {
    }

    public PatientAlert(int threshold, int value, LocalDate alertDate) {
        this.threshold = threshold;
        this.value = value;
        this.alertDate = alertDate;
    }
}
