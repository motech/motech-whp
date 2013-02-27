package org.motechproject.whp.patient.domain;

import org.junit.Test;
import org.motechproject.whp.patient.domain.alerts.PatientAlert;
import org.motechproject.whp.patient.domain.alerts.PatientAlerts;

import static junit.framework.Assert.*;
import static org.motechproject.whp.common.domain.alerts.PatientAlertType.*;

public class PatientAlertsTest {

    @Test
    public void shouldCreateAndReturnPatientAlertForGivenAlertType() {
        PatientAlerts alerts = new PatientAlerts();
        PatientAlert alert = alerts.getAlert(AdherenceMissing);
        alert.setValue(1);

        assertEquals(alert, alerts.getAlert(AdherenceMissing));
        assertNotSame(alert, alerts.getAlert(CumulativeMissedDoses));
        assertNotSame(alert, alerts.getAlert(IPProgress));
    }

    @Test
    public void shouldReturnIfPatientHasAlert() {
        PatientAlerts alerts = new PatientAlerts();
        alerts.updateAlertStatus(AdherenceMissing, 10, 1);
        assertTrue(alerts.hasAlerts());

        alerts.updateAlertStatus(AdherenceMissing, 5, 0);
        assertFalse(alerts.hasAlerts());

        alerts.updateAlertStatus(CPProgress, 95, 0);
        assertFalse(alerts.hasAlerts());

        alerts.updateAlertStatus(CumulativeMissedDoses, 11, 1);
        assertTrue(alerts.hasAlerts());

        alerts.updateAlertStatus(CumulativeMissedDoses, 5, 0);
        alerts.updateAlertStatus(IPProgress, 100, 1);
        assertTrue(alerts.hasAlerts());
    }
}
