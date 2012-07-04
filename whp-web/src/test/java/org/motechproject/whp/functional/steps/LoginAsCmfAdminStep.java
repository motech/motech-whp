package org.motechproject.whp.functional.steps;

import org.motechproject.whp.functional.framework.MyPageFactory;
import org.motechproject.whp.functional.page.LoginPage;
import org.motechproject.whp.functional.page.admin.ListAllPatientsPage;
import org.openqa.selenium.WebDriver;

public class LoginAsCmfAdminStep extends Step {

    private WebDriver webDriver;

    public LoginAsCmfAdminStep(WebDriver webDriver) {
        super(webDriver);
        this.webDriver = webDriver;
    }

    @Override
    public ListAllPatientsPage execute() {
        return MyPageFactory.initElements(webDriver, LoginPage.class).loginWithCorrectAdminUserNamePassword().navigateToShowAllPatients();
    }
}
