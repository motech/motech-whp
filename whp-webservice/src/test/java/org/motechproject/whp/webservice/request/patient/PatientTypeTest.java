package org.motechproject.whp.webservice.request.patient;

import org.junit.Test;
import org.motechproject.whp.patient.command.UpdateScope;
import org.motechproject.whp.webservice.builder.PatientWebRequestBuilder;
import org.motechproject.whp.webservice.request.PatientWebRequest;

public class PatientTypeTest extends BasePatientTest{

    @Test
    public void shouldThrowExceptionIfPatientTypeIsNullInCreateScope() {
        expectFieldValidationRuntimeException("field:patient_type:value should not be null");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withPatientType(null).build();
        validator.validate(webRequest, UpdateScope.createScope);
    }

    @Test
    public void shouldThrowExceptionIfPatientTypeIsEmptyInCreateScope() {
        expectFieldValidationRuntimeException("The value should be one of : [New, Relapse, TransferredIn, TreatmentAfterDefault, TreatmentFailure, Chronic, Others]");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withPatientType("").build();
        validator.validate(webRequest, UpdateScope.createScope);
    }

    @Test
    public void shouldThrowExceptionIfPatientTypeIsEmptyInOpenNewTreatmentScope() {
        expectFieldValidationRuntimeException("The value should be one of : [New, Relapse, TransferredIn, TreatmentAfterDefault, TreatmentFailure, Chronic, Others]");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withPatientType("").build();
        validator.validate(webRequest, UpdateScope.openTreatmentScope);
    }

    @Test
    public void shouldNotThrowExceptionIfPatientTypeIsPresentInOpenNewTreatmentScope() {
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withPatientType("Relapse").build();
        validator.validate(webRequest, UpdateScope.openTreatmentScope);
    }
}
