package org.motechproject.whp.adherenceapi.validator;

import org.motechproject.whp.adherenceapi.domain.ProviderId;
import org.motechproject.whp.adherenceapi.errors.AdherenceErrors;
import org.motechproject.whp.adherenceapi.request.AdherenceValidationRequest;
import org.motechproject.whp.adherenceapi.response.validation.AdherenceValidationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.motechproject.whp.adherenceapi.response.validation.AdherenceValidationResponse.failure;
import static org.motechproject.whp.adherenceapi.response.validation.AdherenceValidationResponse.success;

@Component
public class AdherenceNotCapturedRequestValidator {
    private AdherenceRequestValidator adherenceRequestValidator;

    @Autowired
    public AdherenceNotCapturedRequestValidator(AdherenceRequestValidator adherenceRequestValidator) {
        this.adherenceRequestValidator = adherenceRequestValidator;
    }

    public AdherenceValidationResponse validate(AdherenceValidationRequest request, ProviderId providerId) {
        AdherenceErrors errors = adherenceRequestValidator.validatePatientProviderMapping(request.getPatientId(), providerId);
        if (errors.isNotEmpty())
            return failure(errors.errorMessage());
        return success();
    }
}
