package org.motechproject.whp.functional.test.provider;

import org.junit.Test;
import org.motechproject.whp.functional.data.TestProvider;
import org.motechproject.whp.functional.framework.BaseTest;
import org.motechproject.whp.functional.page.LoginPage;
import org.motechproject.whp.functional.page.admin.ListProvidersPage;
import org.motechproject.whp.functional.service.ProviderDataService;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.motechproject.whp.functional.page.LoginPage.loginAsItAdmin;

public class ListAllProvidersTest extends BaseTest {

    private ListProvidersPage listProvidersPage;

    private TestProvider provider1;
    private TestProvider provider2;
    private TestProvider provider3;

    @Override
    public void setUp() {
        super.setUp();
        setupProvider();
        listProvidersPage = loginAsItAdmin(webDriver).navigateToSearchProviders();
    }

    public void setupProvider() {
        ProviderDataService providerDataService = new ProviderDataService(webDriver);
        provider1 = providerDataService.createProvider("Begusarai");
        provider2 = providerDataService.createProvider("Saharsa");
        provider3 = providerDataService.createProvider("Saharsa");
        providerDataService.activateProvider(provider1.getProviderId());
    }

    @Test
    public void shouldListProvidersForDistrict() {
        listProvidersPage.searchBy("Saharsa", "", true);
        assertFalse(listProvidersPage.hasProviderRow(provider1.getProviderId().toLowerCase()));
        assertTrue(listProvidersPage.hasProviderRow(provider2.getProviderId().toLowerCase()));
        assertTrue(listProvidersPage.hasProviderRow(provider3.getProviderId().toLowerCase()));
    }

    @Test
    public void shouldListProvidersForDistrictAndProviderId() {
        listProvidersPage.searchBy("Saharsa", provider2.getProviderId(), true);
        assertFalse(listProvidersPage.hasProviderRow(provider1.getProviderId().toLowerCase()));
        assertTrue(listProvidersPage.hasProviderRow(provider2.getProviderId().toLowerCase()));
        assertFalse(listProvidersPage.hasProviderRow(provider3.getProviderId().toLowerCase()));
    }

    @Test
    public void shouldDisplayWarningForDistrict_WhenNoProvidersFound() {
        listProvidersPage.searchBy("Bhagalpur", "", false);
        assertEquals("No providers found for District: 'Bhagalpur'", listProvidersPage.getWarningText());
    }

    @Test
    public void shouldDisplayWarningForDistrictAndProviderId_WhenNoProvidersFound() {
        listProvidersPage.searchBy("Saharsa", provider1.getProviderId(), false);
        assertEquals("No providers found for District: 'Saharsa' with provider ID: '" + provider1.getProviderId().toLowerCase() + "'", listProvidersPage.getWarningText());
    }
    @Test
    public void shouldActivateProviderIfNotActive() throws InterruptedException {
        assertTrue(listProvidersPage.isProviderActive(provider1.getProviderId()));
        listProvidersPage.searchBy("Saharsa", "", true);

        //checking hasActiveStatus and hasActivateButton for inactive as isProviderActive will return false even if either of these returns false
        assertFalse(listProvidersPage.isProviderActive(provider2.getProviderId()));
        assertTrue(listProvidersPage.hasActivateButton(provider2.getProviderId()));
        assertFalse(listProvidersPage.hasActiveStatus(provider2.getProviderId()));

        assertFalse(listProvidersPage.isProviderActive(provider3.getProviderId()));
        assertTrue(listProvidersPage.hasActivateButton(provider3.getProviderId()));
        assertFalse(listProvidersPage.hasActiveStatus(provider3.getProviderId()));

        String provider2Password = "passwordForProvider2";
        System.out.println("Activating provider :" + provider2.getProviderId());
        listProvidersPage.activateProvider(provider2.getProviderId(),provider2Password);
        assertTrue(listProvidersPage.isProviderActive(provider2.getProviderId()));

        LoginPage loginPage = listProvidersPage.logout();
        loginPage.loginAsProvider(provider2.getProviderId(), provider2Password).logout();
    }

    @Test
    public void testValidationsForResetPasswordDialog() throws InterruptedException {
        listProvidersPage.searchBy("Saharsa", "", true);
        String passwordForProvider2 = "passwordForProvider2";

        listProvidersPage.openActivateProviderModal(provider2.getProviderId())
                .validateEmptyPasswordOnActivation()
                .validatePasswordLengthUponActivation()
                .validateConfirmPasswordUponActivation(passwordForProvider2)
                .validateValidPasswordUponActivation(passwordForProvider2)
                .closeProviderActivationModal();

        assertFalse(listProvidersPage.isProviderActive(provider2.getProviderId()));
        assertFalse(listProvidersPage.hasActiveStatus(provider2.getProviderId()));
        assertTrue(listProvidersPage.hasActivateButton(provider2.getProviderId()));

        LoginPage loginPage = listProvidersPage.logout();
        loginPage.loginWithInActiveProviderId(provider2.getProviderId(),passwordForProvider2);
    }

}
