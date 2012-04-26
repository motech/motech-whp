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
import org.motechproject.whp.request.PatientRequest;
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
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults().build();
        String defaultProviderId = patientRequest.getProvider_id();
        Provider defaultProvider = new Provider(defaultProviderId, "1234567890", "chambal", DateUtil.now());
        allProviders.add(defaultProvider);
    }

    @Test
    public void shouldNotThrowException_WhenCaseIdIs10Characters() {
        PatientRequest request = new PatientRequestBuilder().withDefaults().withCaseId("1234567890").build();
        validator.validate(request, ValidationScope.create, "patient");
    }

    @Test
    public void shouldNotThrowException_WhenCaseIdIs11Characters() {
        PatientRequest request = new PatientRequestBuilder().withDefaults().withCaseId("12345678901").build();
        validator.validate(request, ValidationScope.create, "patient");
    }

    @Test
    public void shouldThrowExceptionWhenGenderNotEnumerated() {
        expectException("field:gender:The value should be one of : [M, F, O]");
        allProviders.add(new Provider("12345", "1234567890", "chambal", DateUtil.now()));
        PatientRequest request = new PatientRequestBuilder().withDefaults().withProviderId("12345").withGender("H").build();
        validator.validate(request, ValidationScope.create, "patient");
    }

    @Test
    public void shouldThrowExceptionWhenGenderIsMale() {
        allProviders.add(new Provider("12345", "1234567890", "chambal", DateUtil.now()));
        PatientRequest request = new PatientRequestBuilder().withDefaults().withProviderId("12345").withGender(Gender.M.getValue()).build();
        validator.validate(request, ValidationScope.create, "patient");
    }

    @Test
    public void shouldThrowExceptionWhenGenderIsFemale() {
        allProviders.add(new Provider("12345", "1234567890", "chambal", DateUtil.now()));
        PatientRequest request = new PatientRequestBuilder().withDefaults().withProviderId("12345").withGender(Gender.F.getValue()).build();
        validator.validate(request, ValidationScope.create, "patient");
    }

    @Test
    public void shouldThrowExceptionWhenGenderIsOther() {
        allProviders.add(new Provider("12345", "1234567890", "chambal", DateUtil.now()));
        PatientRequest request = new PatientRequestBuilder().withDefaults().withProviderId("12345").withGender(Gender.O.getValue()).build();
        validator.validate(request, ValidationScope.create, "patient");
    }

    @Test
    public void shouldNotThrowException_WhenLastModifiedDateFormatIsCorrect() {
        PatientRequest request = new PatientRequestBuilder().withDefaults().withLastModifiedDate("03/04/2012 02:20:30").build();
        validator.validate(request, ValidationScope.create, "patient");
    }

    @Test
    public void shouldThrowException_WhenLastModifiedDateFormatIsNull() {
        expectException("field:date_modified:null");
        PatientRequest request = new PatientRequestBuilder().withDefaults().withLastModifiedDate(null).build();
        validator.validate(request, ValidationScope.create, "patient");
    }

    @Test
    public void shouldThrowException_WhenLastModifiedDateFormatIsNotTheCorrectDateTimeFormat() {
        expectException("03-04-2012\" is malformed at \"-04-2012");
        PatientRequest request = new PatientRequestBuilder().withDefaults().withLastModifiedDate("03-04-2012").build();
        validator.validate(request, ValidationScope.create, "patient");
    }

    @Test(expected = WHPException.class)
    public void shouldThrowException_WhenSmearTest1DateFormatIsIncorrect() {
        PatientRequest request = new PatientRequestBuilder().withDefaults().withSmearTestDate1("03/04/2012  11:23:40").build();
        validator.validate(request, ValidationScope.create, "patient");
    }

    @Test(expected = WHPException.class)
    public void shouldThrowException_WhenSmearTest2DateFormatIsIncorrect() {
        PatientRequest request = new PatientRequestBuilder().withDefaults().withSmearTestDate2("03/04/2012  11:23:40").build();
        validator.validate(request, ValidationScope.create, "patient");
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
        PatientRequest request = new PatientRequestBuilder().withDefaults().withTreatmentCategory(null).withLastModifiedDate("03/04/2012 02:20:30").build();
        validator.validate(request, ValidationScope.simpleUpdate, "patient");
    }

    @Test
    public void shouldThrowException_WhenTreatmentCategoryIsNotValid() {
        expectException("field:treatment_category:must match \"[0|1][1|2]\"");
        PatientRequest request = new PatientRequestBuilder().withDefaults().withTreatmentCategory("99").build();
        validator.validate(request, ValidationScope.create, "patient");
    }

    @Test
    public void shouldNotThrowException_WhenMobileNumberIsEmpty() {
        PatientRequest request = new PatientRequestBuilder().withDefaults().withMobileNumber("").build();
        validator.validate(request, ValidationScope.create, "patient");
    }

    @Test
    public void shouldNotThrowException_WhenMobileNumberIs10Digits() {
        PatientRequest request = new PatientRequestBuilder().withDefaults().withMobileNumber("1234567890").build();
        validator.validate(request, ValidationScope.create, "patient");
    }

    @Test
    public void shouldThrowException_WhenMobileNumberIsLessThan10Digits() {
        expectException("field:mobile_number:Mobile number should be empty or should have 10 digits");
        PatientRequest request = new PatientRequestBuilder().withDefaults().withMobileNumber("123456789").build();
        validator.validate(request, ValidationScope.create, "patient");
    }

    @Test
    public void shouldThrowException_WhenMobileNumberIsMoreThan10Digits() {
        expectException("field:mobile_number:Mobile number should be empty or should have 10 digits");
        PatientRequest request = new PatientRequestBuilder().withDefaults().withMobileNumber("12345678901").build();
        validator.validate(request, ValidationScope.create, "patient");
    }

    @Test
    public void shouldThrowException_WhenEnumFieldIsNull() {
        expectException("field:smear_test_result_1:The value should be one of : [Positive, Negative]");
        PatientRequest request = new PatientRequestBuilder().withDefaults().withSmearTestResult1(null).build();
        validator.validate(request, ValidationScope.create, "patient");
    }

    @Test
    public void shouldThrowException_WhenTbIdFieldIsNull() {
        expectException("field:tb_id:may not be null");
        PatientRequest request = new PatientRequestBuilder().withDefaults().withTBId(null).build();
        validator.validate(request, ValidationScope.create, "patient");
    }

    @Test
    public void shouldThrowExceptionWhenAgeIsNotNumeric() {
        expectException("field:age:Age must be numeric");
        PatientRequest request = new PatientRequestBuilder().withDefaults().withAge("A").build();
        validator.validate(request, ValidationScope.create, "patient");
    }

    @Test
    public void shouldThrowExceptionWhenAgeIsAFraction(){
        expectException("field:age:Age must be numeric");
        PatientRequest request = new PatientRequestBuilder().withDefaults().withAge("10.1").build();
        validator.validate(request, ValidationScope.create, "patient");
    }

    @Test
    public void shouldNotThrowExceptionWhenAgeIsNumeric(){
        PatientRequest request = new PatientRequestBuilder().withDefaults().withAge("10").build();
        validator.validate(request, ValidationScope.create, "patient");
    }

    private void expectException(String message) {
        exceptionThrown.expect(WHPException.class);
        exceptionThrown.expectMessage(new Contains(message));
    }

    private void validate(PatientRequest patientRequest) {
        validator.validate(patientRequest, ValidationScope.create, "patient");
    }

    @After
    public void tearDown() {
        markForDeletion(allProviders.getAll().toArray());
    }
}
