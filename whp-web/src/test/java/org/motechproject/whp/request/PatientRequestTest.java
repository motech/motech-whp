package org.motechproject.whp.request;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.internal.matchers.Contains;
import org.motechproject.whp.builder.PatientRequestBuilder;
import org.motechproject.whp.common.integration.repository.SpringIntegrationTest;
import org.motechproject.whp.exception.WHPValidationException;
import org.motechproject.whp.validation.validator.BeanValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(locations = "classpath*:/applicationContext.xml")
public class PatientRequestTest extends SpringIntegrationTest {

    @Rule
    public ExpectedException exceptionThrown = ExpectedException.none();

    @Autowired
    private BeanValidator validator;

    @Test
    public void shouldNotThrowException_WhenLastModifiedDateFormatIsCorrect() {
        PatientRequest request = new PatientRequestBuilder().withDefaults().withLastModifiedDate("03/04/2012 02:20:30").build();
        request.validate(validator);
    }

    @Test
    public void shouldThrowException_WhenLastModifiedDateFormatIsNull() {
        exceptionThrown.expect(WHPValidationException.class);
        exceptionThrown.expectMessage(new Contains("field:date_modified:null"));

        PatientRequest request = new PatientRequestBuilder().withDefaults().withLastModifiedDate(null).build();
        request.validate(validator);
    }

    @Test
    public void shouldThrowException_WhenLastModifiedDateFormatIsNotTheCorrectDateTimeFormat() {
        exceptionThrown.expect(WHPValidationException.class);
        exceptionThrown.expectMessage(new Contains("03-04-2012\" is malformed at \"-04-2012"));

        PatientRequest request = new PatientRequestBuilder().withDefaults().withLastModifiedDate("03-04-2012").build();
        request.validate(validator);
    }

    @Test
    public void shouldThrowException_WhenRegistrationDateFormatIsNotTheCorrectLocalDateFormat() {
        exceptionThrown.expect(WHPValidationException.class);
        exceptionThrown.expectMessage(new Contains("field:registration_date:Invalid format: \"03/04/2012  11:23:40\" is malformed at \"  11:23:40\""));

        PatientRequest request = new PatientRequestBuilder().withDefaults().withRegistrationDate("03/04/2012  11:23:40").build();
        request.validate(validator);
    }

    @Test(expected = WHPValidationException.class)
    public void shouldThrowException_WhenSmearTest1DateFormatIsIncorrect() {
        PatientRequest request = new PatientRequestBuilder().withDefaults().withSmearTestDate1("03/04/2012  11:23:40").build();
        request.validate(validator);
    }

    @Test(expected = WHPValidationException.class)
    public void shouldThrowException_WhenSmearTest2DateFormatIsIncorrect() {
        PatientRequest request = new PatientRequestBuilder().withDefaults().withSmearTestDate2("03/04/2012  11:23:40").build();
        request.validate(validator);
    }


}
