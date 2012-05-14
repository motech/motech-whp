package org.motechproject.whp.integration.validation.patient;

import org.junit.Test;
import org.motechproject.whp.builder.PatientWebRequestBuilder;
import org.motechproject.whp.patient.exception.WHPException;
import org.motechproject.whp.request.PatientWebRequest;
import org.motechproject.whp.validation.ValidationScope;

import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

public class TBIdTest extends BasePatientTest {
    @Test
    public void shouldThrowException_WhenTbIdFieldIsNotElevenDigits_InCreateScope() {
        expectWHPException("field:tb_id:size must be between 11 and 11");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withTBId("").build();
        validator.validate(webRequest, ValidationScope.create);
    }

    @Test
    public void shouldThrowException_WhenTbIdFieldIsNotElevenDigits_InSimpleUpdateScope() {
        expectWHPException("field:tb_id:size must be between 11 and 11");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withTBId("").build();
        validator.validate(webRequest, ValidationScope.simpleUpdate);
    }

    @Test
    public void shouldThrowException_WhenTbIdFieldIsNotElevenDigits_InPauseTreatmentScope() {
        expectWHPException("field:tb_id:size must be between 11 and 11");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withTBId("").build();
        validator.validate(webRequest, ValidationScope.pauseTreatment);
    }

    @Test
    public void shouldThrowException_WhenTbIdFieldIsNull() {
        try {
            PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withTBId(null).build();
            validator.validate(webRequest, ValidationScope.create);
        } catch (WHPException e) {
            if (e.getMessage().contains("field:tb_id:TB ID cannot be null")) {
                fail("Not Null validation is not required. Validator implements null validation.");
            }
            assertTrue(e.getMessage().contains("field:tb_id:value should not be null"));
        }
    }
}
