package org.motechproject.whp.functional.test.provider;

import junit.framework.Assert;
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

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/applicationFunctionalTestContext.xml")
public class ListAllProvidersTest extends BaseTest {

    ProviderDataService providerDataService;

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
        providerDataService = new ProviderDataService(webDriver);
        provider1 = providerDataService.createProvider("Begusarai");
        provider2 = providerDataService.createProvider("Saharsa");
        provider3 = providerDataService.createProvider("Saharsa");
    }

    @Test
    public void shouldListProvidersForDistrict() {
        listProvidersPage.searchBy("Saharsa", "");
        assertFalse(listProvidersPage.hasProviderRow(provider1.getProviderId().toLowerCase()));
        assertTrue(listProvidersPage.hasProviderRow(provider2.getProviderId().toLowerCase()));
        assertTrue(listProvidersPage.hasProviderRow(provider3.getProviderId().toLowerCase()));
    }

    @Test
    public void shouldListProvidersForDistrictAndProviderId() {
        listProvidersPage.searchBy("Saharsa", provider2.getProviderId());
        assertFalse(listProvidersPage.hasProviderRow(provider1.getProviderId().toLowerCase()));
        assertTrue(listProvidersPage.hasProviderRow(provider2.getProviderId().toLowerCase()));
        assertFalse(listProvidersPage.hasProviderRow(provider3.getProviderId().toLowerCase()));
    }

    @Test
    public void shouldDisplayWarningForDistrict_WhenNoProvidersFound() {
        listProvidersPage.searchBy("Vaishali", "");
        Assert.assertEquals("No providers found for District: 'Vaishali'", listProvidersPage.getWarningText());
    }

    @Test
    public void shouldDisplayWarningForDistrictAndProviderId_WhenNoProvidersFound() {
        listProvidersPage.searchBy("Saharsa", provider1.getProviderId());
        Assert.assertEquals("No providers found for District: 'Saharsa' with provider ID: '" + provider1.getProviderId().toLowerCase() + "'", listProvidersPage.getWarningText());
    }

    private ListProvidersPage loginAsItAdmin() {
        return MyPageFactory.initElements(webDriver, LoginPage.class).loginWithItAdminUserNamePassword("itadmin1", "password").navigateToSearchProviders();
    }

}
