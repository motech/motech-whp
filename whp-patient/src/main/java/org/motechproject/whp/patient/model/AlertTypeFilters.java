package org.motechproject.whp.patient.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.motechproject.whp.common.domain.alerts.ColorConfiguration;
import org.motechproject.whp.common.domain.alerts.PatientAlertType;
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
    public AlertTypeFilters(ColorConfiguration colorConfiguration) {
        alertTypeFilters.add(allAlerts());
        alertTypeFilters.add(adherenceMissingWithSeverityOneAlertType(colorConfiguration));
        alertTypeFilters.add(adherenceMissingWithSeverityTwo(colorConfiguration));
        alertTypeFilters.add(adherenceMissingWithSeverityThree(colorConfiguration));
        alertTypeFilters.add(cumulativeMissedDoses(colorConfiguration));
        alertTypeFilters.add(treatmentNotStarted(colorConfiguration));
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

    private AlertTypeFilter treatmentNotStarted(ColorConfiguration colorConfiguration) {
        return new AlertTypeFilter(TREATMENT_NOT_STARTED)
                .withMessageCode("message.alert.filter.treatment.not.started.alerts")
                .withColor(colorConfiguration.getColorFor(TreatmentNotStarted, 1))
                .withAlertType(PatientAlertType.TreatmentNotStarted)
                .withFilterKey(alertSeverityParam(TreatmentNotStarted))
                .withFilterValue("1");
    }

    private AlertTypeFilter cumulativeMissedDoses(ColorConfiguration colorConfiguration) {
        return new AlertTypeFilter(CUMULATIVE_MISSED_DOSES)
                .withMessageCode("message.alert.filter.cumulative.missed.dose.alerts")
                .withColor(colorConfiguration.getColorFor(CumulativeMissedDoses, 1))
                .withAlertType(PatientAlertType.CumulativeMissedDoses)
                .withFilterValue(alertSeverityParam(CumulativeMissedDoses))
                .withFilterValue("1");
    }

    private AlertTypeFilter adherenceMissingWithSeverityOneAlertType(ColorConfiguration colorConfiguration) {
        return new AlertTypeFilter(ADHERENCE_MISSING_WITH_SEVERITY_ONE)
                .withMessageCode("message.alert.filter.adherence.missing.severity.one.alerts")
                .withAlertType(AdherenceMissing)
                .withColor(colorConfiguration.getColorFor(AdherenceMissing, 1))
                .withFilterKey(alertSeverityParam(AdherenceMissing))
                .withFilterValue("1");
    }

    private AlertTypeFilter adherenceMissingWithSeverityTwo(ColorConfiguration colorConfiguration) {
        return new AlertTypeFilter(ADHERENCE_MISSING_WITH_SEVERITY_TWO)
                .withMessageCode("message.alert.filter.adherence.missing.severity.two.alerts")
                .withAlertType(AdherenceMissing)
                .withColor(colorConfiguration.getColorFor(AdherenceMissing, 2))
                .withFilterKey(alertSeverityParam(AdherenceMissing))
                .withFilterValue("2");
    }

    private AlertTypeFilter adherenceMissingWithSeverityThree(ColorConfiguration colorConfiguration) {
        return new AlertTypeFilter(ADHERENCE_MISSING_WITH_SEVERITY_THREE)
                .withMessageCode("message.alert.filter.adherence.missing.severity.three.alerts")
                .withAlertType(AdherenceMissing)
                .withColor(colorConfiguration.getColorFor(AdherenceMissing, 3))
                .withFilterKey(alertSeverityParam(AdherenceMissing))
                .withFilterValue("3");
    }
}
