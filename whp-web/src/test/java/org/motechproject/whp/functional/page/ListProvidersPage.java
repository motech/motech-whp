package org.motechproject.whp.functional.page;

import org.motechproject.whp.functional.framework.WHPWebElement;
import org.motechproject.whp.functional.framework.WebDriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class ListProvidersPage extends LoggedInUserPage {

    @FindBy(how = How.ID, using = "providerId")
    protected WebElement providerId;

    @FindBy(how = How.ID, using = "district")
    protected WebElement district;

    @FindBy(how = How.ID, using = "search")
    protected WebElement submit;

    @FindBy(how = How.CLASS_NAME, using = "warning")
    protected WebElement warning;

    public ListProvidersPage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    protected void waitForPageToLoad() {
        waitForElementWithIdToLoad("search");
    }

    @Override
    public void postInitialize() {
        district = WebDriverFactory.createWebElement(district);
    }

    public boolean hasProviderRow(String providerId) {
        return safeFindElement(By.id(String.format("provider_%s_ProviderId", providerId))) != null;
    }

    public void searchBy(String district, String providerId, boolean expectingResult) {
        this.providerId.sendKeys(providerId.toLowerCase());
        ((WHPWebElement) this.district).select(district);
        this.submit.click();
        if (expectingResult) {
            waitForElementWithCSSToLoad("provider-row");
        } else {
            waitForElementWithCSSToLoad("warning");
        }
    }

    public String getWarningText() {
        return warning.getText();
    }
}
