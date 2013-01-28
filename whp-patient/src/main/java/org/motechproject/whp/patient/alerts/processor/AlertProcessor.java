package org.motechproject.whp.patient.alerts.processor;

import org.motechproject.whp.common.domain.alerts.PatientAlertType;
import org.motechproject.whp.patient.domain.Patient;

public interface AlertProcessor {

    public final int NO_ALERT_VALUE = 0;

    int process(Patient patient);
    PatientAlertType alertType();
}
