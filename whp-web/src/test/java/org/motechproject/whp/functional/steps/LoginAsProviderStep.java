package org.motechproject.whp.functional.steps;

import org.motechproject.whp.functional.data.TestProvider;
import org.motechproject.whp.functional.page.provider.ProviderPage;
import org.openqa.selenium.WebDriver;

import static org.motechproject.whp.functional.page.Page.getLoginPage;

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
        return getLoginPage(webDriver).loginAsProvider(testProvider.getProviderId(), testProvider.getPassword());
    }
}
