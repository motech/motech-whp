package org.motechproject.whp.patient.alerts.service;

import org.motechproject.whp.common.domain.alerts.AllAlertConfigurations;
import org.motechproject.whp.patient.alerts.processor.AlertProcessor;
import org.motechproject.whp.patient.domain.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class AllAlertProcessors {
    private Set<AlertProcessor> alertProcessors;
    private AllAlertConfigurations alertConfigurations;

    @Autowired
    public AllAlertProcessors(Set<AlertProcessor> alertProcessors, AllAlertConfigurations alertConfigurations) {
        this.alertProcessors = alertProcessors;
        this.alertConfigurations = alertConfigurations;
    }

    public void processAll(Patient patient) {
        for(AlertProcessor alertProcessor : alertProcessors){
             processAlert(alertProcessor, patient);
        }
    }

    public void processBasedOnAlertConfiguration(Patient patient) {
        for(AlertProcessor alertProcessor : alertProcessors){
            if(alertConfigurations.shouldRunToday(alertProcessor.alertType()))
                processAlert(alertProcessor, patient);
        }
    }

    private void processAlert(AlertProcessor alertProcessor, Patient patient) {
        double alertValue = alertProcessor.process(patient);
        int severity = alertConfigurations.getAlertSeverityFor(alertProcessor.alertType(), alertValue);
        patient.updatePatientAlert(alertProcessor.alertType(), alertValue, severity);
    }
}