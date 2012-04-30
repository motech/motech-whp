package org.motechproject.whp.functional.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.util.List;

public class ProviderPage extends Page {

    @FindBy(how = How.ID, using = "links")
    private WebElement welcomeDiv;

    @FindBy(how = How.CLASS_NAME, using = "name")
    private List<WebElement> patientNames;

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

    public boolean hasPatient(String patientName) {
        for (WebElement patientNameElement : patientNames) {
            if (patientNameElement.getText().equals(patientName)) {
                return true;
            }
        }
        return false;
    }
}
