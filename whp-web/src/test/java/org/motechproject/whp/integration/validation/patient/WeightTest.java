package org.motechproject.whp.integration.validation.patient;

import org.junit.Test;
import org.motechproject.whp.builder.PatientWebRequestBuilder;
import org.motechproject.whp.patient.command.UpdateScope;
import org.motechproject.whp.contract.PatientWebRequest;

public class WeightTest extends BasePatientTest {

    @Test
    public void shouldNotThrowExceptionWhenWeightIsAFraction() {
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withWeight("10.1").build();
        validator.validate(webRequest, UpdateScope.createScope);
    }

    @Test
    public void shouldNotThrowExceptionWhenWeightIsAnInteger() {
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withWeight("10").build();
        validator.validate(webRequest, UpdateScope.createScope);
    }

    @Test
    public void shouldNotThrowExceptionWhenWeightIsAFractionWithZeroInFractionPart() {
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withWeight("10.0").build();
        validator.validate(webRequest, UpdateScope.createScope);
    }

    @Test
    public void shouldNotThrowException_WhenWeightInstanceIsNull_ForUpdateScope() {
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withWeightStatistics(null, null).build();
        validator.validate(webRequest, UpdateScope.simpleUpdateScope);
    }

    @Test
    public void shouldThrowExceptionWhenWeightStartWithANumberWithAndHasAnAlphabet() {
        expectFieldValidationRuntimeException("field:weight:Weight must be a real number");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withWeight("1A").build();
        validator.validate(webRequest, UpdateScope.createScope);
    }

    @Test
    public void shouldThrowExceptionWhenWeightIsNotNumeric() {
        expectFieldValidationRuntimeException("field:weight:Weight must be a real number");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withWeight("A").build();
        validator.validate(webRequest, UpdateScope.createScope);
    }

    @Test
    public void shouldThrowExceptionWhenWeightStringIsJustSpaces_ForCreateScope() {
        expectFieldValidationRuntimeException("field:weight:Weight must be a real number");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withWeight(" ").build();
        validator.validate(webRequest, UpdateScope.createScope);
    }

    @Test
    public void shouldThrowExceptionWhenWeightStringIsNotValid_ForCreateScope() {
        expectFieldValidationRuntimeException("field:weight:Weight must be a real number");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withWeight("3 5").build();
        validator.validate(webRequest, UpdateScope.createScope);
    }

}
