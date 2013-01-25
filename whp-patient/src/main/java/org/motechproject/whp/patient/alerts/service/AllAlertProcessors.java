package org.motechproject.whp.patient.alerts.service;

import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.alerts.processor.AlertProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class AllAlertProcessors {

    private Set<AlertProcessor> alertProcessors;

    @Autowired
    public AllAlertProcessors(Set<AlertProcessor> alertProcessors) {
        this.alertProcessors = alertProcessors;
    }

    public void process(Patient patient) {
        for(AlertProcessor alertProcessor : alertProcessors){
            alertProcessor.process(patient);
        }
    }
}
