package org.motechproject.whp.patient.model;

import lombok.Getter;
import org.motechproject.whp.common.domain.alerts.PatientAlertType;

@Getter
public enum ProgressFilter {

    IPProgress("IP Progress Alert", PatientAlertType.IPProgress, 100),
    CPProgress("CP Progress Alert", PatientAlertType.CPProgress, 100);

    private String displayText;
    private PatientAlertType alertType;
    private int filterValue;

    ProgressFilter(String displayText, PatientAlertType alertType, int filterValue) {
        this.displayText = displayText;
        this.alertType = alertType;
        this.filterValue = filterValue;
    }
}
