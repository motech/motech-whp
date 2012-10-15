package org.motechproject.whp.wgninbound.verification;

import org.motechproject.whp.common.exception.WHPError;
import org.motechproject.whp.common.exception.WHPRuntimeException;
import org.motechproject.whp.common.validation.RequestValidator;
import org.motechproject.whp.wgninbound.request.VerificationRequest;
import org.motechproject.whp.wgninbound.response.VerificationResult;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;

public abstract class Verification<T extends VerificationRequest> {

    private RequestValidator validator;

    public Verification(RequestValidator validator) {
        this.validator = validator;
    }

    public VerificationResult verifyRequest(T request) {
        VerificationResult result = validateRequest(request);
        if (result.isSuccess()) {
            result.addAllErrors(asList(verify(request)));
        }
        return result;
    }

    protected abstract WHPError verify(T request);

    private VerificationResult validateRequest(T request) {
        VerificationResult result = new VerificationResult();
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
