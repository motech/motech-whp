package org.motechproject.whp.functional.page;

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

    public boolean hasProviderRow(String providerId) {
        return safeFindElement(By.id(String.format("provider_%s_ProviderId", providerId))) != null;
    }

    public void searchBy(String district, String providerId) {
        this.providerId.sendKeys(providerId.toLowerCase());
        this.district.sendKeys(district);
        this.submit.click();
    }

    public String getWarningText() {
        return warning.getText();
    }
}
