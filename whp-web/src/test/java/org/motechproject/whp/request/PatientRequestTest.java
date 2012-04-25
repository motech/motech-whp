package org.motechproject.whp.request;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.internal.matchers.Contains;
import org.motechproject.whp.builder.PatientRequestBuilder;
import org.motechproject.whp.common.integration.repository.SpringIntegrationTest;
import org.motechproject.whp.exception.WHPValidationException;
import org.motechproject.whp.validation.validator.BeanValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(locations = "classpath*:META-INF/spring/applicationContext.xml")
public class PatientRequestTest extends SpringIntegrationTest {

    @Rule
    public ExpectedException exceptionThrown = ExpectedException.none();

    @Autowired
    private BeanValidator validator;

    @Test
    public void shouldNotThrowException_WhenCaseIdIs10Characters() throws WHPValidationException {
        PatientRequest request = new PatientRequestBuilder().withDefaults().withCaseId("1234567890").build();
        request.validate(validator);
    }

    @Test
    public void shouldNotThrowException_WhenCaseIdIs11Characters() throws WHPValidationException {
        PatientRequest request = new PatientRequestBuilder().withDefaults().withCaseId("12345678901").build();
        request.validate(validator);
    }

    @Test
    public void shouldThrowException_WhenCaseIdIsLessThan10Characters() throws WHPValidationException {
        expectException("field:case_id:size must be between 10 and 11");
        PatientRequest request = new PatientRequestBuilder().withDefaults().withCaseId("123456789").build();
        request.validate(validator);
    }

    @Test
    public void shouldThrowException_WhenCaseIdIsMoreThan11Characters() throws WHPValidationException {
        expectException("field:case_id:size must be between 10 and 11");
        PatientRequest request = new PatientRequestBuilder().withDefaults().withCaseId("123456789012").build();
        request.validate(validator);
    }

    @Test
    public void shouldNotThrowException_WhenProviderIdIs5Characters() throws WHPValidationException {
        PatientRequest request = new PatientRequestBuilder().withDefaults().withProviderId("12345").build();
        request.validate(validator);
    }

    @Test
    public void shouldNotThrowException_WhenProviderIdIs6Characters() throws WHPValidationException {
        PatientRequest request = new PatientRequestBuilder().withDefaults().withProviderId("123456").build();
        request.validate(validator);
    }

    @Test
    public void shouldThrowException_WhenProviderIdIsLessThan5Characters() throws WHPValidationException {
        expectException("field:provider_id:size must be between 5 and 6");
        PatientRequest request = new PatientRequestBuilder().withDefaults().withProviderId("1234").build();
        request.validate(validator);
    }

    @Test
    public void shouldThrowException_WhenProviderIdIsMoreThan6Characters() throws WHPValidationException {
        expectException("field:provider_id:size must be between 5 and 6");
        PatientRequest request = new PatientRequestBuilder().withDefaults().withProviderId("1234567").build();
        request.validate(validator);
    }

    @Test
    public void shouldNotThrowException_WhenLastModifiedDateFormatIsCorrect() throws WHPValidationException {
        PatientRequest request = new PatientRequestBuilder().withDefaults().withLastModifiedDate("03/04/2012 02:20:30").build();
        request.validate(validator);
    }

    @Test
    public void shouldThrowException_WhenLastModifiedDateFormatIsNull() throws WHPValidationException {
        expectException("field:date_modified:null");
        PatientRequest request = new PatientRequestBuilder().withDefaults().withLastModifiedDate(null).build();
        request.validate(validator);
    }

    @Test
    public void shouldThrowException_WhenLastModifiedDateFormatIsNotTheCorrectDateTimeFormat() throws WHPValidationException {
        expectException("03-04-2012\" is malformed at \"-04-2012");
        PatientRequest request = new PatientRequestBuilder().withDefaults().withLastModifiedDate("03-04-2012").build();
        request.validate(validator);
    }

    @Test(expected = WHPValidationException.class)
    public void shouldThrowException_WhenSmearTest1DateFormatIsIncorrect() throws WHPValidationException {
        PatientRequest request = new PatientRequestBuilder().withDefaults().withSmearTestDate1("03/04/2012  11:23:40").build();
        request.validate(validator);
    }

    @Test(expected = WHPValidationException.class)
    public void shouldThrowException_WhenSmearTest2DateFormatIsIncorrect() throws WHPValidationException {
        PatientRequest request = new PatientRequestBuilder().withDefaults().withSmearTestDate2("03/04/2012  11:23:40").build();
        request.validate(validator);
    }

    @Test
    public void shouldThrowExceptionWhenTreatmentCategoryIsValid() throws WHPValidationException {
        new PatientRequestBuilder().withDefaults().withTreatmentCategory("01").build().validate(validator);
        new PatientRequestBuilder().withDefaults().withTreatmentCategory("02").build().validate(validator);
        new PatientRequestBuilder().withDefaults().withTreatmentCategory("11").build().validate(validator);
        new PatientRequestBuilder().withDefaults().withTreatmentCategory("12").build().validate(validator);
    }

    @Test
    public void shouldThrowException_WhenTreatmentCategoryIsNotValid() throws WHPValidationException {
        expectException("field:treatment_category:must match \"[0|1][1|2]\"");
        PatientRequest request = new PatientRequestBuilder().withDefaults().withTreatmentCategory("99").build();
        request.validate(validator);
    }

    @Test
    public void shouldNotThrowException_WhenMobileNumberIsEmpty() throws WHPValidationException {
        PatientRequest request = new PatientRequestBuilder().withDefaults().withMobileNumber("").build();
        request.validate(validator);
    }

    @Test
    public void shouldNotThrowException_WhenMobileNumberIs10Digits() throws WHPValidationException {
        PatientRequest request = new PatientRequestBuilder().withDefaults().withMobileNumber("1234567890").build();
        request.validate(validator);
    }

    @Test
    public void shouldThrowException_WhenMobileNumberIsLessThan10Digits() throws WHPValidationException {
        expectException("field:mobile_number:must match \"^$|[0-9]{10}\"");
        PatientRequest request = new PatientRequestBuilder().withDefaults().withMobileNumber("123456789").build();
        request.validate(validator);
    }

    @Test
    public void shouldThrowException_WhenMobileNumberIsMoreThan10Digits() throws WHPValidationException {
        expectException("field:mobile_number:must match \"^$|[0-9]{10}\"");
        PatientRequest request = new PatientRequestBuilder().withDefaults().withMobileNumber("12345678901").build();
        request.validate(validator);
    }

    @Test
    public void shouldThrowException_WhenEnumFieldIsNull() {
        expectException("field:smear_test_result_1:The value should be one of : [Positive, Negative]");
        PatientRequest request = new PatientRequestBuilder().withDefaults().withSmearTestResult1(null).build();
        request.validate(validator);
    }

    @Test
    public void shouldThrowException_WhenTbIdFieldIsNull() {
        expectException("field:tb_id:may not be null");
        PatientRequest request = new PatientRequestBuilder().withDefaults().withTBId(null).build();
        request.validate(validator);
    }

    private void expectException(String message) {
        exceptionThrown.expect(WHPValidationException.class);
        exceptionThrown.expectMessage(new Contains(message));
    }

}
