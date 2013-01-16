package org.motechproject.whp.patient.domain;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotSame;
import static org.motechproject.whp.patient.domain.PatientAlertType.AdherenceMissing;
import static org.motechproject.whp.patient.domain.PatientAlertType.CumulativeMissedDoses;

public class PatientAlertsTest {

    @Test
    public void shouldCreateAndReturnPatientAlertForGivenAlertType() {
        PatientAlerts alerts = new PatientAlerts();
        PatientAlert alert = alerts.getAlert(AdherenceMissing);
        alert.setValue(1);

        assertEquals(alert, alerts.getAlert(AdherenceMissing));
        assertNotSame(alert, alerts.getAlert(CumulativeMissedDoses));
    }
}
