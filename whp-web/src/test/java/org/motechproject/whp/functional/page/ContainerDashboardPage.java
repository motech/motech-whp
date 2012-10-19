package org.motechproject.whp.functional.page;

import org.apache.commons.lang.StringUtils;
import org.motechproject.whp.functional.framework.MyPageFactory;
import org.motechproject.whp.functional.framework.WHPWebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.util.List;

import static org.motechproject.whp.functional.framework.WebDriverFactory.createWebElement;
import static org.openqa.selenium.By.cssSelector;

public class ContainerDashboardPage extends Page {

    @FindBy(how = How.ID, id = "sputumTrackingDashboardRowsList")
    private WebElement table;

    @FindBy(how = How.ID, id = "district")
    private WebElement district;

    @FindBy(how = How.ID, id = "containerIssuedDateFrom")
    private WebElement containerIssuedFrom;

    @FindBy(how = How.ID, id = "containerIssuedDateTo")
    private WebElement containerIssuedTo;

    @FindBy(how = How.ID, id = "diagnosis")
    private WebElement diagnosis;

    @FindBy(how = How.ID, id = "search")
    private WebElement search;

    public ContainerDashboardPage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    public void postInitialize() {
        containerIssuedFrom = createWebElement(containerIssuedFrom);
        containerIssuedTo = createWebElement(containerIssuedTo);
        diagnosis = createWebElement(diagnosis);
        search = createWebElement(search);
    }

    @Override
    protected void waitForPageToLoad() {
        waitForElementWithIdToLoad("search");
    }

    public static ContainerDashboardPage fetch(WebDriver webDriver) {
        return getLoginPage(webDriver).loginAsAdmin().navigateToContainerDashboardPage();
    }

    public boolean hasContainerId(String containerId) {
        return rowForContainer(containerId) != null;
    }

    public ContainerDashboardPage filterByContainerIssuedDate(String fromDate, String toDate) {
        containerIssuedFrom.sendKeys(fromDate);
        containerIssuedTo.sendKeys(toDate);
        search.click();
        waitForScript(5000);
        return MyPageFactory.initElements(webDriver, ContainerDashboardPage.class);
    }

    public ContainerDashboardPage filterByDistrict(String district) {
        ((JavascriptExecutor) webDriver).executeScript("$('#district').val('" + district + "')");
        search.click();
        waitForScript(5000);
        return MyPageFactory.initElements(webDriver, ContainerDashboardPage.class);
    }

    public ContainerDashboardPage filterBy(String district, String diagnosis, String containerIssuedFrom, String containerIssuedTo) {
        this.containerIssuedFrom.sendKeys(containerIssuedFrom);
        this.containerIssuedTo.sendKeys(containerIssuedTo);
        ((JavascriptExecutor) webDriver).executeScript("$('#district').val('" + district + "')");
        ((WHPWebElement) this.diagnosis).select(diagnosis);
        search.click();
        waitForScript(5000);
        return MyPageFactory.initElements(webDriver, ContainerDashboardPage.class);
    }

    public ContainerDashboardPage closeContainer(String containerId) {
        WebElement row = rowForContainer(containerId);
        if (row != null) {
            createWebElement(row.findElement(By.linkText("Close"))).click();
            waitForElementToBeVisible(By.id("reason"));
            ((WHPWebElement) createWebElement(webDriver.findElement(By.id("reason")))).select("2");
            webDriver.findElement(By.id("saveReason")).click();
            waitForScript(5000);
        }
        return MyPageFactory.initElements(webDriver, ContainerDashboardPage.class);
    }

    private WebElement rowForContainer(String containerId) {
        List<WebElement> trs = table.findElements(cssSelector(".sputum-tracking-dashboard-row"));
        for (WebElement tr : trs) {
            if (StringUtils.equals(containerId, tr.findElement(By.cssSelector(".containerId")).getText()))
                return tr;
        }
        return null;
    }
}
