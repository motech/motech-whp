package org.motechproject.whp.integration.validation.patient;

import org.junit.Test;
import org.motechproject.whp.builder.PatientWebRequestBuilder;
import org.motechproject.whp.patient.command.AllCommands;
import org.motechproject.whp.request.PatientWebRequest;

public class APIKeyTest extends BasePatientTest {
    @Test
    public void shouldBeValidWhenAPIKeyIsValid() {
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withWeight("20").withAPIKey("3F2504E04F8911D39A0C0305E82C3301").build();
        validator.validate(webRequest, AllCommands.create);
    }

    @Test
    public void shouldBeInvalidWhenAPIKeyIsInvalid() {
        expectFieldValidationRuntimeException("field:api_key:api_key:is invalid.");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withWeight("20").withAPIKey("invalid_api_key").build();
        validator.validate(webRequest, AllCommands.create);
    }
}
