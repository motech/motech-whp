package org.motechproject.whp.functional.page;

import org.apache.commons.lang.StringUtils;
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

    @FindBy(how = How.CLASS_NAME, using = "updateAdherenceLink")
    private List<WebElement> updateAdherenceLinks;

    @FindBy(how = How.CLASS_NAME, using = "tbId")
    private List<WebElement> tbIds;

    public ProviderPage(WebDriver webDriver) {
        super(webDriver);
    }

    public boolean hasPatient(String patientName) {
        for (WebElement patientNameElement : patientNames) {
            if (StringUtils.containsIgnoreCase(patientNameElement.getText(), patientName)) {
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

    public String getTreatmentCategoryText(String patientId) {
        return webDriver.findElement(By.id(String.format("patient_%s_TreatmentCategory", patientId))).getText();
    }

    public String getTreatmentOutcomeText(String patientId) {
        return webDriver.findElement(By.id(String.format("patient_%s_TreatmentOutcome", patientId))).getText();
    }

    public boolean isPatientPaused(String patientId) {
        return webDriver.findElement(By.id(String.format("patientList_%s", patientId))).getCssValue("background-color").equals("rgb(255, 182, 193)");
    }

    public boolean hasTbId(String tbId) {
        for (WebElement tbIdElement : tbIds) {
            if (tbIdElement.getText().equals(tbId)) {
                return true;
            }
        }
        return false;
    }
}
