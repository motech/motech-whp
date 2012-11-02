package org.motechproject.whp.common.domain;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RegistrationInstanceTest {
    @Test
    public void shouldValidateGivenInstanceText() {
        assertTrue(RegistrationInstance.isValidRegistrationInstance(RegistrationInstance.InTreatment.getDisplayText()));
        assertFalse(RegistrationInstance.isValidRegistrationInstance("invalid_instance"));
    }

    @Test
    public void shouldValidateBasedOnInstanceName() {
        assertTrue(RegistrationInstance.isValidRegistrationInstanceName("PreTreatment"));
        assertTrue(RegistrationInstance.isValidRegistrationInstanceName("Pretreatment"));
        assertFalse(RegistrationInstance.isValidRegistrationInstanceName("Pre-treatment"));
        assertFalse(RegistrationInstance.isValidRegistrationInstanceName("Pre-Treatment"));
    }
}
