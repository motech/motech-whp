package org.motechproject.whp.integration.validation.patient;

import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.builder.PatientWebRequestBuilder;
import org.motechproject.whp.patient.domain.Provider;
import org.motechproject.whp.patient.service.AllCommands;
import org.motechproject.whp.request.PatientWebRequest;

public class GenderTest extends BasePatientTest {

    @Test
    public void shouldThrowExceptionWhenGenderNotEnumerated() {
        expectFieldValidationRuntimeException("field:gender:The value should be one of : [M, F, O]");
        allProviders.add(new Provider("12345", "1234567890", "chambal", DateUtil.now()));
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withProviderId("12345").withGender("H").build();
        validator.validate(webRequest, AllCommands.create);
    }

    @Test
    public void shouldNotThrowExceptionWhenGenderIsMale() {
        allProviders.add(new Provider("12345", "1234567890", "chambal", DateUtil.now()));
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withProviderId("12345").withGender(org.motechproject.whp.refdata.domain.Gender.M.name()).build();
        validator.validate(webRequest, AllCommands.create);
    }

    @Test
    public void shouldNotThrowExceptionWhenGenderIsFemale() {
        allProviders.add(new Provider("12345", "1234567890", "chambal", DateUtil.now()));
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withProviderId("12345").withGender(org.motechproject.whp.refdata.domain.Gender.F.name()).build();
        validator.validate(webRequest, AllCommands.create);
    }

    @Test
    public void shouldNotThrowExceptionWhenGenderIsOther() {
        allProviders.add(new Provider("12345", "1234567890", "chambal", DateUtil.now()));
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withProviderId("12345").withGender(org.motechproject.whp.refdata.domain.Gender.O.name()).build();
        validator.validate(webRequest, AllCommands.create);
    }

    //Any enum field
    @Test
    public void shouldThrowExceptionWhenGenderIsNull() {
        expectFieldValidationRuntimeException("field:gender:value should not be null");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withGender(null).build();
        validator.validate(webRequest, AllCommands.create);
    }

}
