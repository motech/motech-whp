package org.motechproject.whp.it.remedi.inbound.request.patient;

import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.patient.command.UpdateScope;
import org.motechproject.whp.patient.domain.Gender;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.webservice.builder.PatientWebRequestBuilder;
import org.motechproject.whp.webservice.request.PatientWebRequest;

public class GenderIT extends BasePatientIT {

    @Test
    public void shouldThrowExceptionWhenGenderNotEnumerated() {
        expectFieldValidationRuntimeException("field:gender:The value should be one of : [M, F, O]");
        allProviders.add(new Provider("12345", "1234567890", "chambal", DateUtil.now()));
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withProviderId("12345").withGender("H").build();
        validator.validate(webRequest, UpdateScope.createScope);
    }

    @Test
    public void shouldNotThrowExceptionWhenGenderIsMale() {
        allProviders.add(new Provider("12345", "1234567890", "chambal", DateUtil.now()));
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withProviderId("12345").withGender(Gender.M.name()).build();
        validator.validate(webRequest, UpdateScope.createScope);
    }

    @Test
    public void shouldNotThrowExceptionWhenGenderIsFemale() {
        allProviders.add(new Provider("12345", "1234567890", "chambal", DateUtil.now()));
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withProviderId("12345").withGender(Gender.F.name()).build();
        validator.validate(webRequest, UpdateScope.createScope);
    }

    @Test
    public void shouldNotThrowExceptionWhenGenderIsOther() {
        allProviders.add(new Provider("12345", "1234567890", "chambal", DateUtil.now()));
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withProviderId("12345").withGender(Gender.O.name()).build();
        validator.validate(webRequest, UpdateScope.createScope);
    }

    //Any enum field
    @Test
    public void shouldThrowExceptionWhenGenderIsNull() {
        expectFieldValidationRuntimeException("field:gender:value should not be null");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withGender(null).build();
        validator.validate(webRequest, UpdateScope.createScope);
    }

}
