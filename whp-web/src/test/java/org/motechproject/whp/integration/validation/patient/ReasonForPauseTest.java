package org.motechproject.whp.integration.validation.patient;

import org.junit.Test;
import org.motechproject.whp.builder.PatientWebRequestBuilder;
import org.motechproject.whp.request.PatientWebRequest;
import org.motechproject.whp.validation.ValidationScope;

public class ReasonForPauseTest extends BasePatientTest {
    @Test
    public void shouldBeValidReasonForPauseIsSpecified() {
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().build();
        webRequest.setReason_for_pause("Pause");
        validator.validate(webRequest, ValidationScope.pauseTreatment);
    }

    @Test
    public void shouldBeValidReasonForPauseIsNotSpecified() {
        expectWHPException("field:reason_for_pause:value should not be null");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().build();
        validator.validate(webRequest, ValidationScope.pauseTreatment);
    }
}
