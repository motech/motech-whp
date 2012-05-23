package org.motechproject.whp.integration.validation.patient;

import org.junit.Test;
import org.motechproject.whp.builder.PatientWebRequestBuilder;
import org.motechproject.whp.patient.service.AllCommands;
import org.motechproject.whp.request.PatientWebRequest;

public class CaseIdTest extends BasePatientTest {
    @Test
    public void shouldNotThrowException_WhenCaseIdIs10Characters() {
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withCaseId("1234567890").build();
        validator.validate(webRequest, AllCommands.create);
    }

    @Test
    public void shouldNotThrowException_WhenCaseIdIs11Characters() {
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withCaseId("12345678901").build();
        validator.validate(webRequest, AllCommands.create);
    }
}
