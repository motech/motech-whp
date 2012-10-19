package org.motechproject.whp.common.exception;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WHPErrorsTest {
    @Test
    public void shouldAddElementIntoTheListIfItDoesNotExist() {
        WHPErrors errors = new WHPErrors();
        errors.add(new WHPError(WHPErrorCode.INVALID_CONTAINER_ID));
        errors.add(new WHPError(WHPErrorCode.INVALID_CONTAINER_ID));

        assertEquals(1, errors.size());
    }

    @Test
    public void shouldReturnAllErrors() {
        WHPErrors errors = new WHPErrors();
        errors.add(new WHPError(WHPErrorCode.INVALID_CONTAINER_ID));
        errors.add(new WHPError(WHPErrorCode.INVALID_PHASE));

        assertEquals(2, errors.size());
        assertEquals(WHPErrorCode.INVALID_CONTAINER_ID, errors.get(0).getErrorCode());
        assertEquals(WHPErrorCode.INVALID_PHASE, errors.get(1).getErrorCode());
    }
}
