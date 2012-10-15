package org.motechproject.whp.wgninbound.verification;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.common.exception.WHPError;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.common.exception.WHPRuntimeException;
import org.motechproject.whp.common.validation.RequestValidator;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class VerificationTest {

    @Mock
    RequestValidator validator;

    StubVerification verification;

    @Before
    public void setup() {
        initMocks(this);
        verification = new StubVerification(validator);
    }

    @Test
    public void shouldReturnResultWithErrorForFailedValidation() {
        String inputValue = "value";
        when(validator.validate(eq(inputValue), anyString())).thenThrow(
                new WHPRuntimeException(WHPErrorCode.INVALID_PHONE_NUMBER)
        );
        assertTrue(verification.verifyRequest(inputValue).isError());
    }

    @Test
    public void shouldReturnResultWithFalseWhenVerificationFailed() {
        String inputValue = "value";
        verification.setValid(false);
        verification.setError(new WHPError(WHPErrorCode.INVALID_PHONE_NUMBER));

        assertTrue(verification.verifyRequest(inputValue).isError());
        assertEquals(new WHPError(WHPErrorCode.INVALID_PHONE_NUMBER), verification.verifyRequest(inputValue).getErrors().get(0));
    }

    @Test
    public void shouldReturnResultWithSuccessWhenVerificationPassed() {
        String inputValue = "value";
        verification.setValid(true);

        assertTrue(verification.verifyRequest(inputValue).isSuccess());
    }
}

class StubVerification extends Verification<String> {

    private boolean isValid;
    private WHPError error;

    public StubVerification(RequestValidator validator) {
        super(validator);
    }

    @Override
    protected String getVerifiedValue(String request) {
        return request;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public WHPError getError() {
        return error;
    }

    public void setError(WHPError error) {
        this.error = error;
    }

    @Override
    protected WHPError verify(String request) {
        if (!isValid) {
            return error;
        } else
            return null;
    }
}
