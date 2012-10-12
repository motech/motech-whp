package org.motechproject.whp.functional.page.admin;


import org.motechproject.whp.functional.page.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import static org.motechproject.whp.functional.framework.WebDriverFactory.createWebElement;

public class ItAdminLandingPage extends Page {

    @FindBy(how = How.ID, using = "cmf-admins")
    private WebElement searchCmfAdminsLink;

    public ItAdminLandingPage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    protected void waitForPageToLoad() {

    }

    public ListAllCmfAdminsPage navigateToSearchCmfAdmins() {
        webDriver.findElement(By.cssSelector(".dropdown-toggle")).click();
        waitForElementToBeVisible(By.id("cmf-admins"));
        createWebElement(webDriver.findElement(By.id("cmf-admins"))).click();
        return getListAllCmfAdminsPage(webDriver);
    }


}
