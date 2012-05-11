package org.motechproject.whp.integration.validation.patient;

import org.junit.Test;
import org.motechproject.whp.builder.PatientWebRequestBuilder;
import org.motechproject.whp.patient.exception.WHPException;
import org.motechproject.whp.request.PatientWebRequest;
import org.motechproject.whp.validation.ValidationScope;

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
        validator.validate(webRequest, ValidationScope.simpleUpdate);
    }

    @Test
    public void shouldThrowException_WhenTreatmentCategoryIsNotValid() {
        expectWHPException("field:treatment_category:must match \"[0|1][1|2]\"");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withTreatmentCategory("99").build();
        validator.validate(webRequest, ValidationScope.create);
    }

    @Test
    public void shouldThrowSingleException_WhenTreatmentCategoryIsNull() {
        String errorMessage = "";
        try {
            PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withTreatmentCategory(null).build();
            validator.validate(webRequest, ValidationScope.create);
        } catch (WHPException e) {
            if (e.getMessage().contains("field:treatment_category:Treatment Category cannot be null")) {
                fail("Not Null validation is not required. Validator implements null validation.");
            }
            errorMessage = e.getMessage();
        }
        assertTrue(errorMessage.contains("field:treatment_category:value should not be null"));
    }
}
