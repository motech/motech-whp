package org.motechproject.whp.functional.test.provider;

import junit.framework.Assert;
import org.junit.Test;
import org.motechproject.whp.functional.data.TestProvider;
import org.motechproject.whp.functional.framework.BaseTest;
import org.motechproject.whp.functional.framework.MyPageFactory;
import org.motechproject.whp.functional.page.admin.ListProvidersPage;
import org.motechproject.whp.functional.page.LoginPage;
import org.motechproject.whp.functional.service.ProviderDataService;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ListAllProvidersTest extends BaseTest {

    private ListProvidersPage listProvidersPage;

    private TestProvider provider1;
    private TestProvider provider2;
    private TestProvider provider3;

    @Override
    public void setUp() {
        super.setUp();
        setupProvider();
        listProvidersPage = loginAsItAdmin();
    }

    public void setupProvider() {
        ProviderDataService providerDataService = new ProviderDataService(webDriver);
        provider1 = providerDataService.createProvider("Begusarai");
        provider2 = providerDataService.createProvider("Saharsa");
        provider3 = providerDataService.createProvider("Saharsa");
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
        listProvidersPage.searchBy("Vaishali", "", false);
        Assert.assertEquals("No providers found for District: 'Vaishali'", listProvidersPage.getWarningText());
    }

    @Test
    public void shouldDisplayWarningForDistrictAndProviderId_WhenNoProvidersFound() {
        listProvidersPage.searchBy("Saharsa", provider1.getProviderId(), false);
        Assert.assertEquals("No providers found for District: 'Saharsa' with provider ID: '" + provider1.getProviderId().toLowerCase() + "'", listProvidersPage.getWarningText());
    }

    private ListProvidersPage loginAsItAdmin() {
        return MyPageFactory.initElements(webDriver, LoginPage.class).loginWithItAdminUserNamePassword("itadmin1", "password").navigateToSearchProviders();
    }

}
