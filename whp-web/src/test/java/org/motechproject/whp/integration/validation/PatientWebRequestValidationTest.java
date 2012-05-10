package org.motechproject.whp.integration.validation;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.internal.matchers.Contains;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.builder.PatientWebRequestBuilder;
import org.motechproject.whp.refdata.domain.TreatmentOutcome;
import org.motechproject.whp.refdata.domain.Gender;
import org.motechproject.whp.patient.domain.Provider;
import org.motechproject.whp.patient.exception.WHPException;
import org.motechproject.whp.patient.repository.AllProviders;
import org.motechproject.whp.patient.repository.SpringIntegrationTest;
import org.motechproject.whp.request.PatientWebRequest;
import org.motechproject.whp.validation.RequestValidator;
import org.motechproject.whp.validation.ValidationScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

@ContextConfiguration(locations = "classpath*:META-INF/spring/applicationContext.xml")
public class PatientWebRequestValidationTest extends SpringIntegrationTest {

    @Rule
    public ExpectedException exceptionThrown = ExpectedException.none();

    @Autowired
    private RequestValidator validator;

    @Autowired
    AllProviders allProviders;

    @Before
    public void setUpDefaultProvider() {
        PatientWebRequest patientWebRequest = new PatientWebRequestBuilder().withDefaults().build();
        String defaultProviderId = patientWebRequest.getProvider_id();
        Provider defaultProvider = new Provider(defaultProviderId, "1234567890", "chambal", DateUtil.now());
        allProviders.add(defaultProvider);
    }

    @After
    public void tearDown() {
        markForDeletion(allProviders.getAll().toArray());
    }

    @Test
    public void shouldNotThrowException_WhenCaseIdIs10Characters() {
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withCaseId("1234567890").build();
        validator.validate(webRequest, ValidationScope.create);
    }

    @Test
    public void shouldNotThrowException_WhenCaseIdIs11Characters() {
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withCaseId("12345678901").build();
        validator.validate(webRequest, ValidationScope.create);
    }

    @Test
    public void shouldThrowExceptionWhenGenderNotEnumerated() {
        expectWHPException("field:gender:The value should be one of : [M, F, O]");
        allProviders.add(new Provider("12345", "1234567890", "chambal", DateUtil.now()));
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withProviderId("12345").withGender("H").build();
        validator.validate(webRequest, ValidationScope.create);
    }

    @Test
    public void shouldNotThrowExceptionWhenGenderIsMale() {
        allProviders.add(new Provider("12345", "1234567890", "chambal", DateUtil.now()));
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withProviderId("12345").withGender(Gender.M.getValue()).build();
        validator.validate(webRequest, ValidationScope.create);
    }

    @Test
    public void shouldNotThrowExceptionWhenGenderIsFemale() {
        allProviders.add(new Provider("12345", "1234567890", "chambal", DateUtil.now()));
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withProviderId("12345").withGender(Gender.F.getValue()).build();
        validator.validate(webRequest, ValidationScope.create);
    }

    @Test
    public void shouldNotThrowExceptionWhenGenderIsOther() {
        allProviders.add(new Provider("12345", "1234567890", "chambal", DateUtil.now()));
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withProviderId("12345").withGender(Gender.O.getValue()).build();
        validator.validate(webRequest, ValidationScope.create);
    }

    @Test
    public void shouldNotThrowException_WhenLastModifiedDateFormatIsCorrect() {
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withLastModifiedDate("03/04/2012 02:20:30").build();
        validator.validate(webRequest, ValidationScope.create);
    }

    @Test
    public void shouldThrowException_WhenLastModifiedDateFormatIsNotTheCorrectDateTimeFormat() {
        expectWHPException("03-04-2012\" is malformed at \"-04-2012");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withLastModifiedDate("03-04-2012").build();
        validator.validate(webRequest, ValidationScope.create);
    }

    @Test
    public void shouldThrowException_WhenLastModifiedDateFormatDoesNotHaveTimeComponent() {
        expectWHPException("field:date_modified:Invalid format: \"03/04/2012\" is too short");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withLastModifiedDate("03/04/2012").build();
        validator.validate(webRequest, ValidationScope.create);
    }

    @Test
    public void shouldThrowAnExceptionIfDateIsEmpty() {
        expectWHPException("field:date_modified:Invalid format: \"\"");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withLastModifiedDate("").build();
        validator.validate(webRequest, ValidationScope.create);
    }

    @Test(expected = WHPException.class)
    public void shouldThrowException_WhenSmearTest1DateFormatIsIncorrect() {
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withSmearTestDate1("03/04/2012  11:23:40").build();
        validator.validate(webRequest, ValidationScope.create);
    }

    @Test(expected = WHPException.class)
    public void shouldThrowException_WhenSmearTest2DateFormatIsIncorrect() {
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withSmearTestDate2("03/04/2012  11:23:40").build();
        validator.validate(webRequest, ValidationScope.create);
    }

    @Test
    public void shouldThrowExceptionWhenTreatmentCategoryIsValid() {
        validate(new PatientWebRequestBuilder().withDefaults().withTreatmentCategory("01").build());
        validate(new PatientWebRequestBuilder().withDefaults().withTreatmentCategory("02").build());
        validate(new PatientWebRequestBuilder().withDefaults().withTreatmentCategory("11").build());
        validate(new PatientWebRequestBuilder().withDefaults().withTreatmentCategory("12").build());
    }

    @Test
    public void shouldNotValidateTreatmentCategoryOnSimpleUpdate() {
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withTreatmentCategory(null).withLastModifiedDate("03/04/2012 02:20:30").build();
        validator.validate(webRequest, ValidationScope.simpleUpdate);
    }

    @Test
    public void shouldThrowException_WhenTreatmentCategoryIsNotValid() {
        expectWHPException("field:treatment_category:must match \"[0|1][1|2]\"");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withTreatmentCategory("99").build();
        validator.validate(webRequest, ValidationScope.create);
    }

    @Test
    public void shouldNotThrowException_WhenMobileNumberIsEmpty() {
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withMobileNumber("").build();
        validator.validate(webRequest, ValidationScope.create);
    }

    @Test
    public void shouldNotThrowException_WhenMobileNumberIs10Digits() {
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withMobileNumber("1234567890").build();
        validator.validate(webRequest, ValidationScope.create);
    }

    @Test
    public void shouldThrowException_WhenMobileNumberIsLessThan10Digits() {
        expectWHPException("field:mobile_number:Mobile number should be empty or should have 10 digits");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withMobileNumber("123456789").build();
        validator.validate(webRequest, ValidationScope.create);
    }

    @Test
    public void shouldThrowException_WhenMobileNumberIsMoreThan10Digits() {
        expectWHPException("field:mobile_number:Mobile number should be empty or should have 10 digits");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withMobileNumber("12345678901").build();
        validator.validate(webRequest, ValidationScope.create);
    }

    @Test
    public void shouldThrowException_WhenMobileNumberIsNotNumeric() {
        expectWHPException("field:mobile_number:Mobile number should be empty or should have 10 digits");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withMobileNumber("123456789a").build();
        validator.validate(webRequest, ValidationScope.create);
    }

    @Test
    public void shouldThrowException_WhenTbIdFieldIsNotElevenDigits_InCreateScope() {
        expectWHPException("field:tb_id:size must be between 11 and 11");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withTBId("").build();
        validator.validate(webRequest, ValidationScope.create);
    }

    @Test
    public void shouldThrowException_WhenTbIdFieldIsNotElevenDigits_InSimpleUpdateScope() {
        expectWHPException("field:tb_id:size must be between 11 and 11");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withTBId("").build();
        validator.validate(webRequest, ValidationScope.simpleUpdate);
    }

    @Test
    public void shouldThrowExceptionWhenAgeIsNotNumeric() {
        expectWHPException("field:age:Age must be numeric and not fractional");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withAge("A").build();
        validator.validate(webRequest, ValidationScope.create);
    }

    @Test
    public void shouldThrowExceptionWhenAgeIsAFractionInCreateScope() {
        expectWHPException("field:age:Age must be numeric and not fractional");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withAge("10.1").build();
        validator.validate(webRequest, ValidationScope.create);
    }

    @Test
    public void shouldThrowExceptionWhenAgeIsAFractionInSimpleUpdateScope() {
        expectWHPException("field:age:Age must be numeric and not fractional");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withAge("10.1").build();
        validator.validate(webRequest, ValidationScope.simpleUpdate);
    }

    @Test
    public void shouldNotThrowExceptionWhenAgeIsNumericInCreateScope() {
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withAge("10").build();
        validator.validate(webRequest, ValidationScope.create);
    }

    @Test
    public void shouldNotThrowExceptionWhenAgeIsNumericInSimpleUpdateScope() {
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withAge("10").build();
        validator.validate(webRequest, ValidationScope.simpleUpdate);
    }

    @Test
    public void shouldThrowExceptionWhenProviderIdIsNotFound() {
        expectWHPException("No provider is found with id:providerId");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withProviderId("providerId").build();
        validator.validate(webRequest, ValidationScope.create);
    }

    @Test
    public void shouldThrowSingleExceptionWhenProviderIdIsNull() {
        String errorMessage = "";
        try {
            PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withProviderId(null).build();
            validator.validate(webRequest, ValidationScope.create);
        } catch (WHPException e) {
            if (e.getMessage().contains("field:provider_id:may not be null")) {
                fail("Not Null validation is not required.");
            }
            errorMessage = e.getMessage();
        }
        assertTrue(errorMessage.contains("field:provider_id:Provider Id cannot be null"));
    }

    @Test
    public void shouldThrowSingleException_WhenTreatmentCategoryIsNull() {
        String errorMessage = "";
        try {
            PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withTreatmentCategory(null).build();
            validator.validate(webRequest, ValidationScope.create);
        } catch (WHPException e) {
            if (e.getMessage().contains("field:treatment_category:Treatment Category cannot be null")) {
                fail("Not Null validation is not required. Validator implements null validation.");
            }
            errorMessage = e.getMessage();
        }
        assertTrue(errorMessage.contains("field:treatment_category:value should not be null"));
    }

    @Test
    public void shouldNotThrowException_WhenMobileNumberIsNull() {
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withMobileNumber(null).build();
        validator.validate(webRequest, ValidationScope.create);
    }

    //Any enum field
    @Test
    public void shouldThrowExceptionWhenGenderIsNull() {
        expectWHPException("field:gender:value should not be null");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withGender(null).build();
        validator.validate(webRequest, ValidationScope.create);
    }

    @Test
    public void shouldThrowExceptionWhenLastModifiedDateFormatIsNull() {
        expectWHPException("field:date_modified:value should not be null");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withLastModifiedDate(null).build();
        validator.validate(webRequest, ValidationScope.create);
    }

    @Test
    public void shouldThrowSingleExceptionWhenWeightIsNull() {
        String errorMessage = "";
        try {
            PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withWeight(null).build();
            validator.validate(webRequest, ValidationScope.create);
        } catch (WHPException e) {
            if (e.getMessage().contains("field:weight:Weight cannot be null")) {
                fail("Not Null validation is not required. Validator implements null validation.");
            }
            errorMessage = e.getMessage();
        }
        assertTrue(errorMessage.contains("field:weight:value should not be null"));
    }

    @Test
    public void shouldThrowException_WhenTbIdFieldIsNull() {
        try {
            PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withTBId(null).build();
            validator.validate(webRequest, ValidationScope.create);
        } catch (WHPException e) {
            if (e.getMessage().contains("field:tb_id:TB ID cannot be null")) {
                fail("Not Null validation is not required. Validator implements null validation.");
            }
            assertTrue(e.getMessage().contains("field:tb_id:value should not be null"));
        }
    }


    @Test
    public void shouldThrowExceptionWhenAgeIsNull() {
        String errorMessage = "";
        try {
            PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withAge(null).build();
            validator.validate(webRequest, ValidationScope.create);
        } catch (WHPException e) {
            if (e.getMessage().contains("field:age:Age cannot be null")) {
                fail("Not Null validation is not required. Validator implements null validation.");
            }
            errorMessage = e.getMessage();
        }
        assertTrue(errorMessage.contains("field:age:value should not be null"));
    }

    @Test
    public void shouldNotThrowExceptionWhenProviderIdIsFound() {
        Provider defaultProvider = new Provider("providerId", "1231231231", "chambal", DateUtil.now());
        allProviders.add(defaultProvider);
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withProviderId("providerId").build();
        validator.validate(webRequest, ValidationScope.create);
    }

    @Test
    public void shouldThrowExceptionWhenWeightStartWithANumberWithAndHasAnAlphabet() {
        expectWHPException("field:weight:Weight must be a real number");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withWeight("1A").build();
        validator.validate(webRequest, ValidationScope.create);
    }

    @Test
    public void shouldBeValidWhenAPIKeyIsValid() {
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withWeight("20").withAPIKey("3F2504E04F8911D39A0C0305E82C3301").build();
        validator.validate(webRequest, ValidationScope.create);
    }

    @Test
    public void shouldBeInvalidWhenAPIIsInvalid() {
        expectWHPException("field:api_key:api_key:is invalid.");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withWeight("20").withAPIKey("invalid_api_key").build();
        validator.validate(webRequest, ValidationScope.create);
    }

    @Test
    public void shouldThrowExceptionIfTreatmentOutcomeIsNull() {
        expectWHPException("field:treatment_outcome:value should not be null");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withOnlyRequiredTreatmentUpdateFields().withTreatmentOutcome(null).build();
        validator.validate(webRequest, ValidationScope.closeTreatment);
    }

    @Test
    public void shouldThrowExceptionIfTreatmentOutcomeIsEmpty() {
        expectWHPException("field:treatment_outcome:The value should be one of : [Cured, Died, Failure, Defaulted, TransferredOut, SwitchedOverToMDRTBTreatment, TreatmentCompleted]");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withOnlyRequiredTreatmentUpdateFields().withTreatmentOutcome("").build();
        validator.validate(webRequest, ValidationScope.closeTreatment);
    }

    @Test
    public void shouldThrowExceptionIfTreatmentOutcomeIsAnInvalidReason() {
        expectWHPException("field:treatment_outcome:The value should be one of : [Cured, Died, Failure, Defaulted, TransferredOut, SwitchedOverToMDRTBTreatment, TreatmentCompleted]");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withOnlyRequiredTreatmentUpdateFields().withTreatmentOutcome("PatientGotBored").build();
        validator.validate(webRequest, ValidationScope.closeTreatment);
    }

    @Test
    public void shouldNotThrowExceptionIfTreatmentOutcomeIsValid() {
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withOnlyRequiredTreatmentUpdateFields().withTreatmentOutcome(TreatmentOutcome.Cured.name()).build();
        validator.validate(webRequest, ValidationScope.closeTreatment);
    }

    @Test
    public void shouldNotThrowExceptionWhenWeightIsAFraction() {
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withWeight("10.1").build();
        validator.validate(webRequest, ValidationScope.create);
    }

    @Test
    public void shouldNotThrowExceptionWhenWeightIsAnInteger() {
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withWeight("10").build();
        validator.validate(webRequest, ValidationScope.create);
    }

    @Test
    public void shouldNotThrowExceptionWhenWeightIsAFractionWithZeroInFractionPart() {
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withWeight("10.0").build();
        validator.validate(webRequest, ValidationScope.create);
    }

    @Test
    public void shouldNotThrowException_WhenWeightInstanceIsNull_ForUpdateScope() {
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withWeightStatistics(null, null).build();
        validator.validate(webRequest, ValidationScope.simpleUpdate);
    }

    @Test
    public void shouldThrowExceptionWhenWeightIsNotNumeric() {
        expectWHPException("field:weight:Weight must be a real number");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withWeight("A").build();
        validator.validate(webRequest, ValidationScope.create);
    }

    @Test
    public void shouldThrowExceptionWhenWeightStringIsJustSpaces_ForCreateScope() {
        expectWHPException("field:weight:Weight must be a real number");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withWeight(" ").build();
        validator.validate(webRequest, ValidationScope.create);
    }

    @Test
    public void shouldThrowExceptionWhenWeightStringIsNotValid_ForCreateScope() {
        expectWHPException("field:weight:Weight must be a real number");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withWeight("3 5").build();
        validator.validate(webRequest, ValidationScope.create);
    }

    @Test
    public void shouldNotThrowException_WhenSmearTestResultsIsNull_ForUpdateScope() {
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withSimpleUpdateFields().withSmearTestResults(null, null, null, null, null).build();
        validator.validate(webRequest, ValidationScope.simpleUpdate);
    }

    private void expectWHPException(String message) {
        exceptionThrown.expect(WHPException.class);
        exceptionThrown.expectMessage(new Contains(message));
    }

    private void validate(PatientWebRequest patientWebRequest) {
        validator.validate(patientWebRequest, ValidationScope.create);
    }

}
