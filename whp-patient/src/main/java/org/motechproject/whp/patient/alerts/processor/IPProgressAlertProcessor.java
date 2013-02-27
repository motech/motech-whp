package org.motechproject.whp.patient.alerts.processor;

import org.motechproject.whp.common.domain.alerts.PatientAlertType;
import org.motechproject.whp.patient.domain.Patient;
import org.springframework.stereotype.Component;

import static org.motechproject.whp.common.domain.alerts.PatientAlertType.IPProgress;

@Component
public class IPProgressAlertProcessor implements AlertProcessor {

    @Override
    public double process(Patient patient) {
        return patient.getIPProgressPercentage();
    }

    @Override
    public PatientAlertType alertType() {
        return IPProgress;
    }
}
