package org.motechproject.whp.it.remedi.inbound.validation;

import lombok.Data;
import org.ektorp.CouchDbConnector;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.testing.utils.SpringIntegrationTest;
import org.motechproject.validation.constraints.NamedConstraint;
import org.motechproject.whp.common.service.RemediProperties;
import org.motechproject.whp.webservice.validation.APIKeyValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.BeanPropertyBindingResult;

import static junit.framework.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/applicationITContext.xml")
public class APIKeyValidatorIT extends SpringIntegrationTest {

    @Autowired
    private RemediProperties remediProperties;
    private TestClass testObject;
    private BeanPropertyBindingResult errors;
    private APIKeyValidator requestValidator;


    @Before
    public void setUp() {
        testObject = new TestClass();
        errors = new BeanPropertyBindingResult(testObject, "test");
        requestValidator = new APIKeyValidator(remediProperties);
    }

    @Test
    public void shouldPassValidationIfAPIKeyIsValid() throws NoSuchFieldException {
        testObject.setApi_key(remediProperties.getApiKey());
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
