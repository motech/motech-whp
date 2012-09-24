package org.motechproject.whp.webservice.it.request.patient;

import org.junit.Test;
import org.motechproject.whp.common.exception.WHPRuntimeException;
import org.motechproject.whp.patient.command.UpdateScope;
import org.motechproject.whp.webservice.builder.PatientWebRequestBuilder;
import org.motechproject.whp.webservice.request.PatientWebRequest;

import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

public class TBIdIT extends BasePatientIT {

    @Test
    public void shouldThrowException_WhenTbIdFieldIsNull() {
        try {
            PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withTbId(null).build();
            validator.validate(webRequest, UpdateScope.createScope);
        } catch (WHPRuntimeException e) {
            if (e.getMessage().contains("field:tb_id:TB ID cannot be null")) {
                fail("Not Null validation is not required. Validator implements null validation.");
            }
            assertTrue(e.getMessage().contains("field:tb_id:value should not be null"));
        }
    }
}
