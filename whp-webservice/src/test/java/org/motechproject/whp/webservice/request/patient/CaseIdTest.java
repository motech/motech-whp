package org.motechproject.whp.webservice.request.patient;

import org.junit.Test;
import org.motechproject.whp.patient.command.UpdateScope;
import org.motechproject.whp.webservice.builder.PatientWebRequestBuilder;
import org.motechproject.whp.webservice.request.PatientWebRequest;

public class CaseIdTest extends BasePatientTest {
    @Test
    public void shouldNotThrowException_WhenCaseIdIs10Characters() {
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withCaseId("1234567890").build();
        validator.validate(webRequest, UpdateScope.createScope);
    }

    @Test
    public void shouldNotThrowException_WhenCaseIdIs11Characters() {
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withCaseId("12345678901").build();
        validator.validate(webRequest, UpdateScope.createScope);
    }
}
