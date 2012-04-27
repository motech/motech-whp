package org.motechproject.whp.functional.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class PatientPage extends Page {
    
    @FindBy(how = How.ID, using = "welcomeMessage")
    private WebElement welcomeDiv;

    public PatientPage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    protected void waitForPageToLoad() {
        waitForElementWithIdToLoad("welcomeMessage");
    }

    public String getWelcomeText() {
        return welcomeDiv.getText();
    }
}
