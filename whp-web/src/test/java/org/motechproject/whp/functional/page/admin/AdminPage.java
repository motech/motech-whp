package org.motechproject.whp.functional.page.admin;

import org.motechproject.whp.functional.framework.MyPageFactory;
import org.motechproject.whp.functional.page.provider.ListPatientsPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class AdminPage extends ListPatientsPage {

    @FindBy(how = How.ID, using = "show-patients")
    private WebElement showPatientsLink;

    public AdminPage(WebDriver webDriver) {
        super(webDriver);
    }

    public ListPatientsPage navigateToShowAllPatients() {
        showPatientsLink.click();
        return MyPageFactory.initElements(webDriver, ListPatientsPage.class);
    }
}
