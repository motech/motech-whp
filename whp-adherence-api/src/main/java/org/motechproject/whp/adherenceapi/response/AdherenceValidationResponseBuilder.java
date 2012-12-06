package org.motechproject.whp.adherenceapi.response;

import org.motechproject.whp.common.webservice.WebServiceResponse;
import org.motechproject.whp.patient.domain.TreatmentCategory;

import static org.motechproject.whp.common.webservice.WebServiceResponse.failure;
import static org.motechproject.whp.adherenceapi.domain.TreatmentCategoryType.GOVERNMENT;
import static org.motechproject.whp.adherenceapi.domain.TreatmentCategoryType.PRIVATE;

public class AdherenceValidationResponseBuilder {

    public static final String VALID_RANGE_FROM = "0";

    public AdherenceValidationResponse successfulResponse() {
        return new AdherenceValidationResponse(WebServiceResponse.success.name());
    }

    public AdherenceValidationResponse failureResponse(TreatmentCategory treatmentCategory) {
        AdherenceValidationResponse response = new AdherenceValidationResponse(failure.name());

        String treatmentCategoryType = treatmentCategory.isGovernmentCategory() ? GOVERNMENT.name() : PRIVATE.name();
        response.setTreatmentCategory(treatmentCategoryType);
        response.setValidRangeFrom(VALID_RANGE_FROM);
        response.setValidRangeTo(treatmentCategory.getDosesPerWeek().toString());

        return response;
    }
}
