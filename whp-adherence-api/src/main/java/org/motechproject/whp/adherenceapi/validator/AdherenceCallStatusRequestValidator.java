package org.motechproject.whp.adherenceapi.validator;

import org.motechproject.whp.adherenceapi.errors.AdherenceErrors;
import org.motechproject.whp.adherenceapi.request.AdherenceCallStatusRequest;
import org.motechproject.whp.adherenceapi.response.validation.AdherenceCallStatusValidationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdherenceCallStatusRequestValidator {

    private AdherenceRequestValidator adherenceRequestValidator;

    @Autowired
    public AdherenceCallStatusRequestValidator(AdherenceRequestValidator adherenceRequestValidator) {
        this.adherenceRequestValidator = adherenceRequestValidator;
    }

    public AdherenceCallStatusValidationResponse validate(AdherenceCallStatusRequest request) {
        AdherenceErrors errors = adherenceRequestValidator.validateProvider(request.getProviderId());
        if (errors.isNotEmpty())
            return AdherenceCallStatusValidationResponse.failure(errors.errorMessage());
        return AdherenceCallStatusValidationResponse.success();
    }
}
