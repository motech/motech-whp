package org.motechproject.whp.functional.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class LoggedInUserPage extends Page {

    @FindBy(how = How.ID, using = "links")
    private WebElement welcomeDiv;

    @FindBy(how = How.ID, using = "logout")
    private WebElement logoutLink;

    public LoggedInUserPage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    protected void waitForPageToLoad() {
        waitForElementWithIdToLoad("links");
    }

    public String getWelcomeText() {
        return welcomeDiv.getText();
    }

    @Override
    public void logout() {
        logoutLink.click();
    }
}
