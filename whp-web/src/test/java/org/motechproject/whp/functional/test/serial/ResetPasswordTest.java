package org.motechproject.whp.functional.test.serial;

import org.junit.Test;
import org.motechproject.whp.functional.data.TestProvider;
import org.motechproject.whp.functional.framework.BaseTest;
import org.motechproject.whp.functional.page.LoginPage;
import org.motechproject.whp.functional.page.admin.ListProvidersPage;
import org.motechproject.whp.functional.page.provider.ProviderPage;
import org.motechproject.whp.functional.service.ProviderDataService;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.motechproject.whp.functional.page.LoginPage.loginAsItAdmin;

public class ResetPasswordTest extends BaseTest {
    private ListProvidersPage listProvidersPage;

    private TestProvider activeProvider;
    private TestProvider inActiveProvider;
    private ProviderDataService providerDataService;

    private final String DEFAULT_PASSWORD = "password";
    private final String OLD_PASSWORD_FOR_ACTIVE_PROVIDER = "oldPassword";

    @Override
    public void setUp() {
        super.setUp();
        try {
            setupProvider();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setupProvider() throws InterruptedException {
        providerDataService = new ProviderDataService(webDriver);
        activeProvider = providerDataService.createProvider("Begusarai");
        inActiveProvider = providerDataService.createProvider("Begusarai");

        listProvidersPage = loginAsItAdmin(webDriver).navigateToSearchProviders();
        listProvidersPage.activateProvider(activeProvider.getProviderId(), OLD_PASSWORD_FOR_ACTIVE_PROVIDER);
    }

    @Test
    public void shouldExposeResetPasswordLinkForActiveProvidersOnly() {
        assertTrue(listProvidersPage.hasResetPasswordLink(activeProvider.getProviderId()));
        assertFalse(listProvidersPage.hasResetPasswordLink(inActiveProvider.getProviderId()));
    }

    @Test
    public void shouldResetPasswordForProvider_uponConfirmation() throws InterruptedException {
        assertResetPasswordForProvider(activeProvider);
    }

    @Test
    public void shouldResetPasswordForProviderUponActivation_whenNoActiveProviderExists() throws InterruptedException {
        //Assuming provider with district Nalanda is not created by any other tests; clean DB to run consecutively
        listProvidersPage.searchBy("Nalanda", "", false);

        TestProvider provider = providerDataService.createProvider("Nalanda");
        loginAsItAdmin(webDriver);
        listProvidersPage.searchBy("Nalanda", "", true);
        assertFalse(listProvidersPage.hasResetPasswordLink(provider.getProviderId()));
        listProvidersPage.activateProvider(provider.getProviderId(),"newPassword");
        assertResetPasswordForProvider(provider);
    }

    private void assertResetPasswordForProvider(TestProvider activeProvider) {
        listProvidersPage.resetPassword(activeProvider.getProviderId());
        listProvidersPage.logout();
        ProviderPage providerPage = loginAsProvider(activeProvider.getProviderId(), DEFAULT_PASSWORD);
        providerPage.logout();
    }

    @Test
    public void shouldNotResetPasswordForProvider_uponCancellation() throws InterruptedException {
        listProvidersPage.openResetPasswordModal(activeProvider.getProviderId()).cancelResetPasswordDialog();
        listProvidersPage.logout();
        ProviderPage providerPage = loginAsProvider(activeProvider.getProviderId(), OLD_PASSWORD_FOR_ACTIVE_PROVIDER);
        providerPage.logout();
    }

    private ProviderPage loginAsProvider(String providerId, String password) {
        return LoginPage.fetch(webDriver).loginAsProvider(providerId, password);
    }
}
