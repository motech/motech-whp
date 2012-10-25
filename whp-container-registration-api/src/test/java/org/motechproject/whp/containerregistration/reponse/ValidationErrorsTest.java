package org.motechproject.whp.containerregistration.reponse;

import org.junit.Test;
import org.motechproject.whp.common.exception.WHPError;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.containerregistration.response.ValidationErrors;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class ValidationErrorsTest {

    @Test
    public void shouldInitializeError() {
        ValidationErrors validationErrors = new ValidationErrors(asList(new WHPError(WHPErrorCode.INVALID_PHONE_NUMBER)));
        assertEquals(WHPErrorCode.INVALID_PHONE_NUMBER.name(), validationErrors.value().get(0).errorCode());
        assertEquals(WHPErrorCode.INVALID_PHONE_NUMBER.getMessage(), validationErrors.value().get(0).errorDescription());
    }

    @Test
    public void shouldInitializeAllErrors() {
        ValidationErrors validationErrors = new ValidationErrors(
                asList(
                        new WHPError(WHPErrorCode.INVALID_PHONE_NUMBER),
                        new WHPError(WHPErrorCode.DUPLICATE_CASE_ID)
                )
        );
        assertEquals(WHPErrorCode.INVALID_PHONE_NUMBER.name(), validationErrors.value().get(0).errorCode());
        assertEquals(WHPErrorCode.DUPLICATE_CASE_ID.name(), validationErrors.value().get(1).errorCode());
    }
}
