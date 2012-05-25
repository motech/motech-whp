package org.motechproject.whp.integration.validation.patient;

import org.junit.Test;
import org.motechproject.whp.builder.PatientWebRequestBuilder;
import org.motechproject.whp.patient.command.UpdateScope;
import org.motechproject.whp.request.PatientWebRequest;

public class ReasonForPauseTest extends BasePatientTest {

    @Test
    public void shouldThrowExceptionIfReasonNotSpecifiedInPauseTreatmentScope() {
        expectFieldValidationRuntimeException("field:reason:value should not be null");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().build();
        validator.validate(webRequest, UpdateScope.pauseTreatmentScope);
    }

    @Test
    public void shouldThrowExceptionIfReasonIsBlankInPauseTreatmentScope() {
        expectFieldValidationRuntimeException("field:reason:value should not be null");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withReasonForPause("").build();
        validator.validate(webRequest, UpdateScope.pauseTreatmentScope);
    }

    @Test
    public void shouldNotThrowExceptionIfReasonIsSpecifiedInPauseTreatmentScope() {
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withReasonForPause("Pause").build();
        validator.validate(webRequest, UpdateScope.pauseTreatmentScope);
    }

}
