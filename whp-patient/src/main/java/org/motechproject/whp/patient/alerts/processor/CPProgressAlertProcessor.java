package org.motechproject.whp.patient.alerts.processor;

import org.motechproject.whp.common.domain.alerts.PatientAlertType;
import org.motechproject.whp.patient.domain.Patient;
import org.springframework.stereotype.Component;

import static org.motechproject.whp.common.domain.alerts.PatientAlertType.CPProgress;

@Component
public class CPProgressAlertProcessor implements AlertProcessor {

    @Override
    public double process(Patient patient) {
        return patient.getCPProgressPercentage();
    }

    @Override
    public PatientAlertType alertType() {
        return CPProgress;
    }
}
