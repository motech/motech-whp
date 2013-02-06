package org.motechproject.whp.patient.domain;

import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class PatientFlagTest {

    @Test
    public void shouldReturnWhetherTheFlagIsSetOrNot(){
        PatientFlag patientFlag = new PatientFlag();

        patientFlag.setFlagValue(true);
        assertTrue(patientFlag.isFlagSet());

        patientFlag.setFlagValue(false);
        assertFalse(patientFlag.isFlagSet());
    }
}
