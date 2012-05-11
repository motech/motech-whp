package org.motechproject.whp.integration.validation.patient;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.AllTests;
import org.mockito.internal.matchers.Contains;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.builder.PatientWebRequestBuilder;
import org.motechproject.whp.patient.domain.Provider;
import org.motechproject.whp.patient.exception.WHPException;
import org.motechproject.whp.patient.repository.AllProviders;
import org.motechproject.whp.patient.repository.SpringIntegrationTest;
import org.motechproject.whp.request.PatientWebRequest;
import org.motechproject.whp.validation.RequestValidator;
import org.motechproject.whp.validation.ValidationScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(locations = "classpath*:META-INF/spring/applicationContext.xml")
public abstract class BasePatientTest extends SpringIntegrationTest {
    @Rule
    public ExpectedException exceptionThrown = ExpectedException.none();

    @Autowired
    protected RequestValidator validator;

    @Autowired
    AllProviders allProviders;

    @Before
    public void setUpDefaultProvider() {
        PatientWebRequest patientWebRequest = new PatientWebRequestBuilder().withDefaults().build();
        String defaultProviderId = patientWebRequest.getProvider_id();
        Provider defaultProvider = new Provider(defaultProviderId, "1234567890", "chambal", DateUtil.now());
        allProviders.add(defaultProvider);
    }

    protected void expectWHPException(String message) {
        exceptionThrown.expect(WHPException.class);
        exceptionThrown.expectMessage(new Contains(message));
    }

    protected void validate(PatientWebRequest patientWebRequest) {
        validator.validate(patientWebRequest, ValidationScope.create);
    }

    @After
    public void tearDown() {
        markForDeletion(allProviders.getAll().toArray());
    }
}
