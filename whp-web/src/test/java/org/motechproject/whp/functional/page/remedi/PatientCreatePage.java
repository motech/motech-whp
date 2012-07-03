package org.motechproject.whp.functional.page.remedi;

import org.apache.commons.lang.StringUtils;
import org.motechproject.whp.functional.data.TestPatient;
import org.motechproject.whp.functional.framework.MyPageFactory;
import org.motechproject.whp.functional.framework.WHPUrl;
import org.motechproject.whp.functional.framework.WebDriverFactory;
import org.motechproject.whp.functional.page.LoginPage;
import org.motechproject.whp.functional.page.Page;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedCondition;

import static org.openqa.selenium.By.id;

public class PatientCreatePage extends Page {

    @FindBy(how = How.ID, using = "case_id")
    WebElement caseId;
    @FindBy(how = How.ID, using = "tb_id")
    WebElement tbId;
    @FindBy(how = How.ID, using = "provider_id")
    WebElement providerId;
    @FindBy(how = How.ID, using = "first_name")
    WebElement firstName;
    @FindBy(how = How.ID, using = "av")
    WebElement village;
    @FindBy(how = How.ID, using = "dclass")
    WebElement diseaseClass;
    @FindBy(how = How.ID, using = "submit")
    WebElement submit;
    @FindBy(how = How.ID, using = "address_district")
    WebElement district;

    public PatientCreatePage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    public void postInitialize() {
        caseId = WebDriverFactory.createWebElement(caseId);
        tbId = WebDriverFactory.createWebElement(tbId);
        providerId = WebDriverFactory.createWebElement(providerId);
        firstName = WebDriverFactory.createWebElement(firstName);
        village = WebDriverFactory.createWebElement(village);
        district = WebDriverFactory.createWebElement(district);
        diseaseClass = WebDriverFactory.createWebElement(diseaseClass);
        submit = WebDriverFactory.createWebElement(submit);
    }

    @Override
    protected void waitForPageToLoad() {
        waitForElementWithIdToLoad("case_id");
    }

    public static PatientCreatePage fetch(WebDriver webDriver) {
        webDriver.get(WHPUrl.baseFor("emulator/patient.jsp"));
        return MyPageFactory.initElements(webDriver, PatientCreatePage.class);
    }

    public LoginPage createPatient(TestPatient testPatient){
        this.caseId.sendKeys(testPatient.getCaseId());
        this.tbId.sendKeys(testPatient.getTbId());
        this.providerId.sendKeys(testPatient.getProviderId());
        this.firstName.sendKeys(testPatient.getFirstName());
        this.village.sendKeys(testPatient.getVillage());
        this.district.sendKeys(testPatient.getDistrict());
        this.diseaseClass.sendKeys(testPatient.getDiseaseClass());
        submit.click();
        waitForSuccess();
        webDriver.get(LoginPage.LOGIN_URL);
        return MyPageFactory.initElements(webDriver, LoginPage.class);
    }

    private void waitForSuccess() {
        wait.until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                WebElement statusMessage = webDriver.findElement(id("statusMessage"));
                return StringUtils.containsIgnoreCase(statusMessage.getText(), "success");
            }
        });
    }
}
