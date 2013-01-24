package org.motechproject.whp.patientalerts.processor;

import org.motechproject.whp.patient.domain.Patient;

public interface AlertProcessor {
    void process(Patient patient);
}
