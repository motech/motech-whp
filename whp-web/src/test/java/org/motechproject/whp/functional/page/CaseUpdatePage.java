package org.motechproject.whp.functional.page;

import org.motechproject.whp.functional.data.TestPatient;
import org.motechproject.whp.functional.framework.MyPageFactory;
import org.motechproject.whp.functional.framework.WHPUrl;
import org.motechproject.whp.functional.framework.WebDriverFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class CaseUpdatePage extends Page {

    @FindBy(how = How.ID, using = "data")
    WebElement requestData;
    @FindBy(how = How.ID, using = "submit")
    WebElement submit;

    public CaseUpdatePage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    public void postInitialize() {
        requestData = WebDriverFactory.createWebElement(requestData);
        submit = WebDriverFactory.createWebElement(submit);
    }

    @Override
    protected void waitForPageToLoad() {
        waitForElementWithIdToLoad("data");
    }

    public static CaseUpdatePage fetch(WebDriver webDriver) {
        webDriver.get(WHPUrl.baseFor("emulator"));
        return MyPageFactory.initElements(webDriver, CaseUpdatePage.class);
    }

    public LoginPage updateCase(String requestData){
        this.requestData.sendKeys(requestData);
        submit.click();
        webDriver.get(LoginPage.LOGIN_URL);
        return MyPageFactory.initElements(webDriver, LoginPage.class);
    }
}
