package org.motechproject.whp.integration.validation.provider;

import org.junit.After;
import org.motechproject.whp.patient.repository.AllProviders;
import org.motechproject.whp.patient.repository.SpringIntegrationTest;
import org.motechproject.whp.validation.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(locations = "classpath*:META-INF/spring/applicationContext.xml")
public abstract class BaseProviderTest extends SpringIntegrationTest {

    @Autowired
    protected RequestValidator validator;

    @Autowired
    AllProviders allProviders;

    @After
    public void tearDown() {
        markForDeletion(allProviders.getAll().toArray());
    }

}
