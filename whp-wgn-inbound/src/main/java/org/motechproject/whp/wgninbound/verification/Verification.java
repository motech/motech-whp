package org.motechproject.whp.wgninbound.verification;

import org.motechproject.whp.common.exception.WHPError;
import org.motechproject.whp.common.exception.WHPErrors;
import org.motechproject.whp.common.exception.WHPRuntimeException;
import org.motechproject.whp.common.validation.RequestValidator;
import org.motechproject.whp.wgninbound.request.ValidatorPool;
import org.motechproject.whp.wgninbound.request.VerificationRequest;
import org.motechproject.whp.wgninbound.response.VerificationResult;

import java.util.Collections;
import java.util.List;

public abstract class Verification<T extends VerificationRequest> {

    private RequestValidator validator;
    protected ValidatorPool validatorPool;

    public Verification(RequestValidator validator, ValidatorPool validatorPool) {
        this.validator = validator;
        this.validatorPool = validatorPool;
    }

    public VerificationResult verifyRequest(T request) {
        VerificationResult result = validateRequest(request);
        if (result.isSuccess()) {
            result.addAllErrors(verify(request));
        }
        return result;
    }

    protected abstract WHPErrors verify(T request);

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
