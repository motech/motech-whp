package org.motechproject.whp.patientalerts.processor;

import org.motechproject.whp.patient.domain.Patient;
import org.springframework.stereotype.Component;

@Component
public class AdherenceMissingAlertProcessor implements AlertProcessor {
    @Override
    public void process(Patient patient) {
        patient.updateAdherenceMissingAlert();
    }
}
