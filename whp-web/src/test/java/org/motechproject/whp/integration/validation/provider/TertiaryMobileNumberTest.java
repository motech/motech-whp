package org.motechproject.whp.integration.validation.provider;

import org.junit.Test;
import org.motechproject.whp.builder.ProviderRequestBuilder;
import org.motechproject.whp.request.ProviderWebRequest;
import org.motechproject.whp.validation.ValidationScope;

public class TertiaryMobileNumberTest extends BaseProviderTest {

    @Test
    public void shouldNotThrowExceptionWhenTertiaryMobileNumberIsEmpty() {
        ProviderWebRequest providerWebRequest = new ProviderRequestBuilder().withProviderId("1a123").withDate("17/03/1990 17:03:56").withDistrict("Chambal").withTertiaryMobile("").build();
        validator.validate(providerWebRequest, ValidationScope.create); //Can be any scope. None of the validation is scope dependent.
    }

    @Test
    public void shouldNotThrowExceptionWhenTertiaryMobileNumberIsNull() {
        ProviderWebRequest providerWebRequest = new ProviderRequestBuilder().withProviderId("1a123").withDate("17/03/1990 17:03:56").withDistrict("Chambal").withTertiaryMobile(null).build();
        validator.validate(providerWebRequest, ValidationScope.create); //Can be any scope. None of the validation is scope dependent.
    }

    @Test
    public void shouldThrowExceptionWhenTertiaryMobileNumberIsLessThan10Digits() {
        expectWHPException("field:tertiary_mobile:Tertiary mobile number should be empty or should have 10 digits");
        ProviderWebRequest providerWebRequest = new ProviderRequestBuilder().withProviderId("1a123").withDate("17/03/1990 17:03:56").withDistrict("Chambal").withTertiaryMobile("1234").build();
        validator.validate(providerWebRequest, ValidationScope.create); //Can be any scope. None of the validation is scope dependent.
    }

    @Test
    public void shouldThrowExceptionWhenTertiaryMobileNumberIsMoreThan10Digits() {
        expectWHPException("field:tertiary_mobile:Tertiary mobile number should be empty or should have 10 digits");
        ProviderWebRequest providerWebRequest = new ProviderRequestBuilder().withProviderId("1a123").withDate("17/03/1990").withDistrict("Chambal").withTertiaryMobile("12345678901").build();
        validator.validate(providerWebRequest, ValidationScope.create); //Can be any scope. None of the validation is scope dependent.
    }

    @Test
    public void shouldThrowExceptionWhenTertiaryMobileNumberIsNotNumeric() {
        expectWHPException("field:tertiary_mobile:Tertiary mobile number should be empty or should have 10 digits");
        ProviderWebRequest providerWebRequest = new ProviderRequestBuilder().withProviderId("1a123").withDate("17/03/1990 17:03:56").withDistrict("Chambal").withTertiaryMobile("123456789a").build();
        validator.validate(providerWebRequest, ValidationScope.create); //Can be any scope. None of the validation is scope dependent.
    }

    @Test
    public void shouldNotThrowExceptionWhenTertiaryMobileNumberIs10Digits() {
        ProviderWebRequest providerWebRequest = new ProviderRequestBuilder().withProviderId("1a123").withDate("17/03/1990 17:03:56").withDistrict("Chambal").withTertiaryMobile("1234567890").build();
        validator.validate(providerWebRequest, ValidationScope.create); //Can be any scope. None of the validation is scope dependent.
    }

}
