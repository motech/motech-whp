package org.motechproject.whp.integration.validation.patient;

import org.junit.Test;
import org.motechproject.whp.builder.PatientWebRequestBuilder;
import org.motechproject.whp.patient.command.UpdateScope;
import org.motechproject.whp.request.PatientWebRequest;

public class ReasonForRestartTest extends BasePatientTest {
    @Test
    public void shouldBeValidReasonForRestartIsNotSpecified() {
        expectFieldValidationRuntimeException("field:reason_for_restart:may not be empty");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().build();
        validator.validate(webRequest, UpdateScope.restartTreatmentScope);
    }

    @Test
    public void shouldBeValidReasonForRestartIsBlank() {
        expectFieldValidationRuntimeException("field:reason_for_restart:may not be empty");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withReasonForRestart("").build();
        validator.validate(webRequest, UpdateScope.restartTreatmentScope);
    }

    @Test
    public void shouldBeValidReasonForRestartIsSpecified() {
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withReasonForRestart("Restart").build();
        validator.validate(webRequest, UpdateScope.restartTreatmentScope);
    }
}
