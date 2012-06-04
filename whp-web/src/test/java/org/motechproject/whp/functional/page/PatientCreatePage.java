package org.motechproject.whp.functional.page;

import org.motechproject.whp.functional.framework.MyPageFactory;
import org.motechproject.whp.functional.framework.WHPUrl;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class PatientCreatePage extends Page {

    @FindBy(how = How.ID, using = "case_id")
    WebElement caseId;
    @FindBy(how = How.ID, using = "provider_id")
    WebElement providerId;
    @FindBy(how = How.ID, using = "first_name")
    WebElement firstName;
    @FindBy(how = How.ID, using = "post_button")
    WebElement submit;

    public PatientCreatePage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    protected void waitForPageToLoad() {
        waitForElementWithIdToLoad("case_id");
    }

    public static PatientCreatePage fetch(WebDriver webDriver) {
        webDriver.get(WHPUrl.baseFor("emulator/patient.jsp"));
        return MyPageFactory.initElements(webDriver, PatientCreatePage.class);
    }

    public LoginPage createPatient(String caseId, String providerId, String firstName){
        this.providerId.sendKeys(providerId);
        this.caseId.sendKeys(caseId);
        this.firstName.sendKeys(firstName);
        submit.click();
        webDriver.get(LoginPage.LOGIN_URL);
        return MyPageFactory.initElements(webDriver, LoginPage.class);
    }
}
