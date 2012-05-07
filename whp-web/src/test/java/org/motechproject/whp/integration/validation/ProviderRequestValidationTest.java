package org.motechproject.whp.integration.validation;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.internal.matchers.Contains;
import org.motechproject.whp.builder.PatientWebRequestBuilder;
import org.motechproject.whp.builder.ProviderRequestBuilder;
import org.motechproject.whp.patient.exception.WHPException;
import org.motechproject.whp.patient.repository.AllProviders;
import org.motechproject.whp.patient.repository.SpringIntegrationTest;
import org.motechproject.whp.request.PatientWebRequest;
import org.motechproject.whp.request.ProviderWebRequest;
import org.motechproject.whp.validation.RequestValidator;
import org.motechproject.whp.validation.ValidationScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

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
        expectWHPException("field:district:may not be empty");
        ProviderWebRequest providerWebRequest = new ProviderRequestBuilder().withProviderId("P00001").withDate("17/03/1990").withDistrict(null).withPrimaryMobile("9880000000").build();
        validator.validate(providerWebRequest, ValidationScope.create); //Can be any scope. None of the validation is scope dependent.
    }

    @Test
    public void shouldThrowAnExceptionIfDistrictIsEmpty() {
        expectWHPException("field:district:may not be empty");
        ProviderWebRequest providerWebRequest = new ProviderRequestBuilder().withProviderId("P00001").withDate("17/03/1990").withDistrict("").withPrimaryMobile("9880000000").build();
        validator.validate(providerWebRequest, ValidationScope.create); //Can be any scope. None of the validation is scope dependent.
    }

    @Test
    public void shouldThrowAnExceptionIfDateIsNull() {
        expectWHPException("field:date:may not be null");
        ProviderWebRequest providerWebRequest = new ProviderRequestBuilder().withProviderId("P00001").withDate(null).withDistrict("Chambal").withPrimaryMobile("9880000000").build();
        validator.validate(providerWebRequest, ValidationScope.create); //Can be any scope. None of the validation is scope dependent.
    }

    @Test
    public void shouldThrowSingleExceptionIfDateIsEmpty() {
        String errorMessage = "";
        try{
            ProviderWebRequest providerWebRequest = new ProviderRequestBuilder().withProviderId("P00001").withDate("").withDistrict("Chambal").withPrimaryMobile("9880000000").build();
            validator.validate(providerWebRequest, ValidationScope.create);
        } catch (WHPException e){
            if(e.getMessage().contains("field:date: may not be empty")){
                fail("Use @NotNull instead of @NotEmpty to validate null condition. @DateTimeFormat already validates empty date field.");
            }
            errorMessage = e.getMessage();
        }
        assertTrue(errorMessage.contains("field:date:Invalid format: \"\""));
    }

    @Test
    public void shouldThrowExceptionWhenDateFormatIsNotTheCorrectDateFormat() {
        expectWHPException("field:date:Invalid format: \"17-03-1990 17:03:56\" is malformed at \"-03-1990 17:03:56\"");
        ProviderWebRequest providerWebRequest = new ProviderRequestBuilder().withProviderId("P00001").withDate("17-03-1990 17:03:56").withDistrict("Chambal").withPrimaryMobile("9880000000").build();
        validator.validate(providerWebRequest, ValidationScope.create); //Can be any scope. None of the validation is scope dependent.
    }

    @Test
    public void shouldThrowExceptionWhenDateFormatDoesNotHaveTimeComponent() {
        expectWHPException("field:date:Invalid format: \"17/03/1990\" is too short");
        ProviderWebRequest providerWebRequest = new ProviderRequestBuilder().withProviderId("P00001").withDate("17/03/1990").withDistrict("Chambal").withPrimaryMobile("9880000000").build();
        validator.validate(providerWebRequest, ValidationScope.create); //Can be any scope. None of the validation is scope dependent.
    }

    @Test
    public void shouldThrowAnExceptionIfProviderIdIsNull() {
        expectWHPException("field:provider_id:may not be empty");
        ProviderWebRequest providerWebRequest = new ProviderRequestBuilder().withProviderId(null).withDate("17/03/1990 17:03:56").withDistrict("Chambal").withPrimaryMobile("9880000000").build();
        validator.validate(providerWebRequest, ValidationScope.create); //Can be any scope. None of the validation is scope dependent.
    }

    @Test
    public void shouldThrowAnExceptionIfProviderIdIsEmpty() {
        expectWHPException("field:provider_id:may not be empty");
        ProviderWebRequest providerWebRequest = new ProviderRequestBuilder().withProviderId("").withDate("17/03/1990 17:03:56").withDistrict("Chambal").withPrimaryMobile("9880000000").build();
        validator.validate(providerWebRequest, ValidationScope.create); //Can be any scope. None of the validation is scope dependent.
    }

    @Test
    public void shouldThrowExceptionWhenPrimaryMobileNumberIsEmpty() {
        expectWHPException("field:primary_mobile:may not be empty");
        ProviderWebRequest providerWebRequest = new ProviderRequestBuilder().withProviderId("1a123").withDate("17/03/1990 17:03:56").withDistrict("Chambal").withPrimaryMobile("").build();
        validator.validate(providerWebRequest, ValidationScope.create); //Can be any scope. None of the validation is scope dependent.
    }

    @Test
    public void shouldThrowExceptionWhenPrimaryMobileNumberIsNull() {
        expectWHPException("field:primary_mobile:may not be empty");
        ProviderWebRequest providerWebRequest = new ProviderRequestBuilder().withProviderId("1a123").withDate("17/03/1990 17:03:56").withDistrict("Chambal").withPrimaryMobile(null).build();
        validator.validate(providerWebRequest, ValidationScope.create); //Can be any scope. None of the validation is scope dependent.
    }

    @Test
    public void shouldThrowExceptionWhenPrimaryMobileNumberIsLessThan10Digits() {
        expectWHPException("field:primary_mobile:Mobile number should have 10 digits");
        ProviderWebRequest providerWebRequest = new ProviderRequestBuilder().withProviderId("1a123").withDate("17/03/1990 17:03:56").withDistrict("Chambal").withPrimaryMobile("1234").build();
        validator.validate(providerWebRequest, ValidationScope.create); //Can be any scope. None of the validation is scope dependent.
    }

    @Test
    public void shouldThrowExceptionWhenPrimaryMobileNumberIsMoreThan10Digits() {
        expectWHPException("field:primary_mobile:Mobile number should have 10 digits");
        ProviderWebRequest providerWebRequest = new ProviderRequestBuilder().withProviderId("1a123").withDate("17/03/1990 17:03:56").withDistrict("Chambal").withPrimaryMobile("12345678901").build();
        validator.validate(providerWebRequest, ValidationScope.create); //Can be any scope. None of the validation is scope dependent.
    }

    @Test
    public void shouldThrowExceptionWhenPrimaryMobileNumberIsNotNumeric() {
        expectWHPException("field:primary_mobile:Mobile number should have 10 digits");
        ProviderWebRequest providerWebRequest = new ProviderRequestBuilder().withProviderId("1a123").withDate("17/03/1990 17:03:56").withDistrict("Chambal").withPrimaryMobile("123456789a").build();
        validator.validate(providerWebRequest, ValidationScope.create); //Can be any scope. None of the validation is scope dependent.
    }

    @Test
    public void shouldNotThrowExceptionWhenPrimaryMobileNumberIs10Digits() {
        ProviderWebRequest providerWebRequest = new ProviderRequestBuilder().withProviderId("1a123").withDate("17/03/1990 17:03:56").withDistrict("Chambal").withPrimaryMobile("1234567890").build();
        validator.validate(providerWebRequest, ValidationScope.create); //Can be any scope. None of the validation is scope dependent.
    }

    @Test
    public void shouldNotThrowExceptionWhenSecondaryMobileNumberIsEmpty() {
        ProviderWebRequest providerWebRequest = new ProviderRequestBuilder().withProviderId("1a123").withDate("17/03/1990 17:03:56").withDistrict("Chambal").withSecondaryMobile("").build();
        validator.validate(providerWebRequest, ValidationScope.create); //Can be any scope. None of the validation is scope dependent.
    }

    @Test
    public void shouldNotThrowExceptionWhenSecondaryMobileNumberIsNull() {
        ProviderWebRequest providerWebRequest = new ProviderRequestBuilder().withProviderId("1a123").withDate("17/03/1990 17:03:56").withDistrict("Chambal").withSecondaryMobile(null).build();
        validator.validate(providerWebRequest, ValidationScope.create); //Can be any scope. None of the validation is scope dependent.
    }


    @Test
    public void shouldThrowExceptionWhenSecondaryMobileNumberIsLessThan10Digits() {
        expectWHPException("field:secondary_mobile:Secondary mobile number should be empty or should have 10 digits");
        ProviderWebRequest providerWebRequest = new ProviderRequestBuilder().withProviderId("1a123").withDate("17/03/1990 17:03:56").withDistrict("Chambal").withSecondaryMobile("1234").build();
        validator.validate(providerWebRequest, ValidationScope.create); //Can be any scope. None of the validation is scope dependent.
    }

    @Test
    public void shouldThrowExceptionWhenSecondaryMobileNumberIsMoreThan10Digits() {
        expectWHPException("field:secondary_mobile:Secondary mobile number should be empty or should have 10 digits");
        ProviderWebRequest providerWebRequest = new ProviderRequestBuilder().withProviderId("1a123").withDate("17/03/1990 17:03:56").withDistrict("Chambal").withSecondaryMobile("12345678901").build();
        validator.validate(providerWebRequest, ValidationScope.create); //Can be any scope. None of the validation is scope dependent.
    }

    @Test
    public void shouldThrowExceptionWhenSecondaryMobileNumberIsNotNumeric() {
        expectWHPException("field:secondary_mobile:Secondary mobile number should be empty or should have 10 digits");
        ProviderWebRequest providerWebRequest = new ProviderRequestBuilder().withProviderId("1a123").withDate("17/03/1990 17:03:56").withDistrict("Chambal").withSecondaryMobile("123456789a").build();
        validator.validate(providerWebRequest, ValidationScope.create); //Can be any scope. None of the validation is scope dependent.
    }

    @Test
    public void shouldNotThrowExceptionWhenSecondaryMobileNumberIs10Digits() {
        ProviderWebRequest providerWebRequest = new ProviderRequestBuilder().withProviderId("1a123").withDate("17/03/1990 17:03:56").withDistrict("Chambal").withSecondaryMobile("1234567890").build();
        validator.validate(providerWebRequest, ValidationScope.create); //Can be any scope. None of the validation is scope dependent.
    }


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

    private void expectWHPException(String message) {
        exceptionThrown.expect(WHPException.class);
        exceptionThrown.expectMessage(new Contains(message));
    }

    @After
    public void tearDown() {
        markForDeletion(allProviders.getAll().toArray());
    }
}
