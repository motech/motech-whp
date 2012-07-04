package org.motechproject.whp.functional.steps;

import org.motechproject.whp.functional.data.TestProvider;
import org.motechproject.whp.functional.framework.MyPageFactory;
import org.motechproject.whp.functional.page.LoginPage;
import org.motechproject.whp.functional.page.provider.ProviderPage;
import org.openqa.selenium.WebDriver;

public class LoginAsProviderStep extends Step {

    private WebDriver webDriver;
    private TestProvider testProvider;

    public LoginAsProviderStep(WebDriver webDriver) {
        super(webDriver);
        this.webDriver = webDriver;
    }

    public LoginAsProviderStep withProvider(TestProvider testProvider) {
        this.testProvider = testProvider;
        return this;
    }

    @Override
    public ProviderPage execute() {
        return MyPageFactory.initElements(webDriver, LoginPage.class).loginWithProviderUserNamePassword(testProvider.getProviderId(), testProvider.getPassword());
    }
}
