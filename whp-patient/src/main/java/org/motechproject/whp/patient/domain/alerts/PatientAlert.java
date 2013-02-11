package org.motechproject.whp.patient.domain.alerts;

import lombok.Data;
import org.joda.time.LocalDate;
import org.motechproject.whp.common.domain.alerts.PatientAlertType;

import static org.motechproject.util.DateUtil.today;

@Data
public class PatientAlert {
    private int value;
    private LocalDate alertDate;
    private PatientAlertType alertType;
    private int alertSeverity;
    private LocalDate resetDate;

    public PatientAlert() {
    }

    public PatientAlert(PatientAlertType alertType) {
        this.alertType = alertType;
    }

    public void update(int newValue, int severity) {
        setValue(newValue);
        if(getAlertSeverity() != severity){
            setAlertDate(today());
            setAlertSeverity(severity);
        }
    }

    public boolean hasAlert() {
        return alertSeverity > 0;
    }
}
