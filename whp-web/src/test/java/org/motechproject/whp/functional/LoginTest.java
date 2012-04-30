package org.motechproject.whp.functional;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.functional.framework.BaseTest;
import org.motechproject.whp.functional.framework.MyPageFactory;
import org.motechproject.whp.functional.page.LoginPage;
import org.motechproject.whp.functional.page.PatientCreatePage;
import org.motechproject.whp.functional.page.ProviderCreatePage;
import org.motechproject.whp.functional.page.ProviderPage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LoginTest extends BaseTest {

    private ProviderCreatePage providerCreatePage;
    private PatientCreatePage patientCreatePage;

    @Test
    public void testLoginFailure() {
        LoginPage page = MyPageFactory.initElements(webDriver, LoginPage.class).loginWithIncorrectAdminUserNamePassword();
        assertEquals(LoginPage.FAILURE_MESSAGE, page.errorMessage());
    }

    @Test
    public void testLoginSuccessForAdministrator() {
        ProviderPage providerPage = MyPageFactory.initElements(webDriver, LoginPage.class).loginWithCorrectAdminUserNamePassword();
        assertTrue(StringUtils.contains(providerPage.getWelcomeText(), "Welcome admin"));
        providerPage.logout();
    }

    @Test
    public void testLoginSuccessForProvider() {
        providerCreatePage = ProviderCreatePage.fetch(webDriver);
        LoginPage loginPage = providerCreatePage.createProviderWithLogin("testProvider", "password");
        ProviderPage providerPage = loginPage.loginWithProviderUserNamePassword("testProvider", "password");
        assertTrue(StringUtils.contains(providerPage.getWelcomeText(), "Welcome testProvider"));
        providerPage.logout();
    }
}
