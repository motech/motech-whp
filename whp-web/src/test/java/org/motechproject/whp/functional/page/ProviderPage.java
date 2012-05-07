package org.motechproject.whp.functional.page;

import org.motechproject.whp.functional.framework.MyPageFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.util.List;

public class ProviderPage extends LoggedInUserPage {

    @FindBy(how = How.CLASS_NAME, using = "patientId")
    private List<WebElement> patientIds;

    @FindBy(how = How.CLASS_NAME, using = "name")
    private List<WebElement> patientNames;

    @FindBy(how = How.CLASS_NAME, using = "category")
    private List<WebElement> treatmentCategories;

    @FindBy(how = How.CLASS_NAME, using = "updateAdherenceLink")
    private List<WebElement> updateAdherenceLinks;


    public ProviderPage(WebDriver webDriver) {
        super(webDriver);
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

    public UpdateAdherencePage clickEditAdherenceLink(String patientId) {
        int index = -1;
        for (int i = 0; i < patientIds.size(); i++) {
            if (patientIds.get(i).getText().equals(patientId)) {
                index = i;
                break;
            }
        }
        updateAdherenceLinks.get(index).findElement(By.linkText("Edit")).click();
        return MyPageFactory.initElements(webDriver, UpdateAdherencePage.class);
    }

}
