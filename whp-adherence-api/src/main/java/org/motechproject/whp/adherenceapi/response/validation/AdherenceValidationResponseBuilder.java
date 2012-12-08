package org.motechproject.whp.adherenceapi.response.validation;

import org.motechproject.whp.adherenceapi.domain.TreatmentCategoryInfo;
import org.motechproject.whp.adherenceapi.response.validation.AdherenceValidationResponse;
import org.motechproject.whp.common.webservice.WebServiceResponse;
import org.springframework.stereotype.Component;

@Component
public class AdherenceValidationResponseBuilder {

    public AdherenceValidationResponse successfulResponse() {
        return new AdherenceValidationResponse();
    }

    public AdherenceValidationResponse invalidDosageFailureResponse(TreatmentCategoryInfo treatmentCategoryInfo) {
        AdherenceValidationResponse response = new AdherenceValidationResponse();

        response.setResult(WebServiceResponse.failure);
        response.setTreatmentCategory(treatmentCategoryInfo.getTreatmentProvider().name());
        response.setValidRangeFrom(treatmentCategoryInfo.getValidRangeFrom());
        response.setValidRangeTo(treatmentCategoryInfo.getValidRangeTo());

        return response;
    }

    public AdherenceValidationResponse validationFailureResponse() {
        AdherenceValidationResponse response = new AdherenceValidationResponse();
        response.setResult(WebServiceResponse.failure);
        return response;
    }
}
