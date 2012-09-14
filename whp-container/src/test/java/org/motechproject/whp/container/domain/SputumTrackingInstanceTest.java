package org.motechproject.whp.container.domain;

import org.junit.Test;
import org.motechproject.whp.refdata.domain.SputumTrackingInstance;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SputumTrackingInstanceTest {
    @Test
    public void shouldValidateGivenInstanceText() {
        assertTrue(SputumTrackingInstance.isValid(SputumTrackingInstance.IN_TREATMENT.getDisplayText()));
        assertTrue(SputumTrackingInstance.isValid(SputumTrackingInstance.IN_TREATMENT.getDisplayText().toLowerCase()));
        assertFalse(SputumTrackingInstance.isValid("invalid_instance"));
    }
}
