package org.motechproject.whp.patient.domain;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class PatientAlerts {
    private Map<PatientAlertType, PatientAlert> alerts = new HashMap<>();

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
        return getAlert(PatientAlertType.CumulativeMissedDoses);
    }

    public PatientAlert adherenceMissingAlert(){
        return getAlert(PatientAlertType.AdherenceMissing);
    }

    public PatientAlert treatmentNotStartedAlert(){
        return getAlert(PatientAlertType.TreatmentNotStarted);
    }

}
