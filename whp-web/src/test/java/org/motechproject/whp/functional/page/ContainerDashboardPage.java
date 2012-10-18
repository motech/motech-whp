package org.motechproject.whp.functional.page;

import org.apache.commons.lang.StringUtils;
import org.motechproject.whp.functional.framework.MyPageFactory;
import org.motechproject.whp.functional.framework.WebDriverFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.util.List;

import static org.openqa.selenium.By.cssSelector;

public class ContainerDashboardPage extends Page {

    @FindBy(how = How.ID, id = "sputumTrackingDashboardRowsList")
    private WebElement table;

    @FindBy(how = How.ID, id = "containerIssuedDateFrom")
    private WebElement containerIssuedFrom;

    @FindBy(how = How.ID, id = "containerIssuedDateTo")
    private WebElement containerIssuedTo;

    @FindBy(how = How.ID, id = "search")
    private WebElement search;

    public ContainerDashboardPage(WebDriver webDriver) {
        super(webDriver);
        containerIssuedFrom = WebDriverFactory.createWebElement(containerIssuedFrom);
        containerIssuedTo = WebDriverFactory.createWebElement(containerIssuedTo);
        search = WebDriverFactory.createWebElement(search);
    }

    @Override
    protected void waitForPageToLoad() {
        waitForElementWithIdToLoad("search");
    }

    public static ContainerDashboardPage fetch(WebDriver webDriver) {
        return getLoginPage(webDriver).loginAsAdmin().navigateToContainerDashboardPage();
    }

    public boolean hasContainerId(String containerId) {
        List<WebElement> tds = table.findElements(cssSelector(".containerId"));
        for (WebElement td : tds) {
            if (StringUtils.equals(containerId, td.getText()))
                return true;
        }
        return false;
    }

    public ContainerDashboardPage filterByContainerIssuedDate(String fromDate, String toDate) {
        containerIssuedFrom.sendKeys(fromDate);
        containerIssuedTo.sendKeys(toDate);
        search.click();
        waitForScript(5000);
        return MyPageFactory.initElements(webDriver, ContainerDashboardPage.class);
    }
}
