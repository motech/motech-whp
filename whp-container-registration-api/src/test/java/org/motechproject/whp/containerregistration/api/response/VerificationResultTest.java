package org.motechproject.whp.containerregistration.api.response;

import org.junit.Test;
import org.motechproject.whp.common.exception.WHPError;
import org.motechproject.whp.common.exception.WHPErrorCode;

import java.util.Collections;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class VerificationResultTest {

    @Test
    public void shouldBeAnErrorResultWhenWHPErrorOccurred() {
        assertTrue(new VerificationResult(new WHPError(WHPErrorCode.INVALID_PHONE_NUMBER)).isError());
    }

    @Test
    public void shouldNotBeASuccessResultWhenWHPErrorOccurred() {
        assertFalse(new VerificationResult(new WHPError(WHPErrorCode.WEB_ACCOUNT_REGISTRATION_ERROR)).isSuccess());
    }

    @Test
    public void shouldNotBeAnErrorResultWhenAddingEmptyErrors() {
        VerificationResult result = new VerificationResult();
        result.addAllErrors(Collections.<WHPError>emptyList());
        assertFalse(result.isError());
    }

    @Test
    public void shouldNotBeAnErrorResultWhenAddingNullErrors() {
        VerificationResult result = new VerificationResult();
        result.addAllErrors(null);
        assertFalse(result.isError());
    }

    @Test
    public void shouldReturnEmptyErrorsAfterAddingNullErrors() {
        VerificationResult result = new VerificationResult();
        result.addAllErrors(null);
        assertTrue(result.getErrors().isEmpty());
    }

    @Test
    public void shouldReturnEmptyErrorsAfterAddingListOfNullErrors() {
        VerificationResult result = new VerificationResult();
        result.addAllErrors(asList((WHPError) null));
        assertTrue(result.getErrors().isEmpty());
    }


    @Test
    public void shouldBeEqualBasedOnErrorAndNotByReference() {
        VerificationResult result = new VerificationResult(new WHPError(WHPErrorCode.INVALID_PHONE_NUMBER));
        VerificationResult anotherResult = new VerificationResult(new WHPError(WHPErrorCode.INVALID_PHONE_NUMBER));

        assertTrue(result.equals(anotherResult));
    }


    @Test
    public void shouldNotBeEqualBasedOnError() {
        VerificationResult result = new VerificationResult(new WHPError(WHPErrorCode.INVALID_PHONE_NUMBER));
        VerificationResult anotherResult = new VerificationResult(new WHPError(WHPErrorCode.INVALID_CONTAINER_ID));

        assertFalse(result.equals(anotherResult));
    }
}
