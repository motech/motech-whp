package org.motechproject.whp.adherenceapi.request;

import org.junit.Test;
import org.motechproject.whp.adherenceapi.domain.TreatmentCategoryInfo;
import org.motechproject.whp.adherenceapi.response.validation.AdherenceValidationResponse;
import org.motechproject.whp.adherenceapi.response.validation.AdherenceValidationResponseBuilder;
import org.motechproject.whp.common.webservice.WebServiceResponse;
import org.motechproject.whp.patient.domain.TreatmentCategory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.motechproject.whp.adherenceapi.domain.TreatmentProvider.GOVERNMENT;
import static org.motechproject.whp.common.webservice.WebServiceResponse.failure;

public class AdherenceValidationResponseBuilderTest {

    @Test
    public void shouldReturnASuccessfulResponse() {
        AdherenceValidationResponse response = new AdherenceValidationResponseBuilder().successfulResponse();

        assertEquals(WebServiceResponse.success, response.getResult());
        assertNull(response.getTreatmentCategory());
        assertNull(response.getValidRangeFrom());
        assertNull(response.getValidRangeTo());
    }

    @Test
    public void shouldReturnInvalidDosageFailureResponse() {
        Integer dosesPerWeek = 3;
        TreatmentCategory treatmentCategory = new TreatmentCategory();
        treatmentCategory.setDosesPerWeek(dosesPerWeek);
        TreatmentCategoryInfo treatmentCategoryinfo = new TreatmentCategoryInfo(treatmentCategory);

        AdherenceValidationResponse response = new AdherenceValidationResponseBuilder().invalidDosageFailureResponse(treatmentCategoryinfo);

        assertEquals(failure, response.getResult());
        assertEquals(GOVERNMENT.name(), response.getTreatmentCategory());
        assertEquals(treatmentCategoryinfo.getValidRangeFrom(), response.getValidRangeFrom());
        assertEquals(treatmentCategoryinfo.getValidRangeTo(), response.getValidRangeTo());
    }

    @Test
    public void shouldReturnValidationFailureResponse() {
        AdherenceValidationResponse response = new AdherenceValidationResponseBuilder().validationFailureResponse();

        assertEquals(failure, response.getResult());
        assertNull(response.getTreatmentCategory());
        assertNull(response.getValidRangeFrom());
        assertNull(response.getValidRangeTo());
    }
}
