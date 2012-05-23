package org.motechproject.whp.integration.validation.patient;

import org.junit.Test;
import org.motechproject.whp.builder.PatientWebRequestBuilder;
import org.motechproject.whp.patient.command.UpdateScope;
import org.motechproject.whp.patient.exception.WHPError;
import org.motechproject.whp.patient.exception.WHPErrorCode;
import org.motechproject.whp.patient.exception.WHPRuntimeException;
import org.motechproject.whp.request.PatientWebRequest;

import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

public class TreatmentCategoryTest extends BasePatientTest {

    @Test
    public void shouldThrowExceptionWhenTreatmentCategoryIsValid() {
        validate(new PatientWebRequestBuilder().withDefaults().withTreatmentCategory("01").build());
        validate(new PatientWebRequestBuilder().withDefaults().withTreatmentCategory("02").build());
        validate(new PatientWebRequestBuilder().withDefaults().withTreatmentCategory("11").build());
        validate(new PatientWebRequestBuilder().withDefaults().withTreatmentCategory("12").build());
    }

    @Test
    public void shouldNotValidateTreatmentCategoryOnSimpleUpdate() {
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withTreatmentCategory(null).withLastModifiedDate("03/04/2012 02:20:30").build();
        validator.validate(webRequest, UpdateScope.simpleUpdateScope);
    }

    @Test
    public void shouldThrowException_WhenTreatmentCategoryIsNotValid() {
        expectFieldValidationRuntimeException("field:treatment_category:must match \"[0|1][1|2]\"");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withTreatmentCategory("99").build();
        validator.validate(webRequest, UpdateScope.createScope);
    }

    @Test
    public void shouldThrowSingleException_WhenTreatmentCategoryIsNull() {
        try {
            PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withTreatmentCategory(null).build();
            validator.validate(webRequest, UpdateScope.createScope);
        } catch (WHPRuntimeException e) {
            WHPError error = e.error(WHPErrorCode.FIELD_VALIDATION_FAILED);
            if (error != null && error.getMessage().contains("field:treatment_category:Treatment Category cannot be null"))
                fail("Not Null validation is not required. Validator implements null validation.");
            assertTrue(error.getMessage().contains("field:treatment_category:value should not be null"));
        }
    }

}
