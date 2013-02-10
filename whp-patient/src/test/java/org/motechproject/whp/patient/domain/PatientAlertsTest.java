package org.motechproject.whp.patient.domain;

import org.junit.Test;
import org.motechproject.whp.patient.domain.alerts.PatientAlert;
import org.motechproject.whp.patient.domain.alerts.PatientAlerts;

import static junit.framework.Assert.*;
import static org.motechproject.whp.common.domain.alerts.PatientAlertType.AdherenceMissing;
import static org.motechproject.whp.common.domain.alerts.PatientAlertType.CumulativeMissedDoses;

public class PatientAlertsTest {

    @Test
    public void shouldCreateAndReturnPatientAlertForGivenAlertType() {
        PatientAlerts alerts = new PatientAlerts();
        PatientAlert alert = alerts.getAlert(AdherenceMissing);
        alert.setValue(1);

        assertEquals(alert, alerts.getAlert(AdherenceMissing));
        assertNotSame(alert, alerts.getAlert(CumulativeMissedDoses));
    }

    @Test
    public void shouldReturnIfPatientHasAlert() {
        PatientAlerts alerts = new PatientAlerts();
        alerts.updateAlertStatus(AdherenceMissing, 10, 1, "yellow");
        assertTrue(alerts.hasAlerts());

        alerts.updateAlertStatus(AdherenceMissing, 5, 0, "");
        assertFalse(alerts.hasAlerts());

        alerts.updateAlertStatus(CumulativeMissedDoses, 11, 1, "blue");
        assertTrue(alerts.hasAlerts());
    }
}
