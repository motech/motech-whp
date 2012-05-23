package org.motechproject.whp.integration.validation.patient;

import org.junit.Test;
import org.motechproject.whp.builder.PatientWebRequestBuilder;
import org.motechproject.whp.patient.command.AllCommands;
import org.motechproject.whp.patient.exception.WHPRuntimeException;
import org.motechproject.whp.request.PatientWebRequest;

import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

public class WeightTest extends BasePatientTest {
    @Test
    public void shouldNotThrowExceptionWhenWeightIsAFraction() {
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withWeight("10.1").build();
        validator.validate(webRequest, AllCommands.create);
    }

    @Test
    public void shouldNotThrowExceptionWhenWeightIsAnInteger() {
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withWeight("10").build();
        validator.validate(webRequest, AllCommands.create);
    }

    @Test
    public void shouldNotThrowExceptionWhenWeightIsAFractionWithZeroInFractionPart() {
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withWeight("10.0").build();
        validator.validate(webRequest, AllCommands.create);
    }

    @Test
    public void shouldNotThrowException_WhenWeightInstanceIsNull_ForUpdateScope() {
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withWeightStatistics(null, null).build();
        validator.validate(webRequest, AllCommands.simpleUpdate);
    }

    @Test
    public void shouldThrowExceptionWhenWeightStartWithANumberWithAndHasAnAlphabet() {
        expectFieldValidationRuntimeException("field:weight:Weight must be a real number");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withWeight("1A").build();
        validator.validate(webRequest, AllCommands.create);
    }

    @Test
    public void shouldThrowExceptionWhenWeightIsNotNumeric() {
        expectFieldValidationRuntimeException("field:weight:Weight must be a real number");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withWeight("A").build();
        validator.validate(webRequest, AllCommands.create);
    }

    @Test
    public void shouldThrowExceptionWhenWeightStringIsJustSpaces_ForCreateScope() {
        expectFieldValidationRuntimeException("field:weight:Weight must be a real number");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withWeight(" ").build();
        validator.validate(webRequest, AllCommands.create);
    }

    @Test
    public void shouldThrowExceptionWhenWeightStringIsNotValid_ForCreateScope() {
        expectFieldValidationRuntimeException("field:weight:Weight must be a real number");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withWeight("3 5").build();
        validator.validate(webRequest, AllCommands.create);
    }

    @Test
    public void shouldThrowSingleExceptionWhenWeightIsNull_InCreateScope() {
        String errorMessage = "";
        try {
            PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withWeight(null).build();
            validator.validate(webRequest, AllCommands.create);
        } catch (WHPRuntimeException e) {
            if (e.getMessage().contains("field:weight:Weight cannot be null")) {
                fail("Not Null validation is not required. Validator implements null validation.");
            }
            errorMessage = e.getMessage();
        }
        assertTrue(errorMessage.contains("field:weight:value should not be null"));
    }

    @Test
    public void shouldThrowSingleExceptionWhenWeightIsNull_InOpenTreatmentScope() {
        String errorMessage = "";
        try {
            PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withWeight(null).build();
            validator.validate(webRequest, AllCommands.openTreatment);
        } catch (WHPRuntimeException e) {
            if (e.getMessage().contains("field:weight:Weight cannot be null")) {
                fail("Not Null validation is not required. Validator implements null validation.");
            }
            errorMessage = e.getMessage();
        }
        assertTrue(errorMessage.contains("field:weight:value should not be null"));
    }

    @Test
    public void shouldThrowSingleExceptionWhenWeightInstanceIsNull_InCreateScope() {
        String errorMessage = "";
        try {
            PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withWeightStatistics(null, null).build();
            validator.validate(webRequest, AllCommands.create);
        } catch (WHPRuntimeException e) {
            if (e.getMessage().contains("field:weight_instance:Weight cannot be null")) {
                fail("Not Null validation is not required. Validator implements null validation.");
            }
            errorMessage = e.getMessage();
        }
        assertTrue(errorMessage.contains("field:weight_instance:value should not be null"));
    }

    @Test
    public void shouldThrowSingleExceptionWhenWeightInstanceIsNull_InOpenTreatmentScope() {
        String errorMessage = "";
        try {
            PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withWeightStatistics(null, null).build();
            validator.validate(webRequest, AllCommands.openTreatment);
        } catch (WHPRuntimeException e) {
            if (e.getMessage().contains("field:weight_instance:Weight cannot be null")) {
                fail("Not Null validation is not required. Validator implements null validation.");
            }
            errorMessage = e.getMessage();
        }
        assertTrue(errorMessage.contains("field:weight_instance:value should not be null"));
    }
}
