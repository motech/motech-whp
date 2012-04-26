package org.motechproject.whp.integration.validation;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.internal.matchers.Contains;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.builder.PatientRequestBuilder;
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
    public void shouldNotThrowException_WhenCaseIdIs10Characters() throws WHPException {
        PatientRequest request = new PatientRequestBuilder().withDefaults().withCaseId("1234567890").build();
        validator.validate(request, ValidationScope.create, "patient");
    }

    @Test
    public void shouldNotThrowException_WhenCaseIdIs11Characters() throws WHPException {
        PatientRequest request = new PatientRequestBuilder().withDefaults().withCaseId("12345678901").build();
        validator.validate(request, ValidationScope.create, "patient");
    }

    @Test
    public void shouldThrowException_WhenCaseIdIsLessThan10Characters() throws WHPException {
        expectException("field:case_id:size must be between 10 and 11");
        PatientRequest request = new PatientRequestBuilder().withDefaults().withCaseId("123456789").build();
        validator.validate(request, ValidationScope.create, "patient");
    }

    @Test
    public void shouldThrowException_WhenCaseIdIsMoreThan11Characters() throws WHPException {
        expectException("field:case_id:size must be between 10 and 11");
        PatientRequest request = new PatientRequestBuilder().withDefaults().withCaseId("123456789012").build();
        validator.validate(request, ValidationScope.create, "patient");
    }

    @Test
    public void shouldNotThrowException_WhenProviderIdIs5Characters() throws WHPException {
        allProviders.add(new Provider("12345", "1234567890", "chambal", DateUtil.now()));
        PatientRequest request = new PatientRequestBuilder().withDefaults().withProviderId("12345").build();
        validator.validate(request, ValidationScope.create, "patient");
    }

    @Test
    public void shouldNotThrowException_WhenProviderIdIs6Characters() throws WHPException {
        PatientRequest request = new PatientRequestBuilder().withDefaults().withProviderId("123456").build();
        validator.validate(request, ValidationScope.create, "patient");
    }

    @Test
    public void shouldThrowException_WhenProviderIdIsLessThan5Characters() throws WHPException {
        expectException("field:provider_id:size must be between 5 and 6");
        PatientRequest request = new PatientRequestBuilder().withDefaults().withProviderId("1234").build();
        validator.validate(request, ValidationScope.create, "patient");
    }

    @Test
    public void shouldThrowException_WhenProviderIdIsMoreThan6Characters() throws WHPException {
        expectException("field:provider_id:size must be between 5 and 6");
        PatientRequest request = new PatientRequestBuilder().withDefaults().withProviderId("1234567").build();
        validator.validate(request, ValidationScope.create, "patient");
    }

    @Test
    public void shouldNotThrowException_WhenLastModifiedDateFormatIsCorrect() throws WHPException {
        PatientRequest request = new PatientRequestBuilder().withDefaults().withLastModifiedDate("03/04/2012 02:20:30").build();
        validator.validate(request, ValidationScope.create, "patient");
    }

    @Test
    public void shouldThrowException_WhenLastModifiedDateFormatIsNull() throws WHPException {
        expectException("field:date_modified:null");
        PatientRequest request = new PatientRequestBuilder().withDefaults().withLastModifiedDate(null).build();
        validator.validate(request, ValidationScope.create, "patient");
    }

    @Test
    public void shouldThrowException_WhenLastModifiedDateFormatIsNotTheCorrectDateTimeFormat() throws WHPException {
        expectException("03-04-2012\" is malformed at \"-04-2012");
        PatientRequest request = new PatientRequestBuilder().withDefaults().withLastModifiedDate("03-04-2012").build();
        validator.validate(request, ValidationScope.create, "patient");
    }

    @Test(expected = WHPException.class)
    public void shouldThrowException_WhenSmearTest1DateFormatIsIncorrect() throws WHPException {
        PatientRequest request = new PatientRequestBuilder().withDefaults().withSmearTestDate1("03/04/2012  11:23:40").build();
        validator.validate(request, ValidationScope.create, "patient");
    }

    @Test(expected = WHPException.class)
    public void shouldThrowException_WhenSmearTest2DateFormatIsIncorrect() throws WHPException {
        PatientRequest request = new PatientRequestBuilder().withDefaults().withSmearTestDate2("03/04/2012  11:23:40").build();
        validator.validate(request, ValidationScope.create, "patient");
    }

    @Test
    public void shouldThrowExceptionWhenTreatmentCategoryIsValid() throws WHPException {
        validate(new PatientRequestBuilder().withDefaults().withTreatmentCategory("01").build());
        validate(new PatientRequestBuilder().withDefaults().withTreatmentCategory("02").build());
        validate(new PatientRequestBuilder().withDefaults().withTreatmentCategory("11").build());
        validate(new PatientRequestBuilder().withDefaults().withTreatmentCategory("12").build());
    }

    @Test
    public void shouldThrowException_WhenTreatmentCategoryIsNotValid() throws WHPException {
        expectException("field:treatment_category:must match \"[0|1][1|2]\"");
        PatientRequest request = new PatientRequestBuilder().withDefaults().withTreatmentCategory("99").build();
        validator.validate(request, ValidationScope.create, "patient");
    }

    @Test
    public void shouldNotThrowException_WhenMobileNumberIsEmpty() throws WHPException {
        PatientRequest request = new PatientRequestBuilder().withDefaults().withMobileNumber("").build();
        validator.validate(request, ValidationScope.create, "patient");
    }

    @Test
    public void shouldNotThrowException_WhenMobileNumberIs10Digits() throws WHPException {
        PatientRequest request = new PatientRequestBuilder().withDefaults().withMobileNumber("1234567890").build();
        validator.validate(request, ValidationScope.create, "patient");
    }

    @Test
    public void shouldThrowException_WhenMobileNumberIsLessThan10Digits() throws WHPException {
        expectException("field:mobile_number:must match \"^$|[0-9]{10}\"");
        PatientRequest request = new PatientRequestBuilder().withDefaults().withMobileNumber("123456789").build();
        validator.validate(request, ValidationScope.create, "patient");
    }

    @Test
    public void shouldThrowException_WhenMobileNumberIsMoreThan10Digits() throws WHPException {
        expectException("field:mobile_number:must match \"^$|[0-9]{10}\"");
        PatientRequest request = new PatientRequestBuilder().withDefaults().withMobileNumber("12345678901").build();
        validator.validate(request, ValidationScope.create, "patient");
    }

    @Test
    public void shouldThrowException_WhenEnumFieldIsNull() throws WHPException {
        expectException("field:smear_test_result_1:The value should be one of : [Positive, Negative]");
        PatientRequest request = new PatientRequestBuilder().withDefaults().withSmearTestResult1(null).build();
        validator.validate(request, ValidationScope.create, "patient");
    }

    @Test
    public void shouldThrowException_WhenTbIdFieldIsNull() throws WHPException {
        expectException("field:tb_id:may not be null");
        PatientRequest request = new PatientRequestBuilder().withDefaults().withTBId(null).build();
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
