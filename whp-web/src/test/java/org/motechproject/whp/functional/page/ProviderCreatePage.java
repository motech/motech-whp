package org.motechproject.whp.functional.page;

import org.motechproject.whp.functional.framework.MyPageFactory;
import org.motechproject.whp.functional.framework.WHPUrl;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class ProviderCreatePage extends Page {

    @FindBy(how = How.ID, using = "provider_id")
    WebElement providerId;
    @FindBy(how = How.ID, using = "password")
    WebElement password;
    @FindBy(how = How.ID, using = "post-button")
    WebElement submit;

    public ProviderCreatePage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    protected void waitForPageToLoad() {
        waitForElementWithIdToLoad("provider_id");
    }

    public static ProviderCreatePage fetch(WebDriver webDriver) {
        webDriver.get(WHPUrl.baseFor("emulator/provider.jsp"));
        return MyPageFactory.initElements(webDriver, ProviderCreatePage.class);
    }

    //Uses providerId as username
    public LoginPage createProviderWithLogin(String providerId, String password) {
        setProviderId(providerId);
        setPassword(password);
        submit.click();
        return transitionToNextPage();
    }

    private void setProviderId(String providerId) {
        this.providerId.clear();
        this.providerId.sendKeys(providerId);
    }

    private void setPassword(String password) {
        this.password.clear();
        this.password.sendKeys(password);
    }

    private LoginPage transitionToNextPage() {
        webDriver.get(LoginPage.LOGIN_URL);
        return MyPageFactory.initElements(webDriver, LoginPage.class);
    }
}
