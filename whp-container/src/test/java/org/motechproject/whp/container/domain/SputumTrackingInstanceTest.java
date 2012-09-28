package org.motechproject.whp.container.domain;

import org.junit.Test;
import org.motechproject.whp.refdata.domain.SputumTrackingInstance;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SputumTrackingInstanceTest {
    @Test
    public void shouldValidateGivenInstanceText() {
        assertTrue(SputumTrackingInstance.isValidRegistrationInstance(SputumTrackingInstance.InTreatment.getDisplayText()));
        assertFalse(SputumTrackingInstance.isValidRegistrationInstance("invalid_instance"));
    }
}
