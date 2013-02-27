package org.motechproject.whp.common.domain.alerts;

import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.common.service.AlertsPropertiesValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.motechproject.whp.common.domain.alerts.PatientAlertType.*;

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
        addAlertConfiguration(AdherenceMissing, getAdherenceMissingAlertThresholds(), alertsProperties.getDaysOfAlertGenerationForAdherenceMissingWeeks());
        addAlertConfiguration(CumulativeMissedDoses, getCumulativeMissedDoseAlertThresholds(), alertsProperties.getDayOfAlertGenerationForCumulativeMissedDoses());
        addAlertConfiguration(TreatmentNotStarted, getTreatmentNotStartedAlertThresholds(), alertsProperties.getDayOfAlertGenerationForTreatmentNotStarted());
        addAlertConfiguration(IPProgress, getIPProgressAlertThresholds(), alertsProperties.getDayOfAlertGenerationForIPProgress());
        addAlertConfiguration(CPProgress, getCPProgressAlertThresholds(), alertsProperties.getDayOfAlertGenerationForCPProgress());
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

    private AlertThreshold getThresholdFor(PatientAlertType alertType, double value){
        return getAlertThresholds(alertType).getThreshold(value);
    }

    public Integer getAlertSeverityFor(PatientAlertType alertType, double value) {
        return getThresholdFor(alertType, value).getAlertSeverity();
    }

    private AlertThresholds getAdherenceMissingAlertThresholds() {
        return new AlertThresholds(alertsProperties.getAdherenceMissingWeeks());
    }

    private AlertThresholds getCumulativeMissedDoseAlertThresholds() {
        return new AlertThresholds(alertsProperties.getCumulativeMissedDoses());
    }

    private AlertThresholds getTreatmentNotStartedAlertThresholds() {
        return new AlertThresholds(alertsProperties.getTreatmentNotStartedDays());
    }

    private AlertThresholds getIPProgressAlertThresholds() {
        return new AlertThresholds(alertsProperties.getIPProgressThreshold());
    }

    private AlertThresholds getCPProgressAlertThresholds() {
        return new AlertThresholds(alertsProperties.getCPProgressThreshold());
    }

    public boolean shouldRunToday(PatientAlertType alertType) {
        return getAlertConfiguration(alertType).shouldRunToday();
    }
}
