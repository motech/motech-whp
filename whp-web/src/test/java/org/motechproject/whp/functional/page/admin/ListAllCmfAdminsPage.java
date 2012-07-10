package org.motechproject.whp.functional.page.admin;

import org.motechproject.whp.functional.page.LoggedInUserPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class ListAllCmfAdminsPage extends LoggedInUserPage{
    @FindBy(how = How.ID, using = "resetPassword")
    protected WebElement resetPasswordButton;

    @FindBy(how = How.ID, using = "resetPasswordClose")
    protected WebElement resetPasswordCancelButton;

    @FindBy(how = How.ID, using = "resetPasswordUserName")
    protected WebElement resetPasswordUserName;



    public ListAllCmfAdminsPage(WebDriver webDriver) {
        super(webDriver);
    }

}
