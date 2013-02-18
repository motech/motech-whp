package org.motechproject.whp.patient.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.motechproject.whp.common.domain.alerts.AlertColorConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.motechproject.whp.common.domain.alerts.PatientAlertType.*;
import static org.motechproject.whp.patient.query.PatientQueryDefinition.alertSeverityParam;
import static org.motechproject.whp.patient.query.PatientQueryDefinition.alertStatusFieldName;

@Component
@EqualsAndHashCode
public class AlertTypeFilters {

    public static final String ALL_ALERTS = "AllAlerts";
    public static final String NO_ALERTS = "NoAlerts";
    public static final String ADHERENCE_MISSING_WITH_SEVERITY_ONE = "AdherenceMissingWithSeverityOne";
    public static final String ADHERENCE_MISSING_WITH_SEVERITY_TWO = "AdherenceMissingWithSeverityTwo";
    public static final String ADHERENCE_MISSING_WITH_SEVERITY_THREE = "AdherenceMissingWithSeverityThree";
    public static final String CUMULATIVE_MISSED_DOSES = "CumulativeMissedDoses";
    public static final String TREATMENT_NOT_STARTED = "TreatmentNotStarted";

    @Getter
    private List<AlertTypeFilter> alertTypeFilters = new ArrayList<>();

    @Autowired
    public AlertTypeFilters(AlertColorConfiguration alertColorConfiguration) {
        alertTypeFilters.add(allAlerts());
        alertTypeFilters.add(adherenceMissingWithSeverityOneAlertType(alertColorConfiguration));
        alertTypeFilters.add(adherenceMissingWithSeverityTwo(alertColorConfiguration));
        alertTypeFilters.add(adherenceMissingWithSeverityThree(alertColorConfiguration));
        alertTypeFilters.add(cumulativeMissedDoses(alertColorConfiguration));
        alertTypeFilters.add(treatmentNotStarted(alertColorConfiguration));
        alertTypeFilters.add(noAlerts());
    }

    public Map<String, Object> getQueryFields(String name) {
        for (AlertTypeFilter filter : alertTypeFilters) {
            if (filter.getName().equals(name)) {
                return filter.getQueryFields();
            }
        }
        return new HashMap<>();
    }

    public AlertTypeFilter getFilter(String alertTypeName) {
        for (AlertTypeFilter filter : alertTypeFilters) {
            if (filter.getName().equals(alertTypeName)) {
                return filter;
            }
        }
        return null;
    }

    private AlertTypeFilter allAlerts() {
        return new AlertTypeFilter(ALL_ALERTS)
                .withMessageCode("message.alert.filter.all.alerts")
                .withFilterKey(alertStatusFieldName())
                .withFilterValue("true");
    }

    private AlertTypeFilter noAlerts() {
        return new AlertTypeFilter(NO_ALERTS)
                .withMessageCode("message.alert.filter.no.alerts")
                .withFilterKey(alertStatusFieldName())
                .withFilterValue("false");
    }

    private AlertTypeFilter treatmentNotStarted(AlertColorConfiguration alertColorConfiguration) {
        return new AlertTypeFilter(TREATMENT_NOT_STARTED)
                .withMessageCode("message.alert.filter.treatment.not.started.alerts")
                .withColor(alertColorConfiguration.getColorFor(TreatmentNotStarted, 1))
                .withAlertType(TreatmentNotStarted)
                .withFilterKey(alertSeverityParam(TreatmentNotStarted))
                .withFilterValue("1");
    }

    private AlertTypeFilter cumulativeMissedDoses(AlertColorConfiguration alertColorConfiguration) {
        return new AlertTypeFilter(CUMULATIVE_MISSED_DOSES)
                .withMessageCode("message.alert.filter.cumulative.missed.dose.alerts")
                .withColor(alertColorConfiguration.getColorFor(CumulativeMissedDoses, 1))
                .withAlertType(CumulativeMissedDoses)
                .withFilterKey(alertSeverityParam(CumulativeMissedDoses))
                .withFilterValue("1");
    }

    private AlertTypeFilter adherenceMissingWithSeverityOneAlertType(AlertColorConfiguration alertColorConfiguration) {
        return new AlertTypeFilter(ADHERENCE_MISSING_WITH_SEVERITY_ONE)
                .withMessageCode("message.alert.filter.adherence.missing.severity.one.alerts")
                .withAlertType(AdherenceMissing)
                .withColor(alertColorConfiguration.getColorFor(AdherenceMissing, 1))
                .withFilterKey(alertSeverityParam(AdherenceMissing))
                .withFilterValue("1");
    }

    private AlertTypeFilter adherenceMissingWithSeverityTwo(AlertColorConfiguration alertColorConfiguration) {
        return new AlertTypeFilter(ADHERENCE_MISSING_WITH_SEVERITY_TWO)
                .withMessageCode("message.alert.filter.adherence.missing.severity.two.alerts")
                .withAlertType(AdherenceMissing)
                .withColor(alertColorConfiguration.getColorFor(AdherenceMissing, 2))
                .withFilterKey(alertSeverityParam(AdherenceMissing))
                .withFilterValue("2");
    }

    private AlertTypeFilter adherenceMissingWithSeverityThree(AlertColorConfiguration alertColorConfiguration) {
        return new AlertTypeFilter(ADHERENCE_MISSING_WITH_SEVERITY_THREE)
                .withMessageCode("message.alert.filter.adherence.missing.severity.three.alerts")
                .withAlertType(AdherenceMissing)
                .withColor(alertColorConfiguration.getColorFor(AdherenceMissing, 3))
                .withFilterKey(alertSeverityParam(AdherenceMissing))
                .withFilterValue("3");
    }
}
