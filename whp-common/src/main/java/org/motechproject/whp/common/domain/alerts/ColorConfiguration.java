package org.motechproject.whp.common.domain.alerts;

import org.motechproject.whp.common.service.AlertsPropertiesValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ColorConfiguration {

    private AlertsPropertiesValues alertsPropertiesValues;
    public static final String DEFAULT_COLOR = "aqua";

    @Autowired
    public ColorConfiguration(AlertsPropertiesValues alertsPropertiesValues) {
        this.alertsPropertiesValues = alertsPropertiesValues;
    }

    public String getColorFor(PatientAlertType alertType, int severity) {
        switch (alertType) {
            case AdherenceMissing:
                return getSeverityColor(severity, alertsPropertiesValues.getAdherenceMissingSeverityColors());
            case CumulativeMissedDoses:
                return getSeverityColor(severity, alertsPropertiesValues.getCumulativeMissedDosesSeverityColors());
            case TreatmentNotStarted:
                return getSeverityColor(severity, alertsPropertiesValues.getTreatmentNotStartedSeverityColors());
            default:
                return DEFAULT_COLOR;
        }
    }

    private String getSeverityColor(int severity, List<String> severityColorList) {
        if (severity < severityColorList.size()) {
            return severityColorList.get(severity);
        }
        return DEFAULT_COLOR;
    }


}
