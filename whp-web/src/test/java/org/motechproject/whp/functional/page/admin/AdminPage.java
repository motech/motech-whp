package org.motechproject.whp.functional.page.admin;

import org.motechproject.whp.functional.framework.MyPageFactory;
import org.motechproject.whp.functional.framework.WebDriverFactory;
import org.motechproject.whp.functional.page.ContainerDashboardPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import static org.motechproject.whp.functional.framework.WebDriverFactory.createWebElement;

public class AdminPage extends ListAllPatientsPage {

    @FindBy(how = How.ID, using = "show-patients")
    private WebElement showPatientsLink;
    private WebElement containerRegistrationLink;

    public AdminPage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    public void postInitialize() {
        super.postInitialize();
        showPatientsLink = WebDriverFactory.createWebElement(showPatientsLink);
    }

    @Override
    protected void waitForPageToLoad() {
        waitForElementWithIdToLoad("show-patients");
    }

    public ListAllPatientsPage navigateToShowAllPatients() {
        webDriver.findElement(By.cssSelector(".dropdown-toggle")).click();
        waitForElementToBeVisible(By.id("show-patients"));
        createWebElement(webDriver.findElement(By.id("show-patients"))).click();
        ListAllPatientsPage listAllPatientsPage = MyPageFactory.initElements(webDriver, ListAllPatientsPage.class);
        return listAllPatientsPage.searchByDistrict("Begusarai");
    }

    public ContainerRegistrationPage navigateToContainerRegistrationPage() {
        webDriver.findElement(By.linkText("Container Tracking")).click();
        waitForElementToBeVisible(By.id("register-container"));
        createWebElement(webDriver.findElement(By.id("register-container"))).click();
        return MyPageFactory.initElements(webDriver, ContainerRegistrationPage.class);
    }

    public ContainerDashboardPage navigateToContainerDashboardPage() {
        webDriver.findElement(By.linkText("Container Tracking")).click();
        waitForElementToBeVisible(By.linkText("Sputum Tracking Dashboard"));
        createWebElement(webDriver.findElement(By.linkText("Sputum Tracking Dashboard"))).click();
        waitForElementWithCSSToLoad("containerId");
        return MyPageFactory.initElements(webDriver, ContainerDashboardPage.class);
    }
}
