package org.motechproject.whp.functional.page.remedi;

import org.apache.commons.lang.StringUtils;
import org.motechproject.whp.functional.framework.MyPageFactory;
import org.motechproject.whp.functional.framework.WHPUrl;
import org.motechproject.whp.functional.framework.WebDriverFactory;
import org.motechproject.whp.functional.page.LoginPage;
import org.motechproject.whp.functional.page.Page;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedCondition;

import static org.openqa.selenium.By.id;

public class ProviderActivatePage extends Page {

    @FindBy(how = How.ID, using = "provider_id")
    WebElement providerId;
    @FindBy(how = How.ID, using = "post-button")
    WebElement submit;

    public ProviderActivatePage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    public void postInitialize() {
        providerId = WebDriverFactory.createWebElement(providerId);
        submit = WebDriverFactory.createWebElement(submit);
    }

    @Override
    protected void waitForPageToLoad() {
        waitForElementWithIdToLoad("provider_id");
    }

    public static ProviderActivatePage fetch(WebDriver webDriver) {
        webDriver.get(WHPUrl.baseFor("emulator/cmfAdmin.jsp"));
        return MyPageFactory.initElements(webDriver, ProviderActivatePage.class);
    }

    //Uses providerId as username
    public LoginPage activateProvider(String providerId) {
        setProviderId(providerId);
        submit.click();
        waitForSuccess("Activate Provider");
        return transitionToNextPage();
    }

    private void setProviderId(String providerId) {
        this.providerId.clear();
        this.providerId.sendKeys(providerId);
    }

    private LoginPage transitionToNextPage() {
        webDriver.get(LoginPage.LOGIN_URL);
        return MyPageFactory.initElements(webDriver, LoginPage.class);
    }
}
