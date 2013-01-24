package org.motechproject.whp.common.domain.alerts;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AlertConfiguration {
    Map<PatientAlertType, AlertThresholds> alertConfigurationMap;

    public AlertConfiguration() {
        alertConfigurationMap = new HashMap<>();
        alertConfigurationMap.put(PatientAlertType.AdherenceMissing, getAdherenceMissingAlertThresholds());
        alertConfigurationMap.put(PatientAlertType.CumulativeMissedDoses, getCumulativeMissedDoseAlertThresholds());
    }

    private AlertThresholds getAdherenceMissingAlertThresholds() {
        return new AlertThresholds(10);
    }

    private AlertThresholds getCumulativeMissedDoseAlertThresholds() {
        return new AlertThresholds(5, 10, 20);
    }

    private AlertThresholds getAlertThresholds(PatientAlertType alertType){
        return alertConfigurationMap.get(alertType);
    }

    public AlertThreshold getThresholdFor(PatientAlertType alertType, int value){
       return getAlertThresholds(alertType).getThreshold(value);
    }
}
