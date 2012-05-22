package org.motechproject.whp.validation;

import lombok.Data;
import org.ektorp.CouchDbConnector;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.testing.utils.SpringIntegrationTest;
import org.motechproject.validation.constraints.NamedConstraint;
import org.motechproject.whp.patient.exception.WHPCaseException;
import org.motechproject.whp.patient.exception.WHPRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Properties;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:META-INF/spring/applicationContext.xml")
public class APIKeyValidatorTest extends SpringIntegrationTest {

    @Autowired
    @Qualifier("whpAPIValidationProperty")
    private Properties whpAPIValidationProperty;

    @Autowired
    private RequestValidator requestValidator;

    @Autowired
    private CouchDbConnector whpDbConnector;

    @Test
    @DirtiesContext
    public void shouldPassValidationIfAPIKeyIsValid() {
        whpAPIValidationProperty.setProperty("remedi.api.key", "remediAPIKey");

        TestClass testObject = new TestClass();
        testObject.setApi_key("remediAPIKey");

        requestValidator.validate(testObject, ValidationScope.create);
    }

    @Test(expected = WHPRuntimeException.class)
    @DirtiesContext
    public void shouldFailValidationIfAPIKeyIsInvalid() {
        whpAPIValidationProperty.setProperty("remedi.api.key", "remediAPIKey");

        TestClass testObject = new TestClass();
        testObject.setApi_key("invalidAPIKey");

        requestValidator.validate(testObject, ValidationScope.create);
    }

    @Override
    public CouchDbConnector getDBConnector() {
        return whpDbConnector;
    }

    @Data
    public static class TestClass {
        @NamedConstraint(name = APIKeyValidator.API_KEY_VALIDATION)
        private String api_key;
    }

}
