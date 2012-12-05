package org.motechproject.whp.adherenceapi.request;

import org.junit.Test;
import org.motechproject.whp.adherenceapi.response.AdherenceValidationResponse;
import org.motechproject.whp.adherenceapi.response.AdherenceValidationResponseBuilder;
import org.motechproject.whp.patient.domain.TreatmentCategory;
import org.motechproject.whp.patient.domain.TreatmentCategoryType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AdherenceValidationResponseBuilderTest {

    @Test
    public void shouldReturnASuccessfulResponse() {
        AdherenceValidationResponse response = new AdherenceValidationResponseBuilder().successfulResponse();

        assertEquals("success", response.getResult());
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

        assertEquals("failure", response.getResult());
        assertEquals(TreatmentCategoryType.GOVERNMENT.name(), response.getTreatmentCategory());
        assertEquals(AdherenceValidationResponseBuilder.VALID_RANGE_FROM, response.getValidRangeFrom());
        assertEquals(dosesPerWeek.toString(), response.getValidRangeTo());
    }
}
