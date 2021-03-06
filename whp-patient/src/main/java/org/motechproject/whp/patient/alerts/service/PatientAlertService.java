package org.motechproject.whp.patient.alerts.service;

import org.motechproject.whp.patient.domain.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatientAlertService {
    private AllAlertProcessors allAlertProcessors;

    @Autowired
    public PatientAlertService(AllAlertProcessors allAlertProcessors) {
        this.allAlertProcessors = allAlertProcessors;
    }

    public void processAllAlerts(Patient patient) {
        allAlertProcessors.processAll(patient);
    }

    public void processAlertsBasedOnConfiguration(Patient patient) {
        allAlertProcessors.processBasedOnAlertConfiguration(patient);
    }
}
