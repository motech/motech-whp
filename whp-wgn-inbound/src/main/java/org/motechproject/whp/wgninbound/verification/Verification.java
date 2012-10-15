package org.motechproject.whp.wgninbound.verification;

import org.motechproject.whp.common.exception.WHPError;
import org.motechproject.whp.common.exception.WHPRuntimeException;
import org.motechproject.whp.common.validation.RequestValidator;
import org.motechproject.whp.wgninbound.response.VerificationResult;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;

public abstract class Verification<T> {

    private RequestValidator validator;

    public Verification(RequestValidator validator) {
        this.validator = validator;
    }

    protected abstract String getVerifiedValue(T request);

    protected abstract WHPError verify(T request);

    public VerificationResult verifyRequest(T request) {
        VerificationResult result = validateRequest(request);
        if (result.isSuccess()) {
            result.addAllErrors(asList(verify(request)));
        }
        return result;
    }

    private VerificationResult validateRequest(T request) {
        VerificationResult result = new VerificationResult(getVerifiedValue(request));
        result.addAllErrors(validateFields(request));
        return result;
    }

    private List<WHPError> validateFields(T request) {
        try {
            validator.validate(request, "");
        } catch (WHPRuntimeException exception) {
            return exception.getErrors();
        }
        return Collections.emptyList();
    }
}
