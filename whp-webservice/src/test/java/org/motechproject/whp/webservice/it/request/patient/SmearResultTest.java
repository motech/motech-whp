package org.motechproject.whp.webservice.it.request.patient;

import org.junit.Test;
import org.motechproject.whp.patient.command.UpdateScope;
import org.motechproject.whp.webservice.builder.PatientWebRequestBuilder;
import org.motechproject.whp.webservice.request.PatientWebRequest;

public class SmearResultTest extends BasePatientTest {

    @Test
    public void shouldThrowException_WhenSmearTest1DateFormatIsIncorrect() {
        expectFieldValidationRuntimeException("field:smear_test_date_1:Invalid format: \"03/04/2012  11:23:40\" is malformed at \"  11:23:40\"");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withSmearTestDate1("03/04/2012  11:23:40").build();
        validator.validate(webRequest, UpdateScope.openTreatmentScope);
    }

    @Test
    public void shouldThrowException_WhenSmearTest2DateFormatIsIncorrect() {
        expectFieldValidationRuntimeException("field:smear_test_date_2:Invalid format: \"03/04/2012  11:23:40\" is malformed at \"  11:23:40\"");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withSmearTestDate2("03/04/2012  11:23:40").build();
        validator.validate(webRequest, UpdateScope.openTreatmentScope);
    }

    @Test
    public void shouldNotThrowException_WhenSmearTestResultsIsNull_ForUpdateScope() {
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withSimpleUpdateFields().withSmearTestResults(null, null, null, null, null).build();
        validator.validate(webRequest, UpdateScope.simpleUpdateScope);
    }

}
