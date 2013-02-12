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

    AllAlerts("message.alert.filter.all.alerts", alertStatusFieldName(), "true", null),
    AdherenceMissingWithSeverityOne("message.alert.filter.adherence.missing.severity.one.alerts", alertSeverityParam(AdherenceMissing), "1", PatientAlertType.AdherenceMissing),
    AdherenceMissingWithSeverityTwo("message.alert.filter.adherence.missing.severity.two.alerts", alertSeverityParam(AdherenceMissing), "2", PatientAlertType.AdherenceMissing),
    AdherenceMissingWithSeverityThree("message.alert.filter.adherence.missing.severity.three.alerts", alertSeverityParam(AdherenceMissing), "3", PatientAlertType.AdherenceMissing),
    CumulativeMissedDoses("message.alert.filter.cumulative.missed.dose.alerts", alertSeverityParam(PatientAlertType.CumulativeMissedDoses), "1", PatientAlertType.CumulativeMissedDoses),
    TreatmentNotStarted("message.alert.filter.treatment.not.started.alerts", alertSeverityParam(PatientAlertType.TreatmentNotStarted), "1", PatientAlertType.TreatmentNotStarted),
    NoAlerts("message.alert.filter.no.alerts", alertStatusFieldName(), "false", null);

    private String messageCode;
    private final String filterKey;
    private final String filterValue;
    private final PatientAlertType alertType;

    AlertTypeFilter(String messageCode, String filterKey, String filterValue, PatientAlertType alertType) {
        this.messageCode = messageCode;
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