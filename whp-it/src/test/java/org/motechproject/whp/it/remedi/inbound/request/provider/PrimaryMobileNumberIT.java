package org.motechproject.whp.it.remedi.inbound.request.provider;

import org.junit.Test;
import org.motechproject.whp.patient.command.UpdateScope;
import org.motechproject.whp.webservice.builder.ProviderRequestBuilder;
import org.motechproject.whp.webservice.request.ProviderWebRequest;

public class PrimaryMobileNumberIT extends BaseProviderIT {
    @Test
    public void shouldThrowExceptionWhenPrimaryMobileNumberIsEmpty() {
        expectFieldValidationRuntimeException("field:primary_mobile:value should not be null");
        ProviderWebRequest providerWebRequest = new ProviderRequestBuilder().withProviderId("1a123").withDate("17/03/1990 17:03:56").withDistrict(VALID_DISTRICT).withPrimaryMobile("").build();
        validator.validate(providerWebRequest, UpdateScope.createScope); //Can be any scope. None of the validation is scope dependent.
    }

    @Test
    public void shouldThrowExceptionWhenPrimaryMobileNumberIsNull() {
        expectFieldValidationRuntimeException("field:primary_mobile:value should not be null");
        ProviderWebRequest providerWebRequest = new ProviderRequestBuilder().withProviderId("1a123").withDate("17/03/1990 17:03:56").withDistrict(VALID_DISTRICT).withPrimaryMobile(null).build();
        validator.validate(providerWebRequest, UpdateScope.createScope); //Can be any scope. None of the validation is scope dependent.
    }

    @Test
    public void shouldThrowExceptionWhenPrimaryMobileNumberIsLessThan10Digits() {
        expectFieldValidationRuntimeException("field:primary_mobile:Mobile number should have 10 digits");
        ProviderWebRequest providerWebRequest = new ProviderRequestBuilder().withProviderId("1a123").withDate("17/03/1990 17:03:56").withDistrict(VALID_DISTRICT).withPrimaryMobile("1234").build();
        validator.validate(providerWebRequest, UpdateScope.createScope); //Can be any scope. None of the validation is scope dependent.
    }

    @Test
    public void shouldThrowExceptionWhenPrimaryMobileNumberIsMoreThan10Digits() {
        expectFieldValidationRuntimeException("field:primary_mobile:Mobile number should have 10 digits");
        ProviderWebRequest providerWebRequest = new ProviderRequestBuilder().withProviderId("1a123").withDate("17/03/1990 17:03:56").withDistrict(VALID_DISTRICT).withPrimaryMobile("12345678901").build();
        validator.validate(providerWebRequest, UpdateScope.createScope); //Can be any scope. None of the validation is scope dependent.
    }

    @Test
    public void shouldThrowExceptionWhenPrimaryMobileNumberIsNotNumeric() {
        expectFieldValidationRuntimeException("field:primary_mobile:Mobile number should have 10 digits");
        ProviderWebRequest providerWebRequest = new ProviderRequestBuilder().withProviderId("1a123").withDate("17/03/1990 17:03:56").withDistrict(VALID_DISTRICT).withPrimaryMobile("123456789a").build();
        validator.validate(providerWebRequest, UpdateScope.createScope); //Can be any scope. None of the validation is scope dependent.
    }

    @Test
    public void shouldNotThrowExceptionWhenPrimaryMobileNumberIs10Digits() {
        ProviderWebRequest providerWebRequest = new ProviderRequestBuilder().withProviderId("1a123").withDate("17/03/1990 17:03:56").withDistrict(VALID_DISTRICT).withPrimaryMobile("1234567890").build();
        validator.validate(providerWebRequest, UpdateScope.createScope); //Can be any scope. None of the validation is scope dependent.
    }
}
