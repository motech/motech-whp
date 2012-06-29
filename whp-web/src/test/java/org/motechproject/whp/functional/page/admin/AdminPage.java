package org.motechproject.whp.functional.page.admin;

import org.motechproject.whp.functional.framework.MyPageFactory;
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

    public ListAllPatientsPage navigateToShowAllPatients() {
        showPatientsLink.click();
        ListAllPatientsPage listAllPatientsPage = MyPageFactory.initElements(webDriver, ListAllPatientsPage.class);
        return listAllPatientsPage.searchByDistrict("Begusarai");
    }
}
