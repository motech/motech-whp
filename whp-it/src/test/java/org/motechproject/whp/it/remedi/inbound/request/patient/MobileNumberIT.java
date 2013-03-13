package org.motechproject.whp.it.remedi.inbound.request.patient;

import org.junit.Test;
import org.motechproject.whp.patient.command.UpdateScope;
import org.motechproject.whp.webservice.builder.PatientWebRequestBuilder;
import org.motechproject.whp.webservice.request.PatientWebRequest;

public class MobileNumberIT extends BasePatientIT {
    public void shouldNotThrowException_WhenMobileNumberIsEmpty() {
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withDefaultMobileNumber().build();
        validator.validate(webRequest, UpdateScope.createScope);
    }

    @Test
    public void shouldNotThrowException_WhenMobileNumberIs10Digits() {
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withDefaultMobileNumber().build();
        validator.validate(webRequest, UpdateScope.createScope);
    }

    @Test
    public void shouldThrowException_WhenMobileNumberIsLessThan10Digits() {
        expectFieldValidationRuntimeException("field:mobile_number:Mobile number should be empty or should have 10 digits");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withMobileNumber("12345").build();
        validator.validate(webRequest, UpdateScope.createScope);
    }

    @Test
    public void shouldThrowException_WhenMobileNumberIsMoreThan10Digits() {
        expectFieldValidationRuntimeException("field:mobile_number:Mobile number should be empty or should have 10 digits");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withMobileNumber("1234567890987654").build();
        validator.validate(webRequest, UpdateScope.createScope);
    }

    @Test
    public void shouldThrowException_WhenMobileNumberIsNotNumeric() {
        expectFieldValidationRuntimeException("field:mobile_number:Mobile number should be empty or should have 10 digits");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withMobileNumber("hahahahaha").build();
        validator.validate(webRequest, UpdateScope.createScope);
    }

    @Test
    public void shouldNotThrowException_WhenMobileNumberIsNull() {
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withMobileNumber(null  ).build();
        validator.validate(webRequest, UpdateScope.createScope);
    }
}
