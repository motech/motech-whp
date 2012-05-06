package org.motechproject.whp.functional.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.util.List;

public class ProviderPage extends Page {

    @FindBy(how = How.ID, using = "links")
    private WebElement welcomeDiv;

    @FindBy(how = How.ID, using = "logout")
    private WebElement logoutLink;

    @FindBy(how = How.CLASS_NAME, using = "name")
    private List<WebElement> patientNames;

    @FindBy(how = How.CLASS_NAME, using = "category")
    private List<WebElement> treatmentCategories;



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

    public boolean hasTreatmentCategory(String treatmentCategory) {
        for (WebElement treatmentCategoryElement : treatmentCategories) {
            if (treatmentCategoryElement.getText().equals(treatmentCategory)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void logout() {
        logoutLink.click();
    }
}
