package org.motechproject.whp.functional.test.provider;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.whp.functional.data.TestProvider;
import org.motechproject.whp.functional.framework.BaseTest;
import org.motechproject.whp.functional.framework.MyPageFactory;
import org.motechproject.whp.functional.page.ListProvidersPage;
import org.motechproject.whp.functional.page.LoginPage;
import org.motechproject.whp.functional.service.ProviderDataService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/applicationFunctionalTestContext.xml")
public class ListAllProvidersTest extends BaseTest {

    ProviderDataService providerDataService;

    List<TestProvider> testProviders = new ArrayList<TestProvider>();
    private ListProvidersPage providerPage;

    @Override
    public void setUp() {
        super.setUp();
        setupProvider();
        providerPage = loginAsItAdmin();
    }

    public void setupProvider() {
        providerDataService = new ProviderDataService(webDriver);
        testProviders.add(providerDataService.createProvider());
        testProviders.add(providerDataService.createProvider());
        testProviders.add(providerDataService.createProvider());
    }

    @Test
    public void shouldLoginAsItAdminAndListAllProviders() {
        for(TestProvider testProvider : testProviders) {
            verifyProviderRow(testProvider);
        }
    }

    private void verifyProviderRow(TestProvider testProvider) {
        assertTrue(providerPage.hasProvider(testProvider.getProviderId()));
    }

    private ListProvidersPage loginAsItAdmin() {
        return MyPageFactory.initElements(webDriver, LoginPage.class).loginWithItAdminUserNamePassword("itadmin1", "password").navigateToSearchProviders();
    }

    @After
    public void tearDown() throws IOException {
        super.tearDown();
    }
}
