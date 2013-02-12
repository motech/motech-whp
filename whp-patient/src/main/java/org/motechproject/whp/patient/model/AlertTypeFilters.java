package org.motechproject.whp.patient.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.motechproject.whp.common.domain.alerts.PatientAlertType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.motechproject.whp.common.domain.alerts.PatientAlertType.AdherenceMissing;
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

    public AlertTypeFilters() {
        alertTypeFilters.add(new AlertTypeFilter(ALL_ALERTS, "message.alert.filter.all.alerts", alertStatusFieldName(), "true", null));
        alertTypeFilters.add(new AlertTypeFilter(ADHERENCE_MISSING_WITH_SEVERITY_ONE, "message.alert.filter.adherence.missing.severity.one.alerts", alertSeverityParam(AdherenceMissing), "1", AdherenceMissing));
        alertTypeFilters.add(new AlertTypeFilter(ADHERENCE_MISSING_WITH_SEVERITY_TWO, "message.alert.filter.adherence.missing.severity.two.alerts", alertSeverityParam(AdherenceMissing), "2", AdherenceMissing));
        alertTypeFilters.add(new AlertTypeFilter(ADHERENCE_MISSING_WITH_SEVERITY_THREE, "message.alert.filter.adherence.missing.severity.three.alerts", alertSeverityParam(AdherenceMissing), "3", AdherenceMissing));
        alertTypeFilters.add(new AlertTypeFilter(CUMULATIVE_MISSED_DOSES, "message.alert.filter.cumulative.missed.dose.alerts", alertSeverityParam(PatientAlertType.CumulativeMissedDoses), "1", PatientAlertType.CumulativeMissedDoses));
        alertTypeFilters.add(new AlertTypeFilter(TREATMENT_NOT_STARTED, "message.alert.filter.treatment.not.started.alerts", alertSeverityParam(PatientAlertType.TreatmentNotStarted), "1", PatientAlertType.TreatmentNotStarted));
        alertTypeFilters.add(new AlertTypeFilter(NO_ALERTS, "message.alert.filter.no.alerts", alertStatusFieldName(), "false", null));
    }

    public Map<String, Object> getQueryFields(String name) {
        for(AlertTypeFilter filter : alertTypeFilters){
            if(filter.getName().equals(name)){
                return filter.getQueryFields();
            }
        }
        return new HashMap<>();
    }

    public AlertTypeFilter getFilter(String alertTypeName) {
        for(AlertTypeFilter filter : alertTypeFilters){
            if(filter.getName().equals(alertTypeName)){
                return filter;
            }
        }
        return null;
    }
}
