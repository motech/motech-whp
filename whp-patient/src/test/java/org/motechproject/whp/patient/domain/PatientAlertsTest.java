package org.motechproject.whp.patient.domain;

import org.junit.Test;
import org.motechproject.whp.patient.domain.alerts.PatientAlert;
import org.motechproject.whp.patient.domain.alerts.PatientAlerts;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotSame;
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
}
