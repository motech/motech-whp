package org.motechproject.whp.adherenceapi.validator;

import org.motechproject.whp.adherenceapi.domain.Dosage;
import org.motechproject.whp.adherenceapi.domain.ProviderId;
import org.motechproject.whp.adherenceapi.errors.AdherenceErrors;
import org.motechproject.whp.adherenceapi.request.AdherenceValidationRequest;
import org.motechproject.whp.adherenceapi.response.validation.AdherenceValidationResponse;
import org.motechproject.whp.adherenceapi.service.AdherenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.motechproject.whp.adherenceapi.response.validation.AdherenceValidationResponse.success;

@Component
public class AdherenceValidationRequestValidator {
    private AdherenceService adherenceService;
    private AdherenceRequestValidator adherenceRequestValidator;

    @Autowired
    public AdherenceValidationRequestValidator(AdherenceService adherenceService, AdherenceRequestValidator adherenceRequestValidator) {
        this.adherenceService = adherenceService;
        this.adherenceRequestValidator = adherenceRequestValidator;
    }

    public AdherenceValidationResponse validate(AdherenceValidationRequest request, ProviderId providerId) {
        AdherenceErrors errors = adherenceRequestValidator.validatePatientProviderMapping(request.getPatientId(), providerId);
        Dosage dosage = adherenceService.dosageForPatient(request.getPatientId());
        if (errors.isNotEmpty()) {
            return AdherenceValidationResponse.failure(errors.errorMessage());
        } else if (isValidDose(request, dosage)) {
            return success();
        } else {
            return AdherenceValidationResponse.failure(dosage);
        }
    }

    private boolean isValidDose(AdherenceValidationRequest adherenceValidationRequest, Dosage dosage) {
        return dosage.isValidInput(adherenceValidationRequest.doseTakenCount());
    }
}
