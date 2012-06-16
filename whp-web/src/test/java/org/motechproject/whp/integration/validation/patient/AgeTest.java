package org.motechproject.whp.integration.validation.patient;

import org.junit.Test;
import org.motechproject.whp.builder.PatientWebRequestBuilder;
import org.motechproject.whp.contract.PatientWebRequest;
import org.motechproject.whp.patient.command.UpdateScope;
import org.motechproject.whp.patient.exception.WHPRuntimeException;

import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

public class AgeTest extends BasePatientTest {
    @Test
    public void shouldThrowExceptionWhenAgeIsNotNumeric() {
        expectFieldValidationRuntimeException("field:age:Age must be numeric and not fractional");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withAge("A").build();
        validator.validate(webRequest, UpdateScope.createScope);
    }

    @Test
    public void shouldThrowExceptionWhenAgeIsAFractionInCreateScope() {
        expectFieldValidationRuntimeException("field:age:Age must be numeric and not fractional");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withAge("10.1").build();
        validator.validate(webRequest, UpdateScope.createScope);
    }

    @Test
    public void shouldNotThrowExceptionWhenAgeIsNotSetInSimpleUpdate(){
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withAge(null).build();
        validator.validate(webRequest, UpdateScope.simpleUpdateScope);
    }

    @Test
    public void shouldThrowExceptionWhenAgeIsAFractionInSimpleUpdateScope() {
        expectFieldValidationRuntimeException("field:age:Age must be numeric and not fractional");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withAge("10.1").build();
        validator.validate(webRequest, UpdateScope.simpleUpdateScope);
    }

    @Test
    public void shouldNotThrowExceptionWhenAgeIsNumericInCreateScope() {
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withAge("10").build();
        validator.validate(webRequest, UpdateScope.createScope);
    }

    @Test
    public void shouldNotThrowExceptionWhenAgeIsNumericInSimpleUpdateScope() {
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withAge("10").build();
        validator.validate(webRequest, UpdateScope.simpleUpdateScope);
    }

    @Test
    public void shouldThrowExceptionWhenAgeIsNull() {
        String errorMessage = "";
        try {
            PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withAge(null).build();
            validator.validate(webRequest, UpdateScope.createScope);
        } catch (WHPRuntimeException e) {
            if (e.getMessage().contains("field:age:Age cannot be null")) {
                fail("Not Null validation is not required. Validator implements null validation.");
            }
            errorMessage = e.getMessage();
        }
        assertTrue(errorMessage.contains("field:age:value should not be null"));
    }
}
