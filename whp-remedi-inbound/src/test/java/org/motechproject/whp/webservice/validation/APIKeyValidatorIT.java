package org.motechproject.whp.webservice.validation;

import lombok.Data;
import org.ektorp.CouchDbConnector;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.testing.utils.SpringIntegrationTest;
import org.motechproject.validation.constraints.NamedConstraint;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.BeanPropertyBindingResult;

import java.util.Properties;

import static junit.framework.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/applicationCommonContext.xml")
public class APIKeyValidatorIT extends SpringIntegrationTest {

    private Properties remediProperty;
    private TestClass testObject;
    private BeanPropertyBindingResult errors;
    private APIKeyValidator requestValidator;


    @Before
    public void setUp() {
        remediProperty = new Properties();
        remediProperty.setProperty("remedi.api.key", "remediAPIKey");

        testObject = new TestClass();
        errors = new BeanPropertyBindingResult(testObject, "test");
        requestValidator = new APIKeyValidator(remediProperty);
    }

    @Test
    public void shouldPassValidationIfAPIKeyIsValid() throws NoSuchFieldException {
        testObject.setApi_key("remediAPIKey");
        requestValidator.validateField(testObject, testObject.getClass().getField("api_key"), errors);
        assertEquals(0, errors.getAllErrors().size());
    }

    @Test
    public void shouldFailValidationIfAPIKeyIsInvalid() throws NoSuchFieldException {
        testObject.setApi_key("invalidAPIKey");
        requestValidator.validateField(testObject, testObject.getClass().getField("api_key"), errors);
        assertEquals(1, errors.getAllErrors().size());
    }

    @Override
    public CouchDbConnector getDBConnector() {
        return null;
    }

    @Data
    public static class TestClass {

        @NamedConstraint(name = APIKeyValidator.API_KEY_VALIDATION)
        public String api_key;
    }

}
