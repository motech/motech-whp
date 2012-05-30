package org.motechproject.whp.integration.validation.patient;

import org.junit.Test;
import org.motechproject.whp.builder.PatientWebRequestBuilder;
import org.motechproject.whp.patient.command.UpdateScope;
import org.motechproject.whp.contract.PatientWebRequest;

public class ReasonForRestartTest extends BasePatientTest {

    @Test
    public void shouldThrowExceptionIfReasonNotSpecifiedInRestartTreatmentScope() {
        expectFieldValidationRuntimeException("field:reason:value should not be null");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().build();
        validator.validate(webRequest, UpdateScope.restartTreatmentScope);
    }

    @Test
    public void shouldThrowExceptionIfReasonIsBlankInRestartTreatmentScope() {
        expectFieldValidationRuntimeException("field:reason:value should not be null");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withReasonForPause("").build();
        validator.validate(webRequest, UpdateScope.pauseTreatmentScope);
    }

    @Test
    public void shouldNotThrowExceptionIfReasonIsSpecifiedInRestartTreatmentScope() {
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withReasonForPause("Restart").build();
        validator.validate(webRequest, UpdateScope.pauseTreatmentScope);
    }

}
