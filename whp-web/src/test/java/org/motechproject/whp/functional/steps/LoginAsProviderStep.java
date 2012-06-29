package org.motechproject.whp.functional.steps;

import org.motechproject.whp.functional.data.TestProvider;
import org.motechproject.whp.functional.framework.MyPageFactory;
import org.motechproject.whp.functional.page.LoginPage;
import org.motechproject.whp.functional.page.provider.ProviderPage;
import org.openqa.selenium.WebDriver;

public class LoginAsProviderStep {

    private WebDriver webDriver;

    public LoginAsProviderStep(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public ProviderPage execute(TestProvider provider) {
        return MyPageFactory.initElements(webDriver, LoginPage.class).loginWithProviderUserNamePassword(provider.getProviderId(), provider.getPassword());
    }

}
