package org.motechproject.whp.patient.alerts.processor;

import org.motechproject.whp.common.domain.alerts.AlertConfiguration;
import org.motechproject.whp.patient.domain.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.motechproject.whp.common.domain.alerts.PatientAlertType.AdherenceMissing;

@Component
public class AdherenceMissingAlertProcessor extends AlertProcessor {

    @Autowired
    public AdherenceMissingAlertProcessor(AlertConfiguration alertConfiguration) {
        super(alertConfiguration);
    }

    @Override
    public void process(Patient patient) {
        int weeksElapsedSinceLastDose = patient.getWeeksElapsedSinceLastDose();
        updatePatientAlertDetails(patient, AdherenceMissing, weeksElapsedSinceLastDose);
    }
}
