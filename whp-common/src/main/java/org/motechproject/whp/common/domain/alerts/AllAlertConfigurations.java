package org.motechproject.whp.common.domain.alerts;

import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.common.domain.AllDaysOfWeek;
import org.motechproject.whp.common.service.AlertsPropertiesValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

@Component
public class AllAlertConfigurations {
    Map<PatientAlertType, AlertConfiguration> alertConfigurationMap;

    AlertsPropertiesValues alertsProperties;

    @Autowired
    public AllAlertConfigurations(AlertsPropertiesValues alertsPropertiesValues) {
        this.alertsProperties = alertsPropertiesValues;
        initializeAlertConfiguration();
    }

    public AllAlertConfigurations(Map<PatientAlertType, AlertConfiguration> alertConfigurationMap) {
        this.alertConfigurationMap = alertConfigurationMap;
    }

    public AllAlertConfigurations() {
        initializeAlertConfiguration();
    }

    private void initializeAlertConfiguration() {
        alertConfigurationMap = new HashMap<>();
        addAlertConfiguration(PatientAlertType.AdherenceMissing, getAdherenceMissingAlertThresholds(), alertsProperties.getDaysOfAlertGenerationForAdherenceMissingWeeks());
        addAlertConfiguration(PatientAlertType.CumulativeMissedDoses, getCumulativeMissedDoseAlertThresholds(), alertsProperties.getDayOfAlertGenerationForCumulativeMissedDoses());
        addAlertConfiguration(PatientAlertType.TreatmentNotStarted, getTreatmentNotStartedAlertThresholds(), alertsProperties.getDayOfAlertGenerationForTreatmentNotStarted());
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

    public Integer getAlertSeverityFor(PatientAlertType alertType, int value) {
        return getThresholdFor(alertType, value).getAlertSeverity();
    }

    private AlertThresholds getAdherenceMissingAlertThresholds() {
        return new AlertThresholds(alertsProperties.getAdherenceMissingWeeks());
    }

    private AlertThresholds getCumulativeMissedDoseAlertThresholds() {
        return new AlertThresholds(asList(alertsProperties.getCumulativeMissedDoses()));
    }

    private AlertThresholds getTreatmentNotStartedAlertThresholds() {
        return new AlertThresholds(asList(alertsProperties.getTreatmentNotStartedDays()));
    }

    public boolean shouldRunToday(PatientAlertType alertType) {
        return getAlertConfiguration(alertType).shouldRunToday();
    }
}
