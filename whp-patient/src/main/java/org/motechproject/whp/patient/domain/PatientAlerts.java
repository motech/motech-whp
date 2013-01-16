package org.motechproject.whp.patient.domain;

public class PatientAlerts {
    PatientAlert adherenceMissedAlert = new PatientAlert();
    CumulativeMissedDoseAlert cumulativeMissedDosesAlert = new CumulativeMissedDoseAlert();
    PatientAlert therapyNotStartedAlert = new PatientAlert();
}
