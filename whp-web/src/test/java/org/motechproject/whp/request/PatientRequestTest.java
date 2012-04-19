package org.motechproject.whp.request;

import org.junit.Test;
import org.motechproject.whp.builder.PatientRequestBuilder;
import org.motechproject.whp.exception.WHPValidationException;

public class PatientRequestTest {

    @Test(expected = WHPValidationException.class)
    public void shouldThrowException_WhenLastModifiedDateFormatIsIncorrect() {
        PatientRequest request = new PatientRequestBuilder().withDefaults().withLastModifiedDate("03/04/2012").build();
        request.validate();
    }

    @Test(expected = WHPValidationException.class)
    public void shouldThrowException_WhenRegistrationDateFormatIsIncorrect() {
        PatientRequest request = new PatientRequestBuilder().withDefaults().withRegistrationDate("03/04/2012  11:23:40").build();
        request.validate();
    }

    @Test(expected = WHPValidationException.class)
    public void shouldThrowException_WhenSmearTest1DateFormatIsIncorrect() {
        PatientRequest request = new PatientRequestBuilder().withDefaults().withSmearTestDate1("03/04/2012  11:23:40").build();
        request.validate();
    }

    @Test(expected = WHPValidationException.class)
    public void shouldThrowException_WhenSmearTest2DateFormatIsIncorrect() {
        PatientRequest request = new PatientRequestBuilder().withDefaults().withSmearTestDate2("03/04/2012  11:23:40").build();
        request.validate();
    }


}
