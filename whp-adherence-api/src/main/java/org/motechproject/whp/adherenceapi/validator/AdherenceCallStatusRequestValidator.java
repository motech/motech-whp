package org.motechproject.whp.adherenceapi.validator;

import org.motechproject.whp.adherenceapi.errors.AdherenceErrors;
import org.motechproject.whp.adherenceapi.request.AdherenceCallStatusRequest;
import org.motechproject.whp.adherenceapi.response.validation.AdherenceValidationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.motechproject.whp.adherenceapi.response.validation.AdherenceValidationResponse.failure;
import static org.motechproject.whp.adherenceapi.response.validation.AdherenceValidationResponse.success;

@Component
public class AdherenceCallStatusRequestValidator {

    private AdherenceRequestValidator adherenceRequestValidator;

    @Autowired
    public AdherenceCallStatusRequestValidator(AdherenceRequestValidator adherenceRequestValidator) {
        this.adherenceRequestValidator = adherenceRequestValidator;
    }

    public AdherenceValidationResponse validate(AdherenceCallStatusRequest request) {
        AdherenceErrors errors = adherenceRequestValidator.validateProvider(request.getProviderId());
        if (errors.isNotEmpty())
            return failure(errors.errorMessage());
        return success();
    }
}
