package org.motechproject.whp.functional.page;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.util.List;

import static org.openqa.selenium.By.cssSelector;

public class ContainerDashboardPage extends Page {

    @FindBy(how = How.ID, id = "sputumTrackingDashboardRowsList")
    private WebElement table;

    public ContainerDashboardPage(WebDriver webDriver) {
        super(webDriver);
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
}
