package org.motechproject.whp.patient.domain.alerts;

import lombok.Data;
import org.motechproject.whp.common.domain.alerts.PatientAlertType;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static org.motechproject.whp.common.domain.alerts.PatientAlertType.*;

@Data
public class PatientAlerts implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<PatientAlertType, PatientAlert> alerts = new HashMap<>();

    private boolean hasAlerts;

    public PatientAlert getAlert(PatientAlertType alertType) {
        if(alerts.containsKey(alertType)){
            return alerts.get(alertType);
        } else {
            PatientAlert patientAlert = new PatientAlert(alertType);
            alerts.put(alertType, patientAlert);
            return patientAlert;
        }
    }

    public PatientAlert cumulativeMissedDoseAlert(){
        return getAlert(CumulativeMissedDoses);
    }

    public PatientAlert adherenceMissingAlert(){
        return getAlert(AdherenceMissing);
    }

    public PatientAlert treatmentNotStartedAlert(){
        return getAlert(TreatmentNotStarted);
    }

    public void updateAlertStatus(PatientAlertType alertType, int value, int severity) {
        getAlert(alertType).update(value, severity);
        updateAlertStatus();
    }

    private void updateAlertStatus() {
        for(PatientAlert alert : alerts.values()){
            if(alert.hasAlert()){
                hasAlerts = true;
                return;
            }
        }
        hasAlerts = false;
    }

    public boolean hasAlerts(){
        return hasAlerts;
    }
}
