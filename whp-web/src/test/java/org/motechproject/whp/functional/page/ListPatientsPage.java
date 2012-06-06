package org.motechproject.whp.functional.page;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.util.List;

public class ListPatientsPage extends LoggedInUserPage {

    @FindBy(how = How.CLASS_NAME, using = "patientId")
    protected List<WebElement> patientIds;

    @FindBy(how = How.CLASS_NAME, using = "name")
    protected List<WebElement> patientNames;

    @FindBy(how = How.CLASS_NAME, using = "tbId")
    protected List<WebElement> tbIds;

    @FindBy(how = How.CLASS_NAME, using = "category")
    protected List<WebElement> categories;

    public ListPatientsPage(WebDriver webDriver) {
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

    public String getTreatmentCategoryText(String patientId) {
        return webDriver.findElement(By.id(String.format("patient_%s_TreatmentCategory", patientId))).getText();
    }

    public String getTreatmentStartDateText(String patientId) {
        return webDriver.findElement(By.id(String.format("patient_%s_TreatmentStartDate", patientId))).getText();
    }

    public String getGenderText(String patientId) {
        return webDriver.findElement(By.id(String.format("patient_%s_Gender", patientId))).getText();
    }

    public String getVillageText(String patientId) {
        return webDriver.findElement(By.id(String.format("patient_%s_Village", patientId))).getText();
    }

    public boolean isPatientTreatmentPaused(String patientId) {
        return webDriver.findElement(By.id(String.format("patientList_%s", patientId))).getCssValue("background-color").equals("rgb(255, 182, 193)");
    }

    public boolean hasTbId(String tbId) {
        for (WebElement tbIdElement : tbIds) {
            if (tbIdElement.getText().compareToIgnoreCase(tbId) == 0) {
                return true;
            }
        }
        return false;
    }
}
