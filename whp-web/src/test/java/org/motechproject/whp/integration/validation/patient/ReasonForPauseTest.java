package org.motechproject.whp.integration.validation.patient;

import org.junit.Test;
import org.motechproject.whp.builder.PatientWebRequestBuilder;
import org.motechproject.whp.patient.command.UpdateScope;
import org.motechproject.whp.request.PatientWebRequest;

public class ReasonForPauseTest extends BasePatientTest {
    @Test
    public void shouldBeValidReasonForPauseIsNotSpecified() {
        expectFieldValidationRuntimeException("field:reason_for_pause:may not be empty");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().build();
        validator.validate(webRequest, UpdateScope.pauseTreatmentScope);
    }

    @Test
    public void shouldBeValidReasonForPauseIsBlank() {
        expectFieldValidationRuntimeException("field:reason_for_pause:may not be empty");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withReasonForPause("").build();
        validator.validate(webRequest, UpdateScope.pauseTreatmentScope);
    }

    @Test
    public void shouldBeValidReasonForPauseIsSpecified() {
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withReasonForPause("Pause").build();
        validator.validate(webRequest, UpdateScope.pauseTreatmentScope);
    }
}
