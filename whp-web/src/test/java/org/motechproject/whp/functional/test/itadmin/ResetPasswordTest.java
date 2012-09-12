package org.motechproject.whp.functional.test.itadmin;

import org.junit.Ignore;
import org.junit.Test;
import org.motechproject.whp.functional.builder.ListAllProviderPageBuilder;
import org.motechproject.whp.functional.data.TestProvider;
import org.motechproject.whp.functional.framework.BaseTest;
import org.motechproject.whp.functional.page.LoginPage;
import org.motechproject.whp.functional.page.admin.listprovider.ListProvidersPage;
import org.motechproject.whp.functional.service.ProviderDataService;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.motechproject.whp.functional.page.LoginPage.loginAsItAdmin;

public class ResetPasswordTest extends BaseTest {

    private ProviderDataService providerDataService;

    private final String DEFAULT_PASSWORD = "password";
    private final String OLD_PASSWORD_FOR_ACTIVE_PROVIDER = "oldPassword";
    private final String DISTRICT_NALANDA = "Nalanda";
    private final String DISTRICT_BEGUSARAI = "Begusarai";

    private ListAllProviderPageBuilder builder;

    @Override
    public void setUp() {
        super.setUp();
        builder = new ListAllProviderPageBuilder(webDriver);
        providerDataService = new ProviderDataService(webDriver);
    }

    @Test
    public void shouldExposeResetPasswordLinkForActiveProvidersOnly() throws InterruptedException {
        TestProvider inActiveProvider = createInactiveProvider();
        TestProvider activeProvider = createInactiveProvider();

        loginAsItAdmin(webDriver);
        ListProvidersPage listProvidersPage = builder.build(activeProvider.getProviderId(), true).activateProvider(OLD_PASSWORD_FOR_ACTIVE_PROVIDER);

        assertTrue(builder.build(activeProvider.getProviderId(), true).hasResetPasswordLink());
        assertFalse(builder.build(inActiveProvider.getProviderId(), true).hasResetPasswordLink());
    }

    @Test
    public void shouldResetPasswordForProvider_uponConfirmation() throws InterruptedException {
        TestProvider activeProvider = createInactiveProvider();

        loginAsItAdmin(webDriver);
        ListProvidersPage listProvidersPage = builder.build(activeProvider.getProviderId(), true).activateProvider(OLD_PASSWORD_FOR_ACTIVE_PROVIDER);

        assertResetPasswordForProvider(activeProvider);
    }

    @Test
    public void shouldNotResetPasswordForProvider_uponCancellation() throws InterruptedException {
        TestProvider activeProvider = createInactiveProvider();

        loginAsItAdmin(webDriver);
        LoginPage loginPage = builder.build(activeProvider.getProviderId(), true).activateProvider(OLD_PASSWORD_FOR_ACTIVE_PROVIDER)
                .openResetPasswordModal().cancelResetPasswordDialog()
                .logout();

        loginPage.loginAsProvider(activeProvider.getProviderId(), OLD_PASSWORD_FOR_ACTIVE_PROVIDER).logout();
    }


    private TestProvider createInactiveProvider() {
        return providerDataService.createProvider(DISTRICT_BEGUSARAI);
    }

    private void assertResetPasswordForProvider(TestProvider activeProvider) {
        LoginPage loginPage = builder.build(activeProvider.getProviderId(), true).resetPassword()
                .logout();

        loginPage.loginAsProvider(activeProvider.getProviderId(), DEFAULT_PASSWORD).logout();
    }


}
