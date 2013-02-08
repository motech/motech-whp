package org.motechproject.whp.patient.model;

import lombok.Getter;
import org.motechproject.whp.common.domain.alerts.PatientAlertType;

import java.util.HashMap;
import java.util.Map;

import static org.motechproject.whp.common.domain.alerts.PatientAlertType.AdherenceMissing;
import static org.motechproject.whp.patient.query.PatientQueryDefinition.alertSeverityParam;
import static org.motechproject.whp.patient.query.PatientQueryDefinition.alertStatusFieldName;

@Getter
public enum AlertTypeFilter {

    AllAlerts("All alerts (Patients with one or more alerts set)", alertStatusFieldName(), "true", null),
    AdherenceMissingWithSeverityOne("Adherence missing for 1 week alert (Yellow alert)", alertSeverityParam(AdherenceMissing), "1", PatientAlertType.AdherenceMissing),
    AdherenceMissingWithSeverityTwo("Adherence missing for 2 weeks alert (Red alert)", alertSeverityParam(AdherenceMissing), "2", PatientAlertType.AdherenceMissing),
    AdherenceMissingWithSeverityThree("Adherence missing for 6 weeks alert (Maroon alert)", alertSeverityParam(AdherenceMissing), "3", PatientAlertType.AdherenceMissing),
    CumulativeMissedDoses("Cumulative missed doses alert", alertSeverityParam(PatientAlertType.CumulativeMissedDoses), "1", PatientAlertType.CumulativeMissedDoses),
    TreatmentNotStarted("Missing Date of Start of Treatment Alert", alertSeverityParam(PatientAlertType.TreatmentNotStarted), "1", PatientAlertType.TreatmentNotStarted),
    NoAlerts("No Alerts", alertStatusFieldName(), "false", null);

    private String displayText;
    private final String filterKey;
    private final String filterValue;
    private final PatientAlertType alertType;

    AlertTypeFilter(String displayText, String filterKey, String filterValue, PatientAlertType alertType) {
        this.displayText = displayText;
        this.filterKey = filterKey;
        this.filterValue = filterValue;
        this.alertType = alertType;
    }

    public static Map<String, Object> getQueryFields(String enumValue) {
        Map<String, Object> queryFields = new HashMap<>();

        AlertTypeFilter alertTypeFilter = AlertTypeFilter.valueOf(enumValue);
        queryFields.put(alertTypeFilter.getFilterKey(), alertTypeFilter.getFilterValue());

        return queryFields;
    }
}