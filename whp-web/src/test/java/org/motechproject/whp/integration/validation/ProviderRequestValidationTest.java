package org.motechproject.whp.integration.validation;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.internal.matchers.Contains;
import org.motechproject.whp.builder.ProviderRequestBuilder;
import org.motechproject.whp.patient.exception.WHPException;
import org.motechproject.whp.patient.repository.AllProviders;
import org.motechproject.whp.patient.repository.SpringIntegrationTest;
import org.motechproject.whp.request.ProviderWebRequest;
import org.motechproject.whp.validation.RequestValidator;
import org.motechproject.whp.validation.ValidationScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(locations = "classpath*:META-INF/spring/applicationContext.xml")
public class ProviderRequestValidationTest extends SpringIntegrationTest {

    @Rule
    public ExpectedException exceptionThrown = ExpectedException.none();

    @Autowired
    private RequestValidator validator;

    @Autowired
    AllProviders allProviders;

    @Test
    public void shouldThrowAnExceptionIfDistrictIsNull() {
        expectException("field:district:may not be empty");
        ProviderWebRequest providerWebRequest = new ProviderRequestBuilder().withProviderId("P00001").withDate("17/03/1990").withDistrict(null).withPrimaryMobile("9880000000").build();
        validator.validate(providerWebRequest, ValidationScope.create); //Can be any scope. None of the validation is scope dependent.
    }

    @Test
    public void shouldNotThrowAnExceptionIfDateIsNull() {
        ProviderWebRequest providerWebRequest = new ProviderRequestBuilder().withProviderId("P00001").withDate(null).withDistrict("Chambal").withPrimaryMobile("9880000000").build();
        validator.validate(providerWebRequest, ValidationScope.create); //Can be any scope. None of the validation is scope dependent.
    }

    @Test
    public void shouldThrowAnExceptionIfDistrictIsEmpty() {
        expectException("field:district:may not be empty");
        ProviderWebRequest providerWebRequest = new ProviderRequestBuilder().withProviderId("P00001").withDate("17/03/1990").withDistrict("").withPrimaryMobile("9880000000").build();
        validator.validate(providerWebRequest, ValidationScope.create); //Can be any scope. None of the validation is scope dependent.
    }

    @Test
    public void shouldThrowAnExceptionIfDateIsEmpty() {
        expectException("field:date:Invalid format: \"\"");
        ProviderWebRequest providerWebRequest = new ProviderRequestBuilder().withProviderId("P00001").withDate("").withDistrict("Chambal").withPrimaryMobile("9880000000").build();
        validator.validate(providerWebRequest, ValidationScope.create); //Can be any scope. None of the validation is scope dependent.
    }

    @Test
    public void shouldThrowExceptionWhenDateFormatIsNotTheCorrectDateFormat() {
        expectException("field:date:Invalid format: \"17-03-1990 17:03:56\" is malformed at \"-03-1990 17:03:56\"");
        ProviderWebRequest providerWebRequest = new ProviderRequestBuilder().withProviderId("P00001").withDate("17-03-1990 17:03:56").withDistrict("Chambal").withPrimaryMobile("9880000000").build();
        validator.validate(providerWebRequest, ValidationScope.create); //Can be any scope. None of the validation is scope dependent.
    }

    @Test
    public void shouldThrowExceptionWhenDateFormatDoesNotHaveTimeComponent() {
        expectException("field:date:Invalid format: \"17/03/1990\" is too short");
        ProviderWebRequest providerWebRequest = new ProviderRequestBuilder().withProviderId("P00001").withDate("17/03/1990").withDistrict("Chambal").withPrimaryMobile("9880000000").build();
        validator.validate(providerWebRequest, ValidationScope.create); //Can be any scope. None of the validation is scope dependent.
    }

    @Test
    public void shouldThrowAnExceptionIfProviderIdIsNull() {
        expectException("field:provider_id:may not be empty");
        ProviderWebRequest providerWebRequest = new ProviderRequestBuilder().withProviderId(null).withDate("17/03/1990").withDistrict("Chambal").withPrimaryMobile("9880000000").build();
        validator.validate(providerWebRequest, ValidationScope.create); //Can be any scope. None of the validation is scope dependent.
    }

    @Test
    public void shouldThrowAnExceptionIfProviderIdIsEmpty() {
        expectException("field:provider_id:may not be empty");
        ProviderWebRequest providerWebRequest = new ProviderRequestBuilder().withProviderId("").withDate("17/03/1990").withDistrict("Chambal").withPrimaryMobile("9880000000").build();
        validator.validate(providerWebRequest, ValidationScope.create); //Can be any scope. None of the validation is scope dependent.
    }

    @Test
    public void shouldThrowExceptionWhenMobileNumberIsEmpty() {
        expectException("field:primary_mobile:may not be empty");
        ProviderWebRequest providerWebRequest = new ProviderRequestBuilder().withProviderId("1a123").withDate("17/03/1990").withDistrict("Chambal").withPrimaryMobile("").build();
        validator.validate(providerWebRequest, ValidationScope.create); //Can be any scope. None of the validation is scope dependent.
    }

    @Test
    public void shouldThrowExceptionWhenMobileNumberIsLessThan10Digits() {
        expectException("field:primary_mobile:Mobile number should have 10 digits");
        ProviderWebRequest providerWebRequest = new ProviderRequestBuilder().withProviderId("1a123").withDate("17/03/1990").withDistrict("Chambal").withPrimaryMobile("1234").build();
        validator.validate(providerWebRequest, ValidationScope.create); //Can be any scope. None of the validation is scope dependent.
    }

    @Test
    public void shouldThrowException_WhenMobileNumberIsMoreThan10Digits() {
        expectException("field:primary_mobile:Mobile number should have 10 digits");
        ProviderWebRequest providerWebRequest = new ProviderRequestBuilder().withProviderId("1a123").withDate("17/03/1990").withDistrict("Chambal").withPrimaryMobile("12345678901").build();
        validator.validate(providerWebRequest, ValidationScope.create); //Can be any scope. None of the validation is scope dependent.
    }

    @Test
    public void shouldThrowException_WhenMobileNumberIsNotNumeric() {
        expectException("field:primary_mobile:Mobile number should have 10 digits");
        ProviderWebRequest providerWebRequest = new ProviderRequestBuilder().withProviderId("1a123").withDate("17/03/1990").withDistrict("Chambal").withPrimaryMobile("123456789a").build();
        validator.validate(providerWebRequest, ValidationScope.create); //Can be any scope. None of the validation is scope dependent.
    }

    @Test
    public void shouldNotThrowException_WhenMobileNumberIs10Digits() {
        ProviderWebRequest providerWebRequest = new ProviderRequestBuilder().withProviderId("1a123").withDate("17/03/1990 17:03:56").withDistrict("Chambal").withPrimaryMobile("1234567890").build();
        validator.validate(providerWebRequest, ValidationScope.create); //Can be any scope. None of the validation is scope dependent.
    }

    private void expectException(String message) {
        exceptionThrown.expect(WHPException.class);
        exceptionThrown.expectMessage(new Contains(message));
    }

    @After
    public void tearDown() {
        markForDeletion(allProviders.getAll().toArray());
    }
}
