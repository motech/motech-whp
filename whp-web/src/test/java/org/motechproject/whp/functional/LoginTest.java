package org.motechproject.whp.functional;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.motechproject.whp.functional.framework.BaseTest;
import org.motechproject.whp.functional.framework.MyPageFactory;
import org.motechproject.whp.functional.page.LoginPage;
import org.motechproject.whp.functional.page.PatientPage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LoginTest extends BaseTest {
    @Test
    public void testLoginFailure() {
        LoginPage page = MyPageFactory.initElements(webDriver, LoginPage.class).loginWithIncorrectAdminUserNamePassword();
        assertEquals(LoginPage.FAILURE_MESSAGE, page.errorMessage());
    }

    @Test
    public void testLoginSuccessForAdministrator() {
        PatientPage patientPage = MyPageFactory.initElements(webDriver, LoginPage.class).loginWithCorrectAdminUserNamePassword();
        assertTrue(StringUtils.contains(patientPage.getWelcomeText(), "Welcome admin!"));
        patientPage.logout();
    }

    @Test
    public void testLoginSuccessForProvider() {
        PatientPage patientPage = MyPageFactory.initElements(webDriver, LoginPage.class).loginWithProviderUserNamePassword("raj", "password");
        assertTrue(StringUtils.contains(patientPage.getWelcomeText(), "Welcome raj!"));
        patientPage.logout();
    }
}
