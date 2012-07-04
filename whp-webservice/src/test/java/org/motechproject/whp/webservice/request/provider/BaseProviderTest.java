package org.motechproject.whp.webservice.request.provider;

import org.junit.After;
import org.motechproject.whp.common.utils.SpringIntegrationTest;
import org.motechproject.whp.user.repository.AllProviders;
import org.motechproject.whp.validation.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(locations = "classpath*:/applicationWebServiceContext.xml")
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