package org.motechproject.whp.adherenceapi.response.validation;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AdherenceValidationResponseTest {
    @Test
    public void shouldReturnWhetherResponseIsFailureOrNot() {
        assertTrue(new AdherenceValidationResponse().failure().failed());
        assertTrue(new AdherenceValidationResponse().failure("errorCode").failed());
        assertFalse(new AdherenceValidationResponse().success().failed());
    }
}
