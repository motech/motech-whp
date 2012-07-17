package org.motechproject.whp.functional.page.admin;

import org.motechproject.whp.functional.framework.MyPageFactory;
import org.motechproject.whp.functional.framework.WebDriverFactory;
import org.motechproject.whp.functional.page.LoggedInUserPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import static junit.framework.Assert.assertEquals;
import static org.motechproject.whp.functional.framework.WebDriverFactory.createWebElement;

public class EditCmfAdminPage extends LoggedInUserPage {

    @FindBy(how = How.ID, using = "locationId")
    private WebElement location;

    @FindBy(how = How.ID, using = "email")
    private WebElement emailId;

    @FindBy(how = How.ID, using = "staffName")
    private WebElement staffName;

    @FindBy(how = How.ID, using = "department")
    private WebElement department;

    @FindBy(how = How.ID, using = "editCmfAdminButton")
    private WebElement submitButton;

    public EditCmfAdminPage(WebDriver webDriver) {
        super(webDriver);
    }

    public static EditCmfAdminPage fetch(WebDriver webDriver) {
        return MyPageFactory.initElements(webDriver, EditCmfAdminPage.class);
    }

    public ListAllCmfAdminsPage updateCmfAdmin(String userId, String location, String emailId, String staffName, String department) {
        setEmailId(emailId);
        setStaffName(staffName);
        setDepartment(department);
        setLocation(location);
        submitButton.click();
        waitForSuccess("Successfully updated cmf admin with user id "+userId);
        return ListAllCmfAdminsPage.getListAllCmfAdminsPage(webDriver);
    }

    private EditCmfAdminPage setDepartment(String department) {
        this.department.clear();
        this.department.sendKeys(department);
        return this;
    }

    private EditCmfAdminPage setStaffName(String staffName) {
        this.staffName.clear();
        this.staffName.sendKeys(staffName);
        return this;
    }

    private EditCmfAdminPage setEmailId(String emailId) {
        this.emailId.clear();
        this.emailId.sendKeys(emailId);
        return this;
    }

    private EditCmfAdminPage setLocation(String location) {
        this.location.sendKeys(location);
        return this;
    }

    public void assertValues(String location, String emailId, String staffName, String department) {
        assertEquals(emailId, this.emailId.getAttribute("value"));
        assertEquals(staffName, this.staffName.getAttribute("value"));
        assertEquals(department, this.department.getAttribute("value"));
        assertEquals(location, this.location.getAttribute("value"));
    }
}
