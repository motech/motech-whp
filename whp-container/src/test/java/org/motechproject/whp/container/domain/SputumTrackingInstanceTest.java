package org.motechproject.whp.container.domain;

import org.junit.Test;
import org.motechproject.whp.common.domain.SputumTrackingInstance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SputumTrackingInstanceTest {
    @Test
    public void shouldValidateGivenInstanceText() {
        assertTrue(SputumTrackingInstance.isValidRegistrationInstance(SputumTrackingInstance.InTreatment.getDisplayText()));
        assertFalse(SputumTrackingInstance.isValidRegistrationInstance("invalid_instance"));
    }

    @Test
    public void shouldGetInstanceForGivenValue() {
        assertEquals(SputumTrackingInstance.PreTreatment, SputumTrackingInstance.getInstanceForValue("Pre-Treatment"));
        assertEquals(SputumTrackingInstance.PreTreatment, SputumTrackingInstance.getInstanceForValue("Pre-treatment"));
    }

    @Test
    public void shouldValidateBasedOnInstanceName() {
        assertTrue(SputumTrackingInstance.isValidRegistrationInstanceName("PreTreatment"));
        assertTrue(SputumTrackingInstance.isValidRegistrationInstanceName("Pretreatment"));
        assertFalse(SputumTrackingInstance.isValidRegistrationInstanceName("Pre-treatment"));
        assertFalse(SputumTrackingInstance.isValidRegistrationInstanceName("Pre-Treatment"));
    }
}
