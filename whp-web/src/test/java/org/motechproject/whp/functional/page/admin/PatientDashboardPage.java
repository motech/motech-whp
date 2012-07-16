package org.motechproject.whp.functional.page.admin;

import org.motechproject.whp.functional.framework.MyPageFactory;
import org.motechproject.whp.functional.framework.WebDriverFactory;
import org.motechproject.whp.functional.page.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import static org.openqa.selenium.By.name;

public class PatientDashboardPage extends Page {

    @FindBy(how = How.ID, using = "setDateLink")
    WebElement adjustStartDatesLink;

    private final String ipStartDate = "ipStartDate";
    private final String eipStartDate = "eipStartDate";
    private final String cpStartDate = "cpStartDate";

    public PatientDashboardPage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    public void postInitialize() {
        adjustStartDatesLink = WebDriverFactory.createWebElement(adjustStartDatesLink);
    }

    @Override
    protected void waitForPageToLoad() {
        waitForElementWithIdToLoad("setDateLink");
    }

    public void clickOnChangePhaseStartDates() {
        adjustStartDatesLink.click();
        waitUntilElementEditable(name(ipStartDate));
    }

    public void editStartDates(String ipStartDate, String eipStartDate, String cpStartDate) {
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) webDriver;
        if (ipStartDate != null)
            javascriptExecutor.executeScript("$('#ipDatePicker').val('" + ipStartDate + "');");
        if (eipStartDate != null)
            javascriptExecutor.executeScript("$('#eipDatePicker').val('" + eipStartDate + "');");
        if (cpStartDate != null)
            javascriptExecutor.executeScript("$('#cpDatePicker').val('" + cpStartDate + "');");
    }

    public PatientDashboardPage saveStartDates() {
        WebElement saveButton = webDriver.findElement(By.id("saveTheDate"));
        saveButton.click();
        return MyPageFactory.initElements(webDriver, PatientDashboardPage.class);
    }

    public String getIpStartDate() {
        return webDriver.findElement(name(ipStartDate)).getAttribute("value");
    }

    public String getEIpStartDate() {
        return webDriver.findElement(name(eipStartDate)).getAttribute("value");
    }

    public String getCpStartDate() {
        return webDriver.findElement(name(cpStartDate)).getAttribute("value");
    }
}
