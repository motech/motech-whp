package org.motechproject.whp.wgninbound.reponse;

import org.junit.Test;
import org.motechproject.whp.common.exception.WHPError;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.wgninbound.response.VerificationResult;

import java.util.Collections;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class VerificationResultTest {

    @Test
    public void shouldBeAnErrorResultWhenWHPErrorOccurred() {
        assertTrue(new VerificationResult(new WHPError(WHPErrorCode.INVALID_PHONE_NUMBER), "invalidPhoneNumber").isError());
    }

    @Test
    public void shouldNotBeASuccessResultWhenWHPErrorOccurred() {
        assertFalse(new VerificationResult(new WHPError(WHPErrorCode.WEB_ACCOUNT_REGISTRATION_ERROR), "invalidPhoneNumber").isSuccess());
    }

    @Test
    public void shouldNotBeAnErrorResultWhenAddingEmptyErrors() {
        VerificationResult result = new VerificationResult("value");
        result.addAllErrors(Collections.<WHPError>emptyList());
        assertFalse(result.isError());
    }

    @Test
    public void shouldNotBeAnErrorResultWhenAddingNullErrors() {
        VerificationResult result = new VerificationResult("value");
        result.addAllErrors(null);
        assertFalse(result.isError());
    }

    @Test
    public void shouldReturnEmptyErrorsAfterAddingNullErrors() {
        VerificationResult result = new VerificationResult("value");
        result.addAllErrors(null);
        assertTrue(result.getErrors().isEmpty());
    }

    @Test
    public void shouldReturnEmptyErrorsAfterAddingListOfNullErrors() {
        VerificationResult result = new VerificationResult("value");
        result.addAllErrors(asList((WHPError) null));
        assertTrue(result.getErrors().isEmpty());
    }

    @Test
    public void shouldBeEqualBasedOnValueAndNotByReference() {
        VerificationResult result = new VerificationResult("value");
        VerificationResult anotherResult = new VerificationResult("value");

        assertTrue(result.equals(anotherResult));
    }

    @Test
    public void shouldBeEqualBasedOnValueAndErrorAndNotByReference() {
        VerificationResult result = new VerificationResult(new WHPError(WHPErrorCode.INVALID_PHONE_NUMBER), "value");
        VerificationResult anotherResult = new VerificationResult(new WHPError(WHPErrorCode.INVALID_PHONE_NUMBER), "value");

        assertTrue(result.equals(anotherResult));
    }

    @Test
    public void shouldNotBeEqualBasedOnValue() {
        VerificationResult result = new VerificationResult("value");
        VerificationResult anotherResult = new VerificationResult("anotherValue");

        assertFalse(result.equals(anotherResult));
    }

    @Test
    public void shouldNotBeEqualBasedOnError() {
        VerificationResult result = new VerificationResult(new WHPError(WHPErrorCode.INVALID_PHONE_NUMBER), "value");
        VerificationResult anotherResult = new VerificationResult(new WHPError(WHPErrorCode.INVALID_CONTAINER_ID), "value");

        assertFalse(result.equals(anotherResult));
    }
}
