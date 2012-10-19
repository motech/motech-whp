package org.motechproject.whp.wgninbound.verification;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.common.exception.WHPError;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.common.exception.WHPErrors;
import org.motechproject.whp.common.exception.WHPRuntimeException;
import org.motechproject.whp.common.validation.RequestValidator;
import org.motechproject.whp.wgninbound.request.ValidatorPool;
import org.motechproject.whp.wgninbound.request.VerificationRequest;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class VerificationTest {

    @Mock
    RequestValidator validator;
    @Mock
    ValidatorPool validatorPool;

    StubVerification verification;

    @Before
    public void setup() {
        initMocks(this);
        verification = new StubVerification(validator, validatorPool);
    }

    @Test
    public void shouldReturnResultWithErrorForFailedValidation() {
        VerificationRequest inputValue = new VerificationRequest();
        when(validator.validate(eq(inputValue), anyString())).thenThrow(
                new WHPRuntimeException(WHPErrorCode.INVALID_PHONE_NUMBER)
        );
        assertTrue(verification.verifyRequest(inputValue).isError());
    }

    @Test
    public void shouldReturnResultWithFalseWhenVerificationFailed() {
        VerificationRequest inputValue = new VerificationRequest();
        verification.setValid(false);
        verification.setError(new WHPError(WHPErrorCode.INVALID_PHONE_NUMBER));

        assertTrue(verification.verifyRequest(inputValue).isError());
        assertEquals(new WHPError(WHPErrorCode.INVALID_PHONE_NUMBER), verification.verifyRequest(inputValue).getErrors().get(0));
    }

    @Test
    public void shouldReturnResultWithSuccessWhenVerificationPassed() {
        verification.setValid(true);
        assertTrue(verification.verifyRequest(new VerificationRequest()).isSuccess());
    }
}

class StubVerification extends Verification<VerificationRequest> {

    private boolean isValid;
    private WHPErrors errors;

    public StubVerification(RequestValidator validator, ValidatorPool validatorPool) {
        super(validator, validatorPool);
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public void setError(WHPError error) {
        if(errors == null)
            errors = new WHPErrors();
        this.errors.add(error);
    }

    @Override
    protected WHPErrors verify(VerificationRequest request) {
        if (!isValid) {
            return errors;
        } else
            return null;
    }
}
