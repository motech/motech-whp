package org.motechproject.whp.integration.validation.provider;

import org.junit.After;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.mockito.internal.matchers.Contains;
import org.motechproject.whp.patient.exception.WHPException;
import org.motechproject.whp.patient.repository.AllProviders;
import org.motechproject.whp.patient.repository.SpringIntegrationTest;
import org.motechproject.whp.validation.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(locations = "classpath*:META-INF/spring/applicationContext.xml")
public abstract class BaseProviderTest extends SpringIntegrationTest {
    @Rule
    public ExpectedException exceptionThrown = ExpectedException.none();

    @Autowired
    protected RequestValidator validator;

    @Autowired
    AllProviders allProviders;

    protected void expectWHPException(String message) {
        exceptionThrown.expect(WHPException.class);
        exceptionThrown.expectMessage(new Contains(message));
    }

    @After
    public void tearDown() {
        markForDeletion(allProviders.getAll().toArray());
    }
}
