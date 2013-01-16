package org.motechproject.whp.patient.domain;

import lombok.Data;
import org.joda.time.LocalDate;

@Data
public class PatientAlert {
    private int value;
    private LocalDate alertDate;
    private PatientAlertType alertType;
    private int severity;
    private LocalDate resetDate;

    public PatientAlert(PatientAlertType alertType) {
        this.alertType = alertType;
    }
}
