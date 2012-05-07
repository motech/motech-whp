package org.motechproject.whp.functional;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.motechproject.whp.functional.framework.BaseTest;
import org.motechproject.whp.functional.framework.MyPageFactory;
import org.motechproject.whp.functional.page.AdminPage;
import org.motechproject.whp.functional.page.LoginPage;

import static org.junit.Assert.assertTrue;

public class ChangePasswordTest extends BaseTest {

    @Test
    public void testChangePasswordForAdministrator() {
        AdminPage providerPage = MyPageFactory.initElements(webDriver, LoginPage.class).loginWithCorrectAdminUserNamePassword();
        assertTrue(StringUtils.contains(providerPage.getWelcomeText(), "admin"));
        providerPage.logout();
    }
}
