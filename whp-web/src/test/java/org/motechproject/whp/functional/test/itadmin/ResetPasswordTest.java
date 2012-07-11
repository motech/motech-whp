package org.motechproject.whp.functional.test.itadmin;

import org.junit.Test;
import org.motechproject.whp.functional.data.TestCmfAdmin;
import org.motechproject.whp.functional.data.TestProvider;
import org.motechproject.whp.functional.framework.BaseTest;
import org.motechproject.whp.functional.page.LoginPage;
import org.motechproject.whp.functional.page.Page;
import org.motechproject.whp.functional.page.admin.ListAllCmfAdminsPage;
import org.motechproject.whp.functional.page.admin.ListProvidersPage;
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


    @Override
    public void setUp() {
        super.setUp();
        providerDataService = new ProviderDataService(webDriver);
    }

    @Test
    public void shouldExposeResetPasswordLinkForActiveProvidersOnly() throws InterruptedException {
        TestProvider inActiveProvider = createInactiveProvider();
        TestProvider activeProvider = createInactiveProvider();

        ListProvidersPage listProvidersPage = loginAsItAdmin(webDriver)
                .activateProvider(activeProvider.getProviderId(), OLD_PASSWORD_FOR_ACTIVE_PROVIDER);

        assertTrue(listProvidersPage.hasResetPasswordLink(activeProvider.getProviderId()));
        assertFalse(listProvidersPage.hasResetPasswordLink(inActiveProvider.getProviderId()));
    }

    @Test
    public void shouldResetPasswordForProvider_uponConfirmation() throws InterruptedException {
        TestProvider activeProvider = createInactiveProvider();
        ListProvidersPage listProvidersPage = loginAsItAdmin(webDriver)
                .activateProvider(activeProvider.getProviderId(), OLD_PASSWORD_FOR_ACTIVE_PROVIDER);

        assertResetPasswordForProvider(listProvidersPage, activeProvider);
    }

    @Test
    public void shouldNotResetPasswordForProvider_uponCancellation() throws InterruptedException {
        TestProvider activeProvider = createInactiveProvider();
        LoginPage loginPage = loginAsItAdmin(webDriver)
                .activateProvider(activeProvider.getProviderId(), OLD_PASSWORD_FOR_ACTIVE_PROVIDER)
                .openResetPasswordModal(activeProvider.getProviderId()).cancelResetPasswordDialog()
                .logout();

        loginPage.loginAsProvider(activeProvider.getProviderId(), OLD_PASSWORD_FOR_ACTIVE_PROVIDER).logout();
    }

    @Test
    public void shouldResetPasswordForProviderUponActivation_whenNoActiveProviderExists() throws InterruptedException {
        //Assuming provider with district Nalanda is not created by any other tests; clean DB to run consecutively
        loginAsItAdmin(webDriver).searchBy(DISTRICT_NALANDA, "", false).logout();

        TestProvider provider = providerDataService.createProvider(DISTRICT_NALANDA);
        ListProvidersPage listProvidersPage = loginAsItAdmin(webDriver).searchBy(DISTRICT_NALANDA, "", true);

        assertFalse(listProvidersPage.hasResetPasswordLink(provider.getProviderId()));

        listProvidersPage.activateProvider(provider.getProviderId(), "newPassword");

        assertResetPasswordForProvider(listProvidersPage, provider);
    }

    @Test
    public void shouldResetPasswordForCmfAdmin_uponConfirmation() throws InterruptedException {
        TestCmfAdmin testCmfAdmin = loginAsItAdmin(webDriver)
                .navigateToSearchCmfAdmins()
                .navigateToCreateCmfAdminPage().createCmfAdmin();

        ListAllCmfAdminsPage listAllCmfAdminsPage = Page.getListAllCmfAdminsPage(webDriver);

        assertResetPasswordForCmfAdmin(listAllCmfAdminsPage, testCmfAdmin);
    }

    @Test
    public void shouldNotResetPasswordForCmfAdmin_uponCancellation() throws InterruptedException {
        TestCmfAdmin cmfAdmin = loginAsItAdmin(webDriver)
                .navigateToSearchCmfAdmins()
                .navigateToCreateCmfAdminPage().createCmfAdmin();

        ListAllCmfAdminsPage listAllCmfAdminsPage = Page.getListAllCmfAdminsPage(webDriver);

        LoginPage loginPage = listAllCmfAdminsPage.openResetPasswordDialog(cmfAdmin.getUserId())
                .cancelResetPasswordDialog()
                .logout();

        loginPage.loginAsCmfAdminWith(cmfAdmin.getUserId(), cmfAdmin.getPassword()).logout();
    }

    private TestProvider createInactiveProvider() {
        return providerDataService.createProvider(DISTRICT_BEGUSARAI);
    }

    private void assertResetPasswordForProvider(ListProvidersPage listProvidersPage, TestProvider activeProvider) {
        LoginPage loginPage = listProvidersPage.resetPassword(activeProvider.getProviderId())
                .logout();

        loginPage.loginAsProvider(activeProvider.getProviderId(), DEFAULT_PASSWORD).logout();
    }

    private void assertResetPasswordForCmfAdmin(ListAllCmfAdminsPage listAllCmfAdminsPage, TestCmfAdmin cmfAdmin) {
        LoginPage loginPage = listAllCmfAdminsPage.resetPassword(cmfAdmin.getUserId())
                .logout();

        loginPage.loginAsCmfAdminWith(cmfAdmin.getUserId(), cmfAdmin.getPassword()).logout();
    }

}
