package org.motechproject.whp.functional;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.functional.data.TestProvider;
import org.motechproject.whp.functional.framework.BaseTest;
import org.motechproject.whp.functional.framework.MyPageFactory;
import org.motechproject.whp.functional.page.LoginPage;
import org.motechproject.whp.functional.page.ProviderPage;
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
        ProviderPage providerPage = MyPageFactory.initElements(webDriver, LoginPage.class).loginWithCorrectAdminUserNamePassword();
        assertTrue(StringUtils.contains(providerPage.getWelcomeText(), "admin"));
        providerPage.logout();
    }

    @Test
    public void testLoginSuccessForProvider() {
        TestProvider provider = providerDataService.createProvider();
        LoginPage loginPage = MyPageFactory.initElements(webDriver, LoginPage.class);
        ProviderPage providerPage = loginPage.loginWithProviderUserNamePassword(provider.getProviderId(), provider.getPassword());
        assertTrue(StringUtils.contains(providerPage.getWelcomeText(), provider.getProviderId()));
        providerPage.logout();
    }
}
