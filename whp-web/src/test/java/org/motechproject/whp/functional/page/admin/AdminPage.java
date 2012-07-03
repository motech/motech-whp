package org.motechproject.whp.functional.page.admin;

import org.motechproject.whp.functional.framework.MyPageFactory;
import org.motechproject.whp.functional.framework.WebDriverFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class AdminPage extends ListAllPatientsPage {

    @FindBy(how = How.ID, using = "show-patients")
    private WebElement showPatientsLink;

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
        showPatientsLink.click();
        ListAllPatientsPage listAllPatientsPage = MyPageFactory.initElements(webDriver, ListAllPatientsPage.class);
        return listAllPatientsPage.searchByDistrict("Begusarai");
    }
}
