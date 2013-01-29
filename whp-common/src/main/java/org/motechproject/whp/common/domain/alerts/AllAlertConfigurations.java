package org.motechproject.whp.common.domain.alerts;

import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.common.domain.AllDaysOfWeek;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

@Component
public class AllAlertConfigurations {
    Map<PatientAlertType, AlertConfiguration> alertConfigurationMap;

    public AllAlertConfigurations(Map<PatientAlertType, AlertConfiguration> alertConfigurationMap) {
        this.alertConfigurationMap = alertConfigurationMap;
    }

    public AllAlertConfigurations() {
        alertConfigurationMap = new HashMap<>();
        addAlertConfiguration(PatientAlertType.AdherenceMissing, getAdherenceMissingAlertThresholds(), asList(DayOfWeek.Wednesday));
        addAlertConfiguration(PatientAlertType.CumulativeMissedDoses, getCumulativeMissedDoseAlertThresholds(), asList(DayOfWeek.Wednesday));
        addAlertConfiguration(PatientAlertType.TreatmentNotStarted, getTreatmentNotStartedAlertThresholds(), AllDaysOfWeek.allDaysOfWeek);
    }

    private void addAlertConfiguration(PatientAlertType alertType, AlertThresholds alertThresholds, List<DayOfWeek> daysOfWeek) {
        alertConfigurationMap.put(alertType, new AlertConfiguration(alertType, alertThresholds, daysOfWeek));
    }

    private AlertThresholds getAlertThresholds(PatientAlertType alertType){
        return getAlertConfiguration(alertType).getAlertThresholds();
    }

    private AlertConfiguration getAlertConfiguration(PatientAlertType alertType) {
        return alertConfigurationMap.get(alertType);
    }

    private AlertThreshold getThresholdFor(PatientAlertType alertType, int value){
        return getAlertThresholds(alertType).getThreshold(value);
    }

    public int getAlertSeverityFor(PatientAlertType alertType, int value) {
        return getThresholdFor(alertType, value).getAlertSeverity();
    }

    private AlertThresholds getAdherenceMissingAlertThresholds() {
        return new AlertThresholds(1, 2, 6);
    }

    private AlertThresholds getCumulativeMissedDoseAlertThresholds() {
        return new AlertThresholds(10);
    }

    private AlertThresholds getTreatmentNotStartedAlertThresholds() {
        return new AlertThresholds(10);
    }

    public boolean shouldRunToday(PatientAlertType alertType) {
        return getAlertConfiguration(alertType).shouldRunToday();
    }
}
