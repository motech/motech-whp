package org.motechproject.whp.functional.page.provider;

import org.apache.commons.lang.StringUtils;
import org.motechproject.whp.functional.page.LoggedInUserPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class ListPatientsPage extends LoggedInUserPage {

    protected List<WebElement> patientIds;

    protected List<WebElement> patientNames;

    protected List<WebElement> tbIds;

    public ListPatientsPage(WebDriver webDriver) {
        super(webDriver);
    }

    public boolean hasPatient(String patientName) {
        System.out.println("*********************  " + getWelcomeText() + "  *********************");
        patientNames = webDriver.findElements(By.className("name"));
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
        waitForElementWithIdToLoad(String.format("patientList_%s", patientId));
        return webDriver.findElement(By.id(String.format("patientList_%s", patientId))).getAttribute("class").contains("paused");
    }

    public boolean hasTbId(String tbId) {
        tbIds = webDriver.findElements(By.className("tbId"));
        for (WebElement tbIdElement : tbIds) {
            if (tbIdElement.getText().compareToIgnoreCase(tbId) == 0) {
                return true;
            }
        }
        return false;
    }
}
