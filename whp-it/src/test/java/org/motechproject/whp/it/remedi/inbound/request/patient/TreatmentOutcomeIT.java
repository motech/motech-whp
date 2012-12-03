package org.motechproject.whp.it.remedi.inbound.request.patient;

import org.junit.Test;
import org.motechproject.whp.patient.command.UpdateScope;
import org.motechproject.whp.patient.domain.TreatmentOutcome;
import org.motechproject.whp.webservice.builder.PatientWebRequestBuilder;
import org.motechproject.whp.webservice.request.PatientWebRequest;

public class TreatmentOutcomeIT extends BasePatientIT {
    @Test
    public void shouldThrowExceptionIfTreatmentOutcomeIsNull() {
        expectFieldValidationRuntimeException("field:treatment_outcome:value should not be null");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withOnlyRequiredTreatmentUpdateFields().withTreatmentOutcome(null).build();
        validator.validate(webRequest, UpdateScope.closeTreatmentScope);
    }

    @Test
    public void shouldThrowExceptionIfTreatmentOutcomeIsEmpty() {
        expectFieldValidationRuntimeException("field:treatment_outcome:The value should be one of : [Cured, Died, Failure, Defaulted, TransferredOut, SwitchedOverToMDRTBTreatment, TreatmentCompleted]");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withOnlyRequiredTreatmentUpdateFields().withTreatmentOutcome("").build();
        validator.validate(webRequest, UpdateScope.closeTreatmentScope);
    }

    @Test
    public void shouldThrowExceptionIfTreatmentOutcomeIsAnInvalidReason() {
        expectFieldValidationRuntimeException("field:treatment_outcome:The value should be one of : [Cured, Died, Failure, Defaulted, TransferredOut, SwitchedOverToMDRTBTreatment, TreatmentCompleted]");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withOnlyRequiredTreatmentUpdateFields().withTreatmentOutcome("PatientGotBored").build();
        validator.validate(webRequest, UpdateScope.closeTreatmentScope);
    }

    @Test
    public void shouldNotThrowExceptionIfTreatmentOutcomeIsValid() {
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withOnlyRequiredTreatmentUpdateFields().withTreatmentOutcome(TreatmentOutcome.Cured.name()).build();
        validator.validate(webRequest, UpdateScope.closeTreatmentScope);
    }
}
