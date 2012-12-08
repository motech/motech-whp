package org.motechproject.whp.adherenceapi.service;

import org.motechproject.whp.adherenceapi.request.AdherenceValidationRequest;
import org.motechproject.whp.adherenceapi.response.validation.AdherenceValidationResponse;
import org.motechproject.whp.adherenceapi.response.validation.AdherenceValidationResponseBuilder;
import org.motechproject.whp.adherenceapi.validator.AdherenceRequestsValidator;
import org.motechproject.whp.common.error.ErrorWithParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdherenceWebService {

    private AdherenceService adherenceService;
    private AdherenceRequestsValidator adherenceRequestsValidator;
    private AdherenceValidationResponseBuilder adherenceValidationResponseBuilder;

    @Autowired
    public AdherenceWebService(AdherenceService adherenceService, AdherenceRequestsValidator adherenceRequestsValidator, AdherenceValidationResponseBuilder adherenceValidationResponseBuilder) {
        this.adherenceService = adherenceService;
        this.adherenceRequestsValidator = adherenceRequestsValidator;
        this.adherenceValidationResponseBuilder = adherenceValidationResponseBuilder;
    }

    public AdherenceValidationResponse processValidationRequest(AdherenceValidationRequest adherenceValidationRequest) {
        ErrorWithParameters errors = adherenceRequestsValidator.validateValidationRequest(adherenceValidationRequest);
        if (errors == null) {
            if (adherenceService.validateDosage(adherenceValidationRequest.getPatientId(), adherenceValidationRequest.getDoseTakenCount()))
                return adherenceValidationResponseBuilder.successfulResponse();
            return adherenceValidationResponseBuilder.invalidDosageFailureResponse(adherenceService.getTreatmentCategoryInformation(adherenceValidationRequest.getPatientId()));
        }
        return adherenceValidationResponseBuilder.validationFailureResponse();
    }
}
