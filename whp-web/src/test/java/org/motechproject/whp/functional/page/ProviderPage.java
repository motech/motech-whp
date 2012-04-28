package org.motechproject.whp.functional.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class ProviderPage extends Page {
    
    @FindBy(how = How.ID, using = "links")
    private WebElement welcomeDiv;

    public ProviderPage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    protected void waitForPageToLoad() {
        waitForElementWithIdToLoad("links");
    }

    public String getWelcomeText() {
        return welcomeDiv.getText();
    }
}
