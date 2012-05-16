package org.motechproject.whp.integration.validation.patient;

import org.junit.Test;
import org.motechproject.whp.builder.PatientWebRequestBuilder;
import org.motechproject.whp.request.PatientWebRequest;
import org.motechproject.whp.validation.ValidationScope;

public class ReasonForRestartTest extends BasePatientTest {
    @Test
    public void shouldBeValidReasonForRestartIsNotSpecified() {
        expectWHPException("field:reason_for_restart:may not be empty");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().build();
        validator.validate(webRequest, ValidationScope.restartTreatment);
    }

    @Test
    public void shouldBeValidReasonForRestartIsBlank() {
        expectWHPException("field:reason_for_restart:may not be empty");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withReasonForRestart("").build();
        validator.validate(webRequest, ValidationScope.restartTreatment);
    }

    @Test
    public void shouldBeValidReasonForRestartIsSpecified() {
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withReasonForRestart("Restart").build();
        validator.validate(webRequest, ValidationScope.restartTreatment);
    }
}
