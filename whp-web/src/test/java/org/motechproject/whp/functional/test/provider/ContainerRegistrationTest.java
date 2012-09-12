package org.motechproject.whp.functional.test.provider;

import org.junit.Test;
import org.motechproject.whp.functional.data.TestProvider;
import org.motechproject.whp.functional.framework.BaseTest;
import org.motechproject.whp.functional.page.provider.ProviderPage;
import org.motechproject.whp.functional.service.ProviderDataService;

import static org.motechproject.whp.functional.page.Page.getLoginPage;

public class ContainerRegistrationTest extends BaseTest {

    private ProviderPage providerPage;

    private TestProvider provider1;

    @Override
    public void setUp() {
        super.setUp();
        setupProvider();
        providerPage = getLoginPage(webDriver).loginAsProvider(provider1.getProviderId(), "password");
    }

    public void setupProvider() {
        ProviderDataService providerDataService = new ProviderDataService(webDriver);
        provider1 = providerDataService.createProvider("Begusarai");
        providerDataService.activateProvider(provider1.getProviderId());
    }

    @Test
    public void shouldRegisterAContainer() {
        providerPage.registerContainer();
    }
}
