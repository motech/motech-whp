package org.motechproject.whp.integration.validation.patient;

import org.junit.Test;
import org.motechproject.whp.builder.PatientWebRequestBuilder;
import org.motechproject.whp.patient.service.AllCommands;
import org.motechproject.whp.request.PatientWebRequest;

public class SmearResultTest extends BasePatientTest {

    @Test
    public void shouldThrowException_WhenSmearTest1DateFormatIsIncorrect() {
        expectFieldValidationRuntimeException("field:smear_test_date_1:Invalid format: \"03/04/2012  11:23:40\" is malformed at \"  11:23:40\"");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withSmearTestDate1("03/04/2012  11:23:40").build();
        validator.validate(webRequest, AllCommands.openTreatment);
    }

    @Test
    public void shouldThrowException_WhenSmearTest2DateFormatIsIncorrect() {
        expectFieldValidationRuntimeException("field:smear_test_date_2:Invalid format: \"03/04/2012  11:23:40\" is malformed at \"  11:23:40\"");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withSmearTestDate2("03/04/2012  11:23:40").build();
        validator.validate(webRequest, AllCommands.openTreatment);
    }

    @Test
    public void shouldNotThrowException_WhenSmearTestResultsIsNull_ForUpdateScope() {
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withSimpleUpdateFields().withSmearTestResults(null, null, null, null, null).build();
        validator.validate(webRequest, AllCommands.simpleUpdate);
    }

    @Test
    public void shouldThrowException_WhenSmearTestResultsIsNull_InCreateScope() {
        expectFieldValidationRuntimeException("field:smear_test_date_1:value should not be null");
        expectFieldValidationRuntimeException("field:smear_test_result_1:value should not be null");
        expectFieldValidationRuntimeException("field:smear_test_date_2:value should not be null");
        expectFieldValidationRuntimeException("field:smear_test_result_2:value should not be null");
        expectFieldValidationRuntimeException("field:smear_sample_instance:value should not be null");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withOnlyRequiredTreatmentUpdateFields().withSmearTestResults(null, null, null, null, null).build();
        validator.validate(webRequest, AllCommands.create);
    }

    @Test
    public void shouldThrowException_WhenSmearTestResultsIsNull_InOpenTreatmentScope() {
        expectFieldValidationRuntimeException("field:smear_test_date_1:value should not be null");
        expectFieldValidationRuntimeException("field:smear_test_result_1:value should not be null");
        expectFieldValidationRuntimeException("field:smear_test_date_2:value should not be null");
        expectFieldValidationRuntimeException("field:smear_test_result_2:value should not be null");
        expectFieldValidationRuntimeException("field:smear_sample_instance:value should not be null");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withOnlyRequiredTreatmentUpdateFields().withSmearTestResults(null, null, null, null, null).build();
        validator.validate(webRequest, AllCommands.openTreatment);
    }

}
