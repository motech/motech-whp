package org.motechproject.whp.functional.steps;

import org.motechproject.whp.functional.framework.MyPageFactory;
import org.motechproject.whp.functional.page.LoginPage;
import org.motechproject.whp.functional.page.admin.ListAllPatientsPage;
import org.openqa.selenium.WebDriver;

public class LoginAsCmfAdminStep {

    private WebDriver webDriver;
    public ListAllPatientsPage listAllPatientsPage;

    public LoginAsCmfAdminStep(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public void execute() {
        listAllPatientsPage = MyPageFactory.initElements(webDriver, LoginPage.class).loginWithCorrectAdminUserNamePassword().navigateToShowAllPatients();
    }

}
