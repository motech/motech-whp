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

@ContextConfiguration(locations = "classpath*:/applicationContext.xml")
public class PatientRequestTest extends SpringIntegrationTest {

    @Rule
    public ExpectedException exceptionThrown = ExpectedException.none();

    @Autowired
    private BeanValidator validator;

    @Test
    public void shouldNotThrowException_WhenCaseIdIs10Characters() {
        PatientRequest request = new PatientRequestBuilder().withDefaults().withCaseId("1234567890").build();
        request.validate(validator);
    }

    @Test
    public void shouldNotThrowException_WhenCaseIdIs11Characters() {
        PatientRequest request = new PatientRequestBuilder().withDefaults().withCaseId("12345678901").build();
        request.validate(validator);
    }

    @Test
    public void shouldThrowException_WhenCaseIdIsLessThan10Characters() {
        expectException("field:case_id:size must be between 10 and 11");
        PatientRequest request = new PatientRequestBuilder().withDefaults().withCaseId("123456789").build();
        request.validate(validator);
    }

    @Test
    public void shouldThrowException_WhenCaseIdIsMoreThan11Characters() {
        expectException("field:case_id:size must be between 10 and 11");
        PatientRequest request = new PatientRequestBuilder().withDefaults().withCaseId("123456789012").build();
        request.validate(validator);
    }

    @Test
    public void shouldNotThrowException_WhenProviderIdIs5Characters() {
        PatientRequest request = new PatientRequestBuilder().withDefaults().withProviderId("12345").build();
        request.validate(validator);
    }

    @Test
    public void shouldNotThrowException_WhenProviderIdIs6Characters() {
        PatientRequest request = new PatientRequestBuilder().withDefaults().withProviderId("123456").build();
        request.validate(validator);
    }

    @Test
    public void shouldThrowException_WhenProviderIdIsLessThan5Characters() {
        expectException("field:provider_id:size must be between 5 and 6");
        PatientRequest request = new PatientRequestBuilder().withDefaults().withProviderId("1234").build();
        request.validate(validator);
    }

    @Test
    public void shouldThrowException_WhenProviderIdIsMoreThan6Characters() {
        expectException("field:provider_id:size must be between 5 and 6");
        PatientRequest request = new PatientRequestBuilder().withDefaults().withProviderId("1234567").build();
        request.validate(validator);
    }

    @Test
    public void shouldNotThrowException_WhenLastModifiedDateFormatIsCorrect() {
        PatientRequest request = new PatientRequestBuilder().withDefaults().withLastModifiedDate("03/04/2012 02:20:30").build();
        request.validate(validator);
    }

    @Test
    public void shouldThrowException_WhenLastModifiedDateFormatIsNull() {
        expectException("field:date_modified:null");
        PatientRequest request = new PatientRequestBuilder().withDefaults().withLastModifiedDate(null).build();
        request.validate(validator);
    }

    @Test
    public void shouldThrowException_WhenLastModifiedDateFormatIsNotTheCorrectDateTimeFormat() {
        expectException("03-04-2012\" is malformed at \"-04-2012");
        PatientRequest request = new PatientRequestBuilder().withDefaults().withLastModifiedDate("03-04-2012").build();
        request.validate(validator);
    }

    @Test(expected = WHPValidationException.class)
    public void shouldThrowException_WhenSmearTest1DateFormatIsIncorrect() {
        PatientRequest request = new PatientRequestBuilder().withDefaults().withSmearTestDate1("03/04/2012  11:23:40").build();
        request.validate(validator);
    }

    @Test(expected = WHPValidationException.class)
    public void shouldThrowException_WhenSmearTest2DateFormatIsIncorrect() {
        PatientRequest request = new PatientRequestBuilder().withDefaults().withSmearTestDate2("03/04/2012  11:23:40").build();
        request.validate(validator);
    }

    @Test
    public void shouldThrowExceptionWhenTreatmentCategoryIsValid() {
        new PatientRequestBuilder().withDefaults().withTreatmentCategory("01").build().validate(validator);
        new PatientRequestBuilder().withDefaults().withTreatmentCategory("02").build().validate(validator);
        new PatientRequestBuilder().withDefaults().withTreatmentCategory("11").build().validate(validator);
        new PatientRequestBuilder().withDefaults().withTreatmentCategory("12").build().validate(validator);
    }

    @Test
    public void shouldThrowException_WhenTreatmentCategoryIsNotValid() {
        expectException("field:treatment_category:must match \"[0|1][1|2]\"");
        PatientRequest request = new PatientRequestBuilder().withDefaults().withTreatmentCategory("99").build();
        request.validate(validator);
    }

    @Test
    public void shouldNotThrowException_WhenMobileNumberIsEmpty() {
        PatientRequest request = new PatientRequestBuilder().withDefaults().withMobileNumber("").build();
        request.validate(validator);
    }

    @Test
    public void shouldNotThrowException_WhenMobileNumberIs10Digits() {
        PatientRequest request = new PatientRequestBuilder().withDefaults().withMobileNumber("1234567890").build();
        request.validate(validator);
    }

    @Test
    public void shouldThrowException_WhenMobileNumberIsLessThan10Digits() {
        expectException("field:mobile_number:must match \"^$|[0-9]{10}\"");
        PatientRequest request = new PatientRequestBuilder().withDefaults().withMobileNumber("123456789").build();
        request.validate(validator);
    }

    @Test
    public void shouldThrowException_WhenMobileNumberIsMoreThan10Digits() {
        expectException("field:mobile_number:must match \"^$|[0-9]{10}\"");
        PatientRequest request = new PatientRequestBuilder().withDefaults().withMobileNumber("12345678901").build();
        request.validate(validator);
    }

    private void expectException(String message) {
        exceptionThrown.expect(WHPValidationException.class);
        exceptionThrown.expectMessage(new Contains(message));
    }

}
