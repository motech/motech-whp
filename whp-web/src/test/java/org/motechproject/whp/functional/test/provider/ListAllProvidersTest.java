package org.motechproject.whp.functional.test.provider;

import org.junit.Ignore;
import org.junit.Test;
import org.motechproject.whp.functional.builder.ListAllProviderPageBuilder;
import org.motechproject.whp.functional.data.TestProvider;
import org.motechproject.whp.functional.framework.BaseTest;
import org.motechproject.whp.functional.page.LoginPage;
import org.motechproject.whp.functional.page.admin.listprovider.ListProvidersPage;
import org.motechproject.whp.functional.service.ProviderDataService;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.motechproject.whp.functional.page.LoginPage.loginAsItAdmin;

public class ListAllProvidersTest extends BaseTest {

    private TestProvider provider1;
    private TestProvider provider2;
    private TestProvider provider3;

    private ListAllProviderPageBuilder builder;

    @Override
    public void setUp() {
        super.setUp();
        setupProvider();
        loginAsItAdmin(webDriver);
        builder = new ListAllProviderPageBuilder(webDriver);
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
        builder.build(provider1.getProviderId(), true).searchBy("Saharsa", "", true);
        assertFalse(builder.build(provider1.getProviderId(), true).hasProviderRow());
        assertTrue(builder.build(provider2.getProviderId(), true).hasProviderRow());
        assertTrue(builder.build(provider3.getProviderId(), true).hasProviderRow());
    }

    @Test
    public void shouldListProvidersForDistrictAndProviderId() {
        builder.build(provider2.getProviderId(), true).searchBy("Saharsa", provider2.getProviderId(), true);
        assertFalse(builder.build(provider1.getProviderId(), false).hasProviderRow());
        assertTrue(builder.build(provider2.getProviderId(), false).hasProviderRow());
        assertFalse(builder.build(provider3.getProviderId(), false).hasProviderRow());
    }

    @Test
    public void shouldDisplayWarningForDistrict_WhenNoProvidersFound() {
        ListProvidersPage page = builder.build(provider2.getProviderId(), true);
        page.searchBy("Bhagalpur", "", false);
        assertEquals("No Providers found for District 'Bhagalpur'", page.getWarningText());
    }

    @Test
    public void shouldDisplayWarningForDistrictAndProviderId_WhenNoProvidersFound() {
        ListProvidersPage page = builder.build(provider2.getProviderId(), true);
        page.searchBy("Saharsa", provider1.getProviderId(), false);
        assertEquals("No Providers found for District 'Saharsa' with provider ID: '" + provider1.getProviderId() + "'", page.getWarningText());
    }

    @Test
    public void shouldActivateProviderIfNotActive() throws InterruptedException {
        assertTrue((builder.build(provider1.getProviderId(), true).isProviderActive()));
        builder.build(provider2.getProviderId(), true).searchBy("Saharsa", "", true);

        //checking hasActiveStatus and hasActivateButton for inactive as isProviderActive will return false even if either of these returns false
        assertFalse(builder.build(provider2.getProviderId(), true).isProviderActive());
        assertTrue(builder.build(provider2.getProviderId(), true).hasActivateButton());
        assertFalse(builder.build(provider2.getProviderId(), true).hasActiveStatus());

        assertFalse(builder.build(provider3.getProviderId(), true).isProviderActive());
        assertTrue(builder.build(provider3.getProviderId(), true).hasActivateButton());
        assertFalse(builder.build(provider3.getProviderId(), true).hasActiveStatus());

        String provider2Password = "passwordForProvider2";
        System.out.println("Activating provider :" + provider2.getProviderId());
        builder.build(provider2.getProviderId(), true).activateProvider(provider2Password);

        LoginPage loginPage = builder.build(provider2.getProviderId(), true).logout();
        loginPage.loginAsProvider(provider2.getProviderId(), provider2Password).logout();
    }

    @Test
    public void testValidationsForResetPasswordDialog() throws InterruptedException {
        builder.build(provider2.getProviderId(), true).searchBy("Saharsa", "", true);
        String passwordForProvider2 = "passwordForProvider2";

        builder.build(provider2.getProviderId(), true)
                .openActivateProviderModal()
                .validateEmptyPasswordOnActivation()
                .validatePasswordLengthUponActivation()
                .validateConfirmPasswordUponActivation(passwordForProvider2)
                .validateValidPasswordUponActivation(passwordForProvider2)
                .closeProviderActivationModal();

        assertFalse(builder.build(provider2.getProviderId(), true).isProviderActive());
        assertTrue(builder.build(provider2.getProviderId(), true).hasActivateButton());
        assertFalse(builder.build(provider2.getProviderId(), true).hasActiveStatus());

        LoginPage loginPage = builder.build(provider2.getProviderId(), true).logout();
        loginPage.loginWithInActiveProviderId(provider2.getProviderId(), passwordForProvider2);
    }

}
