package org.motechproject.whp.it.remedi.inbound.request.provider;

import org.junit.After;
import org.motechproject.whp.common.util.SpringIntegrationTest;
import org.motechproject.whp.user.repository.AllProviders;
import org.motechproject.whp.common.validation.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(locations = "classpath*:/applicationWebServiceContext.xml")
public abstract class BaseProviderIT extends SpringIntegrationTest {

    @Autowired
    protected RequestValidator validator;

    @Autowired
    AllProviders allProviders;

    @After
    public void tearDown() {
        markForDeletion(allProviders.getAll().toArray());
    }

}
