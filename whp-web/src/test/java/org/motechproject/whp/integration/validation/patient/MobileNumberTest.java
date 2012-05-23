package org.motechproject.whp.integration.validation.patient;

import org.junit.Test;
import org.motechproject.whp.builder.PatientWebRequestBuilder;
import org.motechproject.whp.patient.service.AllCommands;
import org.motechproject.whp.request.PatientWebRequest;

public class MobileNumberTest extends BasePatientTest {
    public void shouldNotThrowException_WhenMobileNumberIsEmpty() {
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withMobileNumber("").build();
        validator.validate(webRequest, AllCommands.create);
    }

    @Test
    public void shouldNotThrowException_WhenMobileNumberIs10Digits() {
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withMobileNumber("1234567890").build();
        validator.validate(webRequest, AllCommands.create);
    }

    @Test
    public void shouldThrowException_WhenMobileNumberIsLessThan10Digits() {
        expectFieldValidationRuntimeException("field:mobile_number:Mobile number should be empty or should have 10 digits");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withMobileNumber("123456789").build();
        validator.validate(webRequest, AllCommands.create);
    }

    @Test
    public void shouldThrowException_WhenMobileNumberIsMoreThan10Digits() {
        expectFieldValidationRuntimeException("field:mobile_number:Mobile number should be empty or should have 10 digits");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withMobileNumber("12345678901").build();
        validator.validate(webRequest, AllCommands.create);
    }

    @Test
    public void shouldThrowException_WhenMobileNumberIsNotNumeric() {
        expectFieldValidationRuntimeException("field:mobile_number:Mobile number should be empty or should have 10 digits");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withMobileNumber("123456789a").build();
        validator.validate(webRequest, AllCommands.create);
    }

    @Test
    public void shouldNotThrowException_WhenMobileNumberIsNull() {
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withMobileNumber(null).build();
        validator.validate(webRequest, AllCommands.create);
    }
}
