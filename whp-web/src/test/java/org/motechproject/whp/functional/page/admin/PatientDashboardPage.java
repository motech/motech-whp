package org.motechproject.whp.functional.page.admin;

import org.hamcrest.Matchers;
import org.joda.time.DateTime;
import org.motechproject.whp.functional.framework.MyPageFactory;
import org.motechproject.whp.functional.framework.WebDriverFactory;
import org.motechproject.whp.functional.page.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.core.Is.is;
import static org.openqa.selenium.By.name;

public class PatientDashboardPage extends Page {

    @FindBy(how = How.ID, using = "setDateLink")
    WebElement adjustStartDatesLink;

    @FindBy(how = How.ID, using = "remarks-content")
    WebElement remarksContent;

    @FindBy(how = How.ID, using = "cmf-admin-remarks")
    WebElement cmfAdminRemarks;

    @FindBy(how = How.ID, using = "provider-remarks")
    WebElement providerRemarks;

    @FindBy(how = How.ID, using = "addRemark")
    WebElement addRemarkButton;

    @FindBy(how = How.ID, using = "patientRemark")
    WebElement patientRemarks;

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

    public String getRemarks() {
        return remarksContent.getText();
    }

    public PatientDashboardPage addRemarks(String remarks) {
        patientRemarks.sendKeys(remarks);
        addRemarkButton.click();
        waitForElementToBeReloadedByAjax();

        return MyPageFactory.initElements(webDriver, PatientDashboardPage.class);
    }

    public void verifyCmfAdminRemark(String user, DateTime dateTime, String remark, int position) {
        List<WebElement> remarks = cmfAdminRemarks.findElements(By.className("cmf-admin-remark"));

        assertRemark(user, dateTime, remark, position, remarks, false);
    }

    public void verifyProviderRemark(String user, DateTime dateTime, String remark, int position) {
        List<WebElement> remarks = providerRemarks.findElements(By.className("provider-remark"));

        assertRemark(user, dateTime, remark, position, remarks, true);
    }

    private void assertRemark(String user, DateTime dateTime, String remark, int position, List<WebElement> remarks, boolean isProviderRemark) {
        assertThat(remarks.size(), is(greaterThan(position)));
        WebElement cmfAdminRemark = remarks.get(position);
        String adminSays = user.toLowerCase();
        assertThat(cmfAdminRemark.findElement(By.tagName("h5")).getText(), is(Matchers.containsString(adminSays)));
        assertThat(cmfAdminRemark.findElement(By.tagName("div")).getText(), is(remark));

        if(isProviderRemark)
            assertThat(cmfAdminRemark.findElement(By.tagName("h5")).getText(), is(startsWith("Provider ")));
    }

}
