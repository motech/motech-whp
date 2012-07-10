package org.motechproject.whp.functional.test.serial;

import org.junit.Test;
import org.motechproject.whp.functional.data.TestProvider;
import org.motechproject.whp.functional.framework.BaseTest;
import org.motechproject.whp.functional.framework.MyPageFactory;
import org.motechproject.whp.functional.page.LoginPage;
import org.motechproject.whp.functional.page.admin.ListAllCmfAdminsPage;
import org.motechproject.whp.functional.page.admin.ListProvidersPage;
import org.motechproject.whp.functional.service.ProviderDataService;

import static org.junit.Assert.assertTrue;

public class ResetPasswordTest extends BaseTest {
    private ListProvidersPage listProvidersPage;
    private ListAllCmfAdminsPage listAllCmfAdminsPage;

    private TestProvider provider1;

    @Override
    public void setUp() {
        super.setUp();
        setupProvider();
        listProvidersPage = loginAsItAdmin();
    }

    public void setupProvider() {
        ProviderDataService providerDataService = new ProviderDataService(webDriver);
        provider1 = providerDataService.createProvider("Begusarai");
        providerDataService.activateProvider(provider1.getProviderId());
    }

    @Test
    public void shouldExposeResetPasswordLinkForProviderRows(){
        assertTrue(listProvidersPage.hasResetPasswordButton(provider1.getProviderId()));
    }

    private ListProvidersPage loginAsItAdmin() {
        return MyPageFactory.initElements(webDriver, LoginPage.class).loginWithItAdminUserNamePassword("itadmin1", "password").navigateToSearchProviders();
    }
}
