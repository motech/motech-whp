package org.motechproject.whp.integration.validation.patient;

import org.junit.Test;
import org.motechproject.whp.builder.PatientWebRequestBuilder;
import org.motechproject.whp.patient.command.AllCommands;
import org.motechproject.whp.patient.exception.WHPRuntimeException;
import org.motechproject.whp.request.PatientWebRequest;

import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

public class TBIdTest extends BasePatientTest {
    @Test
    public void shouldThrowException_WhenTbIdFieldIsNotElevenDigits_InCreateScope() {
        expectFieldValidationRuntimeException("field:tb_id:size must be between 11 and 11");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withTBId("").build();
        validator.validate(webRequest, AllCommands.create);
    }

    @Test
    public void shouldThrowException_WhenTbIdFieldIsNotElevenDigits_InSimpleUpdateScope() {
        expectFieldValidationRuntimeException("field:tb_id:size must be between 11 and 11");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withTBId("").build();
        validator.validate(webRequest, AllCommands.simpleUpdate);
    }

    @Test
    public void shouldThrowException_WhenTbIdFieldIsNotElevenDigits_InPauseTreatmentScope() {
        expectFieldValidationRuntimeException("field:tb_id:size must be between 11 and 11");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withTBId("").build();
        validator.validate(webRequest, AllCommands.pauseTreatment);
    }

    @Test
    public void shouldThrowException_WhenTbIdFieldIsNull() {
        try {
            PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withTBId(null).build();
            validator.validate(webRequest, AllCommands.create);
        } catch (WHPRuntimeException e) {
            if (e.getMessage().contains("field:tb_id:TB ID cannot be null")) {
                fail("Not Null validation is not required. Validator implements null validation.");
            }
            assertTrue(e.getMessage().contains("field:tb_id:value should not be null"));
        }
    }
}
