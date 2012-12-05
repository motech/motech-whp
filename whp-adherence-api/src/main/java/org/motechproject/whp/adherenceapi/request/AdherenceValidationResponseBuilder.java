package org.motechproject.whp.adherenceapi.request;

import org.motechproject.whp.patient.domain.TreatmentCategory;
import org.motechproject.whp.patient.domain.TreatmentCategoryType;

public class AdherenceValidationResponseBuilder {

    public static final String VALID_RANGE_FROM = "0";

    public AdherenceValidationResponse successfulResponse() {
        return new AdherenceValidationResponse("success");
    }

    public AdherenceValidationResponse failureResponse(TreatmentCategory treatmentCategory) {
        AdherenceValidationResponse response = new AdherenceValidationResponse("failure");

        String treatmentCategoryType = treatmentCategory.isGovernmentCategory() ? TreatmentCategoryType.GOVERNMENT.name() : TreatmentCategoryType.PRIVATE.name();
        response.setTreatmentCategory(treatmentCategoryType);
        response.setValidRangeFrom(VALID_RANGE_FROM);
        response.setValidRangeTo(treatmentCategory.getDosesPerWeek().toString());

        return response;
    }
}
