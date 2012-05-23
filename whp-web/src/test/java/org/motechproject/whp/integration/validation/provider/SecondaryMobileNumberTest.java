package org.motechproject.whp.integration.validation.provider;

import org.junit.Test;
import org.motechproject.whp.builder.ProviderRequestBuilder;
import org.motechproject.whp.patient.command.AllCommands;
import org.motechproject.whp.request.ProviderWebRequest;

public class SecondaryMobileNumberTest extends BaseProviderTest {
    @Test
    public void shouldNotThrowExceptionWhenSecondaryMobileNumberIsEmpty() {
        ProviderWebRequest providerWebRequest = new ProviderRequestBuilder().withProviderId("1a123").withDate("17/03/1990 17:03:56").withDistrict("Chambal").withSecondaryMobile("").build();
        validator.validate(providerWebRequest, AllCommands.create); //Can be any scope. None of the validation is scope dependent.
    }

    @Test
    public void shouldNotThrowExceptionWhenSecondaryMobileNumberIsNull() {
        ProviderWebRequest providerWebRequest = new ProviderRequestBuilder().withProviderId("1a123").withDate("17/03/1990 17:03:56").withDistrict("Chambal").withSecondaryMobile(null).build();
        validator.validate(providerWebRequest, AllCommands.create); //Can be any scope. None of the validation is scope dependent.
    }

    @Test
    public void shouldThrowExceptionWhenSecondaryMobileNumberIsLessThan10Digits() {
        expectFieldValidationRuntimeException("field:secondary_mobile:Secondary mobile number should be empty or should have 10 digits");
        ProviderWebRequest providerWebRequest = new ProviderRequestBuilder().withProviderId("1a123").withDate("17/03/1990 17:03:56").withDistrict("Chambal").withSecondaryMobile("1234").build();
        validator.validate(providerWebRequest, AllCommands.create); //Can be any scope. None of the validation is scope dependent.
    }

    @Test
    public void shouldThrowExceptionWhenSecondaryMobileNumberIsMoreThan10Digits() {
        expectFieldValidationRuntimeException("field:secondary_mobile:Secondary mobile number should be empty or should have 10 digits");
        ProviderWebRequest providerWebRequest = new ProviderRequestBuilder().withProviderId("1a123").withDate("17/03/1990 17:03:56").withDistrict("Chambal").withSecondaryMobile("12345678901").build();
        validator.validate(providerWebRequest, AllCommands.create); //Can be any scope. None of the validation is scope dependent.
    }

    @Test
    public void shouldThrowExceptionWhenSecondaryMobileNumberIsNotNumeric() {
        expectFieldValidationRuntimeException("field:secondary_mobile:Secondary mobile number should be empty or should have 10 digits");
        ProviderWebRequest providerWebRequest = new ProviderRequestBuilder().withProviderId("1a123").withDate("17/03/1990 17:03:56").withDistrict("Chambal").withSecondaryMobile("123456789a").build();
        validator.validate(providerWebRequest, AllCommands.create); //Can be any scope. None of the validation is scope dependent.
    }

    @Test
    public void shouldNotThrowExceptionWhenSecondaryMobileNumberIs10Digits() {
        ProviderWebRequest providerWebRequest = new ProviderRequestBuilder().withProviderId("1a123").withDate("17/03/1990 17:03:56").withDistrict("Chambal").withSecondaryMobile("1234567890").build();
        validator.validate(providerWebRequest, AllCommands.create); //Can be any scope. None of the validation is scope dependent.
    }
}
