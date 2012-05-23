package org.motechproject.whp.integration.validation.provider;

import org.junit.Test;
import org.motechproject.whp.builder.ProviderRequestBuilder;
import org.motechproject.whp.patient.command.AllCommands;
import org.motechproject.whp.request.ProviderWebRequest;

public class PrimaryMobileNumberTest extends BaseProviderTest {
    @Test
    public void shouldThrowExceptionWhenPrimaryMobileNumberIsEmpty() {
        expectFieldValidationRuntimeException("field:primary_mobile:may not be empty");
        ProviderWebRequest providerWebRequest = new ProviderRequestBuilder().withProviderId("1a123").withDate("17/03/1990 17:03:56").withDistrict("Chambal").withPrimaryMobile("").build();
        validator.validate(providerWebRequest, AllCommands.create); //Can be any scope. None of the validation is scope dependent.
    }

    @Test
    public void shouldThrowExceptionWhenPrimaryMobileNumberIsNull() {
        expectFieldValidationRuntimeException("field:primary_mobile:may not be empty");
        ProviderWebRequest providerWebRequest = new ProviderRequestBuilder().withProviderId("1a123").withDate("17/03/1990 17:03:56").withDistrict("Chambal").withPrimaryMobile(null).build();
        validator.validate(providerWebRequest, AllCommands.create); //Can be any scope. None of the validation is scope dependent.
    }

    @Test
    public void shouldThrowExceptionWhenPrimaryMobileNumberIsLessThan10Digits() {
        expectFieldValidationRuntimeException("field:primary_mobile:Mobile number should have 10 digits");
        ProviderWebRequest providerWebRequest = new ProviderRequestBuilder().withProviderId("1a123").withDate("17/03/1990 17:03:56").withDistrict("Chambal").withPrimaryMobile("1234").build();
        validator.validate(providerWebRequest, AllCommands.create); //Can be any scope. None of the validation is scope dependent.
    }

    @Test
    public void shouldThrowExceptionWhenPrimaryMobileNumberIsMoreThan10Digits() {
        expectFieldValidationRuntimeException("field:primary_mobile:Mobile number should have 10 digits");
        ProviderWebRequest providerWebRequest = new ProviderRequestBuilder().withProviderId("1a123").withDate("17/03/1990 17:03:56").withDistrict("Chambal").withPrimaryMobile("12345678901").build();
        validator.validate(providerWebRequest, AllCommands.create); //Can be any scope. None of the validation is scope dependent.
    }

    @Test
    public void shouldThrowExceptionWhenPrimaryMobileNumberIsNotNumeric() {
        expectFieldValidationRuntimeException("field:primary_mobile:Mobile number should have 10 digits");
        ProviderWebRequest providerWebRequest = new ProviderRequestBuilder().withProviderId("1a123").withDate("17/03/1990 17:03:56").withDistrict("Chambal").withPrimaryMobile("123456789a").build();
        validator.validate(providerWebRequest, AllCommands.create); //Can be any scope. None of the validation is scope dependent.
    }

    @Test
    public void shouldNotThrowExceptionWhenPrimaryMobileNumberIs10Digits() {
        ProviderWebRequest providerWebRequest = new ProviderRequestBuilder().withProviderId("1a123").withDate("17/03/1990 17:03:56").withDistrict("Chambal").withPrimaryMobile("1234567890").build();
        validator.validate(providerWebRequest, AllCommands.create); //Can be any scope. None of the validation is scope dependent.
    }
}
