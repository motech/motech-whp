package org.motechproject.whp.adherenceapi.response.validation;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AdherenceValidationResponseTest {
    @Test
    public void shouldReturnWhetherResponseIsFailureOrNot() {
        assertTrue(AdherenceValidationResponse.failure().failed());
        assertTrue(AdherenceValidationResponse.failure("errorCode").failed());
        assertFalse(AdherenceValidationResponse.success().failed());
    }
}
