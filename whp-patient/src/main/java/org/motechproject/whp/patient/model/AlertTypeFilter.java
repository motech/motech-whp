package org.motechproject.whp.patient.model;

import lombok.Getter;
import org.motechproject.whp.common.domain.alerts.PatientAlertType;

import static org.motechproject.whp.common.domain.alerts.PatientAlertType.AdherenceMissing;
import static org.motechproject.whp.patient.query.PatientQueryDefinition.alertSeverityParam;
import static org.motechproject.whp.patient.query.PatientQueryDefinition.alertStatusFieldName;

@Getter
public enum AlertTypeFilter {

    AllAlerts("All alerts (Patients with one or more alerts set)", alertStatusFieldName(), "true"),
    AdherenceMissingWithSeverityOne("Adherence missing for 1 week alert (Yellow alert)", alertSeverityParam(AdherenceMissing), "1"),
    AdherenceMissingWithSeverityTwo("Adherence missing for 2 weeks alert (Red alert)", alertSeverityParam(AdherenceMissing), "2"),
    AdherenceMissingWithSeverityThree("Adherence missing for 6 weeks alert (Maroon alert)", alertSeverityParam(AdherenceMissing), "3"),
    CumulativeMissedDoses("Cumulative missed doses alert", alertSeverityParam(PatientAlertType.CumulativeMissedDoses), "1"),
    TreatmentNotStarted("Missing Date of Start of Treatment Alert", alertSeverityParam(PatientAlertType.TreatmentNotStarted), "1"),
    NoAlerts("No Alerts", alertStatusFieldName(), "false");

    private String displayText;
    private final String filterKey;
    private final String filterValue;

    AlertTypeFilter(String displayText, String filterKey, String filterValue) {
        this.displayText = displayText;
        this.filterKey = filterKey;
        this.filterValue = filterValue;
    }
}