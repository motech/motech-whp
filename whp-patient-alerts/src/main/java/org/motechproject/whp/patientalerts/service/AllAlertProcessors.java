package org.motechproject.whp.patientalerts.service;

import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patientalerts.processor.AlertProcessor;
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
