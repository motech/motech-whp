package org.motechproject.whp.functional.page.admin;

import org.apache.commons.lang.StringUtils;
import org.motechproject.whp.functional.framework.MyPageFactory;
import org.motechproject.whp.functional.page.LoggedInUserPage;
import org.motechproject.whp.refdata.domain.Phase;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.util.List;

import static org.motechproject.whp.functional.framework.WebDriverFactory.createWebElement;
import static org.openqa.selenium.By.id;

public class ListAllPatientsPage extends LoggedInUserPage {

    protected List<WebElement> patientIds;

    protected List<WebElement> patientNames;

    @FindBy(how = How.ID, using = "district-autocomplete")
    private WebElement districtSearchBox;

    @FindBy(how = How.ID, using = "providerId-autocomplete")
    private WebElement providerSearchBox;

    @FindBy(how = How.ID, using = "searchButton")

    private WebElement searchButton;

    private List<WebElement> dashboardLinks;

    @FindBy(how = How.ID, using = "patientList")
    private WebElement patientList;

    @Override
    protected void waitForPageToLoad() {
        waitForElementWithIdToLoad("district");
    }

    @Override
    public void postInitialize() {
        super.postInitialize();
        districtSearchBox = createWebElement(districtSearchBox);
        providerSearchBox = createWebElement(providerSearchBox);
        searchButton = createWebElement(searchButton);
        patientList = createWebElement(patientList);
    }

    public ListAllPatientsPage(WebDriver webDriver) {
        super(webDriver);
    }

    public boolean hasPatient(String patientName) {
        waitForElementWithCSSToLoad("name");
        patientNames = webDriver.findElements(By.className("name"));
        for (WebElement patientNameElement : patientNames) {
            if (StringUtils.containsIgnoreCase(patientNameElement.getText(), patientName)) {
                return true;
            }
        }
        return false;
    }

    public String getGenderText(String patientId) {
        return webDriver.findElement(id(String.format("patient_%s_Gender", patientId))).getText();
    }

    public String getVillageText(String patientId) {
        return webDriver.findElement(id(String.format("patient_%s_Village", patientId))).getText();
    }

    public String getPhaseProgress(String patientId, Phase phase) {
        return webDriver.findElement(id(String.format("patient_%s_%sProgress", patientId, phase.name()))).getText();
    }

    public String getCumulativeMissedDoses(String patientId) {
        return webDriver.findElement(id(String.format("patient_%s_MissedDoses", patientId))).getText();
    }

    public PatientDashboardPage clickOnPatientWithTherapyNotYetStarted(String patientId) {
        clickOnPatientRow(patientId);
        return MyPageFactory.initElements(webDriver, PatientDashboardPage.class);
    }

    public TreatmentCardPage clickOnPatientWithTherapyStarted(String patientId) {
        clickOnPatientRow(patientId);
        return MyPageFactory.initElements(webDriver, TreatmentCardPage.class);
    }

    public ListAllPatientsPage searchByDistrict(String district) {
        districtSearchBox.sendKeys(district);
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) webDriver;
        javascriptExecutor.executeScript("$('#district').val('" + district + "');"); // can't set select box directly as it is hidden
        searchButton.click();
        waitForElementToBeReloadedByAjax();
        return MyPageFactory.initElements(webDriver, ListAllPatientsPage.class);
    }

    public ListAllPatientsPage searchByDistrictAndProvider(String districtName, String providerId) {
        districtSearchBox.sendKeys(districtName);
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) webDriver;
        javascriptExecutor.executeScript("$('#district').val('" + districtName + "');"); // can't set select box directly as it is hidden
        waitForElementToBeReloadedByAjax();
        searchButton.click();

        providerSearchBox.sendKeys(providerId);
        javascriptExecutor.executeScript("$('#providerId').val('" + providerId + "');"); // can't set select box directly as it is hidden
        waitForElementToBeReloadedByAjax();
        searchButton.click();
        waitForElementToBeReloadedByAjax();

        return MyPageFactory.initElements(webDriver, ListAllPatientsPage.class);
    }

    private void clickOnPatientRow(String patientId) {
        waitForElementWithCSSToLoad("patientId");
        patientIds = webDriver.findElements(By.className("patientId"));
        int index = -1;
        for (int i = 0; i < patientIds.size(); i++) {
            if (patientIds.get(i).getText().equals(patientId)) {
                index = i;
                break;
            }
        }
        waitForElementWithCSSToLoad("link");
        dashboardLinks = webDriver.findElements(By.className("link"));
        createWebElement(dashboardLinks.get(index).findElement(By.className("patientId")).findElement(By.tagName("a"))).click();
    }
}
