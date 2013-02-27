package org.motechproject.whp.common.domain.alerts;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PatientAlertTypeTest {
    @Test
    public void shouldReturnWhetherAlertTypeIsNotifiable(){

        PatientAlertType patientAlertType = PatientAlertType.IPProgress;
        assertFalse(patientAlertType.isNotifiable());

        patientAlertType = PatientAlertType.AdherenceMissing;
        assertTrue(patientAlertType.isNotifiable());

        patientAlertType = PatientAlertType.CumulativeMissedDoses;
        assertTrue(patientAlertType.isNotifiable());

        patientAlertType = PatientAlertType.TreatmentNotStarted;
        assertTrue(patientAlertType.isNotifiable());

        patientAlertType = PatientAlertType.CPProgress;
        assertFalse(patientAlertType.isNotifiable());

    }
}
