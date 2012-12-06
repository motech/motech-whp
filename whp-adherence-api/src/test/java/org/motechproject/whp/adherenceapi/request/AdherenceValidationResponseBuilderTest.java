package org.motechproject.whp.adherenceapi.request;

import org.junit.Test;
import org.motechproject.whp.adherenceapi.response.AdherenceValidationResponse;
import org.motechproject.whp.adherenceapi.response.AdherenceValidationResponseBuilder;
import org.motechproject.whp.common.webservice.WebServiceResponse;
import org.motechproject.whp.patient.domain.TreatmentCategory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.motechproject.whp.adherenceapi.response.AdherenceValidationResponseBuilder.VALID_RANGE_FROM;
import static org.motechproject.whp.common.webservice.WebServiceResponse.failure;
import static org.motechproject.whp.adherenceapi.domain.TreatmentCategoryType.GOVERNMENT;

public class AdherenceValidationResponseBuilderTest {

    @Test
    public void shouldReturnASuccessfulResponse() {
        AdherenceValidationResponse response = new AdherenceValidationResponseBuilder().successfulResponse();

        assertEquals(WebServiceResponse.success.name(), response.getResult());
        assertNull(response.getTreatmentCategory());
        assertNull(response.getValidRangeFrom());
        assertNull(response.getValidRangeTo());
    }

    @Test
    public void shouldReturnFailureResponse() {
        Integer dosesPerWeek = 3;
        TreatmentCategory treatmentCategory = new TreatmentCategory();
        treatmentCategory.setDosesPerWeek(dosesPerWeek);

        AdherenceValidationResponse response = new AdherenceValidationResponseBuilder().failureResponse(treatmentCategory);

        assertEquals(failure.name(), response.getResult());
        assertEquals(GOVERNMENT.name(), response.getTreatmentCategory());
        assertEquals(VALID_RANGE_FROM, response.getValidRangeFrom());
        assertEquals(dosesPerWeek.toString(), response.getValidRangeTo());
    }
}
