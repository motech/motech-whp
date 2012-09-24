package org.motechproject.whp.webservice.it.request.patient;

import org.junit.Test;
import org.motechproject.whp.patient.command.UpdateScope;
import org.motechproject.whp.webservice.builder.PatientWebRequestBuilder;
import org.motechproject.whp.webservice.request.PatientWebRequest;

public class APIKeyIT extends BasePatientIT {
    @Test
    public void shouldBeValidWhenAPIKeyIsValid() {
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withWeight("20").withAPIKey("3F2504E04F8911D39A0C0305E82C3301").build();
        validator.validate(webRequest, UpdateScope.createScope);
    }

    @Test
    public void shouldBeInvalidWhenAPIKeyIsInvalid() {
        expectFieldValidationRuntimeException("field:api_key:api_key:is invalid.");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withWeight("20").withAPIKey("invalid_api_key").build();
        validator.validate(webRequest, UpdateScope.createScope);
    }
}
