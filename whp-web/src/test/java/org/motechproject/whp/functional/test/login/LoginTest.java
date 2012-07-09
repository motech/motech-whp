package org.motechproject.whp.functional.test.login;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.functional.data.TestProvider;
import org.motechproject.whp.functional.framework.BaseTest;
import org.motechproject.whp.functional.framework.MyPageFactory;
import org.motechproject.whp.functional.page.admin.AdminPage;
import org.motechproject.whp.functional.page.LoginPage;
import org.motechproject.whp.functional.page.provider.ProviderPage;
import org.motechproject.whp.functional.service.ProviderDataService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LoginTest extends BaseTest {

    ProviderDataService providerDataService;

    @Before
    public void setup() {
        providerDataService = new ProviderDataService(webDriver);
    }

    @Test
    public void testLoginFailure() {
        LoginPage page = MyPageFactory.initElements(webDriver, LoginPage.class).loginWithIncorrectAdminUserNamePassword();
        assertEquals(LoginPage.FAILURE_MESSAGE, page.errorMessage());
    }

    @Test
    public void testLoginSuccessForAdministrator() {
        AdminPage adminPage = MyPageFactory.initElements(webDriver, LoginPage.class).loginWithCorrectAdminUserNamePassword();
        assertTrue(StringUtils.containsIgnoreCase(adminPage.getWelcomeText(), "admin"));
        adminPage.logout();
    }

    @Test
    public void testLoginSuccessForProvider() {
        TestProvider provider = providerDataService.createProvider();
        providerDataService.activateProvider(provider.getProviderId());
        LoginPage loginPage = MyPageFactory.initElements(webDriver, LoginPage.class);
        ProviderPage providerPage = loginPage.loginAsProvider(provider.getProviderId(), provider.getPassword());
        assertTrue(StringUtils.containsIgnoreCase(providerPage.getWelcomeText(), provider.getProviderId()));
        providerPage.logout();
    }

}
