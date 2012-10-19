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
        assertTrue(SputumTrackingInstance.isValidRegistrationInstance(SputumTrackingInstance.InTreatment.name().toLowerCase()));
        assertTrue(SputumTrackingInstance.isValidRegistrationInstance(SputumTrackingInstance.InTreatment.name()));
        assertFalse(SputumTrackingInstance.isValidRegistrationInstance("invalid_instance"));
    }

    @Test
    public void shouldGetInstanceForGivenValue() {
        assertEquals(SputumTrackingInstance.PreTreatment, SputumTrackingInstance.getInstanceForValue("PreTreatment"));
        assertEquals(SputumTrackingInstance.PreTreatment, SputumTrackingInstance.getInstanceForValue("Pretreatment"));
        assertEquals(SputumTrackingInstance.PreTreatment, SputumTrackingInstance.getInstanceForValue("Pre-Treatment"));
        assertEquals(SputumTrackingInstance.PreTreatment, SputumTrackingInstance.getInstanceForValue("Pre-treatment"));
    }
}
