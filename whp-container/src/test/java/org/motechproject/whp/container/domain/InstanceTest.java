package org.motechproject.whp.container.domain;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InstanceTest {
    @Test
    public void shouldValidateGivenInstanceText() {
        assertTrue(Instance.isValid(Instance.IN_TREATMENT.getDisplayText()));
        assertTrue(Instance.isValid(Instance.IN_TREATMENT.getDisplayText().toLowerCase()));
        assertFalse(Instance.isValid("invalid_instance"));
    }
}
