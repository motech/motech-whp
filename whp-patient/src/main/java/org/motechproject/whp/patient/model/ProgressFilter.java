package org.motechproject.whp.patient.model;

import lombok.Getter;
import org.motechproject.whp.common.domain.alerts.PatientAlertType;

@Getter
public enum ProgressFilter {

    IPProgress("IP Progress Alert", PatientAlertType.IPProgress, 100.01),
    CPProgress("CP Progress Alert", PatientAlertType.CPProgress, 100.01);

    private String displayText;
    private PatientAlertType alertType;
    private double filterValue;

    ProgressFilter(String displayText, PatientAlertType alertType, double filterValue) {
        this.displayText = displayText;
        this.alertType = alertType;
        this.filterValue = filterValue;
    }
}
