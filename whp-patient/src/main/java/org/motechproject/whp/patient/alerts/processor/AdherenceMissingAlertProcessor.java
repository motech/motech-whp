package org.motechproject.whp.patient.alerts.processor;

import org.motechproject.whp.common.domain.alerts.PatientAlertType;
import org.motechproject.whp.patient.domain.Patient;
import org.springframework.stereotype.Component;

import static org.motechproject.whp.common.domain.alerts.PatientAlertType.AdherenceMissing;

@Component
public class AdherenceMissingAlertProcessor implements AlertProcessor {

    @Override
    public int process(Patient patient) {
        if(patient.isCurrentTreatmentPaused())
            return NO_ALERT_VALUE;
        return patient.getWeeksElapsedSinceLastDose();
    }

    @Override
    public PatientAlertType alertType() {
        return AdherenceMissing;
    }
}
