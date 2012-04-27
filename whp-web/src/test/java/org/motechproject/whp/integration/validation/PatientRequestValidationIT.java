package org.motechproject.whp.integration.validation;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.internal.matchers.Contains;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.builder.PatientRequestBuilder;
import org.motechproject.whp.patient.domain.Gender;
import org.motechproject.whp.patient.domain.Provider;
import org.motechproject.whp.patient.exception.WHPException;
import org.motechproject.whp.patient.repository.AllProviders;
import org.motechproject.whp.patient.repository.SpringIntegrationTest;
import org.motechproject.whp.request.PatientWebRequest;
import org.motechproject.whp.validation.RequestValidator;
import org.motechproject.whp.validation.ValidationScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(locations = "classpath*:META-INF/spring/applicationContext.xml")
public class PatientRequestValidationIT extends SpringIntegrationTest {

    @Rule
    public ExpectedException exceptionThrown = ExpectedException.none();

    @Autowired
    private RequestValidator validator;

    @Autowired
    AllProviders allProviders;

    @Before
    public void setUpDefaultProvider() {
        PatientWebRequest patientWebRequest = new PatientRequestBuilder().withDefaults().build();
        String defaultProviderId = patientWebRequest.getProvider_id();
        Provider defaultProvider = new Provider(defaultProviderId, "1234567890", "chambal", DateUtil.now());
        allProviders.add(defaultProvider);
    }

    @Test
    public void shouldNotThrowException_WhenCaseIdIs10Characters() {
        PatientWebRequest webRequest = new PatientRequestBuilder().withDefaults().withCaseId("1234567890").build();
        validator.validate(webRequest, ValidationScope.create);
    }

    @Test
    public void shouldNotThrowException_WhenCaseIdIs11Characters() {
        PatientWebRequest webRequest = new PatientRequestBuilder().withDefaults().withCaseId("12345678901").build();
        validator.validate(webRequest, ValidationScope.create);
    }

    @Test
    public void shouldThrowExceptionWhenGenderNotEnumerated() {
        expectException("field:gender:The value should be one of : [M, F, O]");
        allProviders.add(new Provider("12345", "1234567890", "chambal", DateUtil.now()));
        PatientWebRequest webRequest = new PatientRequestBuilder().withDefaults().withProviderId("12345").withGender("H").build();
        validator.validate(webRequest, ValidationScope.create);
    }

    @Test
    public void shouldThrowExceptionWhenGenderIsMale() {
        allProviders.add(new Provider("12345", "1234567890", "chambal", DateUtil.now()));
        PatientWebRequest webRequest = new PatientRequestBuilder().withDefaults().withProviderId("12345").withGender(Gender.M.getValue()).build();
        validator.validate(webRequest, ValidationScope.create);
    }

    @Test
    public void shouldThrowExceptionWhenGenderIsFemale() {
        allProviders.add(new Provider("12345", "1234567890", "chambal", DateUtil.now()));
        PatientWebRequest webRequest = new PatientRequestBuilder().withDefaults().withProviderId("12345").withGender(Gender.F.getValue()).build();
        validator.validate(webRequest, ValidationScope.create);
    }

    @Test
    public void shouldThrowExceptionWhenGenderIsOther() {
        allProviders.add(new Provider("12345", "1234567890", "chambal", DateUtil.now()));
        PatientWebRequest webRequest = new PatientRequestBuilder().withDefaults().withProviderId("12345").withGender(Gender.O.getValue()).build();
        validator.validate(webRequest, ValidationScope.create);
    }

    @Test
    public void shouldNotThrowException_WhenLastModifiedDateFormatIsCorrect() {
        PatientWebRequest webRequest = new PatientRequestBuilder().withDefaults().withLastModifiedDate("03/04/2012 02:20:30").build();
        validator.validate(webRequest, ValidationScope.create);
    }

    @Test
    public void shouldThrowException_WhenLastModifiedDateFormatIsNull() {
        expectException("field:date_modified:null");
        PatientWebRequest webRequest = new PatientRequestBuilder().withDefaults().withLastModifiedDate(null).build();
        validator.validate(webRequest, ValidationScope.create);
    }

    @Test
    public void shouldThrowException_WhenLastModifiedDateFormatIsNotTheCorrectDateTimeFormat() {
        expectException("03-04-2012\" is malformed at \"-04-2012");
        PatientWebRequest webRequest = new PatientRequestBuilder().withDefaults().withLastModifiedDate("03-04-2012").build();
        validator.validate(webRequest, ValidationScope.create);
    }

    @Test(expected = WHPException.class)
    public void shouldThrowException_WhenSmearTest1DateFormatIsIncorrect() {
        PatientWebRequest webRequest = new PatientRequestBuilder().withDefaults().withSmearTestDate1("03/04/2012  11:23:40").build();
        validator.validate(webRequest, ValidationScope.create);
    }

    @Test(expected = WHPException.class)
    public void shouldThrowException_WhenSmearTest2DateFormatIsIncorrect() {
        PatientWebRequest webRequest = new PatientRequestBuilder().withDefaults().withSmearTestDate2("03/04/2012  11:23:40").build();
        validator.validate(webRequest, ValidationScope.create);
    }

    @Test
    public void shouldThrowExceptionWhenTreatmentCategoryIsValid() {
        validate(new PatientRequestBuilder().withDefaults().withTreatmentCategory("01").build());
        validate(new PatientRequestBuilder().withDefaults().withTreatmentCategory("02").build());
        validate(new PatientRequestBuilder().withDefaults().withTreatmentCategory("11").build());
        validate(new PatientRequestBuilder().withDefaults().withTreatmentCategory("12").build());
    }

    @Test
    public void shouldNotValidateTreatmentCategoryOnSimpleUpdate() {
        PatientWebRequest webRequest = new PatientRequestBuilder().withDefaults().withTreatmentCategory(null).withLastModifiedDate("03/04/2012 02:20:30").build();
        validator.validate(webRequest, ValidationScope.simpleUpdate);
    }

    @Test
    public void shouldThrowException_WhenTreatmentCategoryIsNotValid() {
        expectException("field:treatment_category:must match \"[0|1][1|2]\"");
        PatientWebRequest webRequest = new PatientRequestBuilder().withDefaults().withTreatmentCategory("99").build();
        validator.validate(webRequest, ValidationScope.create);
    }

    @Test
    public void shouldNotThrowException_WhenMobileNumberIsEmpty() {
        PatientWebRequest webRequest = new PatientRequestBuilder().withDefaults().withMobileNumber("").build();
        validator.validate(webRequest, ValidationScope.create);
    }

    @Test
    public void shouldNotThrowException_WhenMobileNumberIs10Digits() {
        PatientWebRequest webRequest = new PatientRequestBuilder().withDefaults().withMobileNumber("1234567890").build();
        validator.validate(webRequest, ValidationScope.create);
    }

    @Test
    public void shouldThrowException_WhenMobileNumberIsLessThan10Digits() {
        expectException("field:mobile_number:Mobile number should be empty or should have 10 digits");
        PatientWebRequest webRequest = new PatientRequestBuilder().withDefaults().withMobileNumber("123456789").build();
        validator.validate(webRequest, ValidationScope.create);
    }

    @Test
    public void shouldThrowException_WhenMobileNumberIsMoreThan10Digits() {
        expectException("field:mobile_number:Mobile number should be empty or should have 10 digits");
        PatientWebRequest webRequest = new PatientRequestBuilder().withDefaults().withMobileNumber("12345678901").build();
        validator.validate(webRequest, ValidationScope.create);
    }

    @Test
    public void shouldThrowException_WhenEnumFieldIsNull() {
        expectException("field:smear_test_result_1:The value should be one of : [Positive, Negative]");
        PatientWebRequest webRequest = new PatientRequestBuilder().withDefaults().withSmearTestResult1(null).build();
        validator.validate(webRequest, ValidationScope.create);
    }

    @Test
    public void shouldThrowException_WhenTbIdFieldIsNull() {
        expectException("field:tb_id:may not be null");
        PatientWebRequest webRequest = new PatientRequestBuilder().withDefaults().withTBId(null).build();
        validator.validate(webRequest, ValidationScope.create);
    }

    @Test
    public void shouldThrowExceptionWhenAgeIsNotNumeric() {
        expectException("field:age:Age must be numeric");
        PatientWebRequest webRequest = new PatientRequestBuilder().withDefaults().withAge("A").build();
        validator.validate(webRequest, ValidationScope.create);
    }

    @Test
    public void shouldThrowExceptionWhenAgeIsAFraction() {
        expectException("field:age:Age must be numeric");
        PatientWebRequest webRequest = new PatientRequestBuilder().withDefaults().withAge("10.1").build();
        validator.validate(webRequest, ValidationScope.create);
    }

    @Test
    public void shouldNotThrowExceptionWhenAgeIsNumeric() {
        PatientWebRequest webRequest = new PatientRequestBuilder().withDefaults().withAge("10").build();
        validator.validate(webRequest, ValidationScope.create);
    }

    @Test
    public void shouldThrowExceptionWhenWeightIsNotNumeric() {
        expectException("field:weight:Weight must be a real number");
        PatientWebRequest webRequest = new PatientRequestBuilder().withDefaults().withWeight("A").build();
        validator.validate(webRequest, ValidationScope.create);
    }

    @Test
    public void shouldThrowExceptionWhenWeightStartWithANumberWithAndHasAnAlphabet() {
        expectException("field:weight:Weight must be a real number");
        PatientWebRequest webRequest = new PatientRequestBuilder().withDefaults().withWeight("1A").build();
        validator.validate(webRequest, ValidationScope.create);
    }

    @Test
    public void shouldNotThrowExceptionWhenWeightIsAFraction() {
        PatientWebRequest webRequest = new PatientRequestBuilder().withDefaults().withWeight("10.1").build();
        validator.validate(webRequest, ValidationScope.create);
    }

    @Test
    public void shouldNotThrowExceptionWhenWeightIsAnInteger() {
        PatientWebRequest webRequest = new PatientRequestBuilder().withDefaults().withWeight("10").build();
        validator.validate(webRequest, ValidationScope.create);
    }

    @Test
    public void shouldNotThrowExceptionWhenWeightIsAFractionWithZeroInFractionPart() {
        PatientWebRequest webRequest = new PatientRequestBuilder().withDefaults().withWeight("10.0").build();
        validator.validate(webRequest, ValidationScope.create);
    }

    private void expectException(String message) {
        exceptionThrown.expect(WHPException.class);
        exceptionThrown.expectMessage(new Contains(message));
    }

    private void validate(PatientWebRequest patientWebRequest) {
        validator.validate(patientWebRequest, ValidationScope.create);
    }

    @After
    public void tearDown() {
        markForDeletion(allProviders.getAll().toArray());
    }
}
