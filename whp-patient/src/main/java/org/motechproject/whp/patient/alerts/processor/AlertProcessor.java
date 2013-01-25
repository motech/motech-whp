package org.motechproject.whp.patient.alerts.processor;

import org.motechproject.whp.common.domain.alerts.AlertConfiguration;
import org.motechproject.whp.common.domain.alerts.AlertThreshold;
import org.motechproject.whp.common.domain.alerts.PatientAlertType;
import org.motechproject.whp.patient.domain.Patient;

import static org.motechproject.whp.common.domain.alerts.PatientAlertType.AdherenceMissing;

public abstract class AlertProcessor {
    private AlertConfiguration alertConfiguration;
    public abstract void process(Patient patient);

    protected AlertProcessor(AlertConfiguration alertConfiguration) {
        this.alertConfiguration = alertConfiguration;
    }

    protected void updatePatientAlertDetails(Patient patient, PatientAlertType alertType, int alertValue) {
        AlertThreshold threshold =  alertConfiguration.getThresholdFor(alertType, alertValue);
        patient.updatePatientAlert(AdherenceMissing, alertValue, threshold.getAlertSeverity());
    }

}
