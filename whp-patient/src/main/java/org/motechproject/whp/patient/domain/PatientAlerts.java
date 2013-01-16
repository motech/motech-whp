package org.motechproject.whp.patient.domain;

import lombok.Data;

@Data
public class PatientAlerts {
    PatientAlert adherenceMissedAlert = new PatientAlert();
    CumulativeMissedDoseAlert cumulativeMissedDosesAlert = new CumulativeMissedDoseAlert();
    PatientAlert therapyNotStartedAlert = new PatientAlert();
}
