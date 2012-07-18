package org.motechproject.whp.functional.page.admin;

import org.motechproject.whp.functional.data.TestCmfAdmin;
import org.motechproject.whp.functional.framework.MyPageFactory;
import org.motechproject.whp.functional.framework.WHPUrl;
import org.motechproject.whp.functional.framework.WHPWebElement;
import org.motechproject.whp.functional.framework.WebDriverFactory;
import org.motechproject.whp.functional.page.LoggedInUserPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.Select;

import java.util.UUID;

public class CreateCmfAdminPage extends LoggedInUserPage {

    @FindBy(how = How.ID, using = "userId")
    private WebElement userId;

    @FindBy(how = How.ID, using = "password")
    private WebElement password;

    @FindBy(how = How.ID, using = "confirmPassword")
    private WebElement confirmPassword;

    @FindBy(how = How.ID, using = "location")
    private WebElement location;

    @FindBy(how = How.ID, using = "email")
    private WebElement emailId;

    @FindBy(how = How.ID, using = "staffName")
    private WebElement staffName;

    @FindBy(how = How.ID, using = "department")
    private WebElement department;

    @FindBy(how = How.ID, using = "createCmfAdminButton")
    private WebElement submitButton;

    public CreateCmfAdminPage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    public void postInitialize() {
        location = WebDriverFactory.createWebElement(location);
        emailId = WebDriverFactory.createWebElement(emailId);
        staffName = WebDriverFactory.createWebElement(staffName);
        department = WebDriverFactory.createWebElement(department);
    }

    public static CreateCmfAdminPage fetch(WebDriver webDriver) {
        webDriver.get(WHPUrl.baseFor("cmfadmin/create"));
        return MyPageFactory.initElements(webDriver, CreateCmfAdminPage.class);
    }

    public ListAllCmfAdminsPage createCmfAdmin(TestCmfAdmin cmfAdmin) {
        setUserId(cmfAdmin.getUserId());
        setPassword(cmfAdmin.getPassword());
        setConfirmPassword(cmfAdmin.getPassword());
        setLocation(cmfAdmin.getLocation());
        setEmailId(cmfAdmin.getEmailId());
        setStaffName(cmfAdmin.getStaffName());
        setDepartment(cmfAdmin.getDepartment());
        submitButton.click();
        waitForSuccess("Successfully created cmf admin with user id " + cmfAdmin.getUserId().toLowerCase());
        return ListAllCmfAdminsPage.getListAllCmfAdminsPage(webDriver);
    }

    public TestCmfAdmin createCmfAdmin() {
        TestCmfAdmin testCmfAdmin = new TestCmfAdmin(generateId(), "password", "Patna", "a@b.com", "staff", "department");
        this.createCmfAdmin(testCmfAdmin);
        return testCmfAdmin;
    }

    public TestCmfAdmin createCmfAdmin(String password, String location, String email, String staffName, String department) {
        TestCmfAdmin testCmfAdmin = new TestCmfAdmin(generateId(), password, location, email, staffName, department);
        this.createCmfAdmin(testCmfAdmin);
        return testCmfAdmin;
    }

    private String generateId() {
        return "testCmfAdmin-" + UUID.randomUUID();
    }

    private CreateCmfAdminPage setDepartment(String department) {
        this.department.clear();
        this.department.sendKeys(department);
        return this;
    }

    private CreateCmfAdminPage setStaffName(String staffName) {
        this.staffName.clear();
        this.staffName.sendKeys(staffName);
        return this;
    }

    private CreateCmfAdminPage setEmailId(String emailId) {
        this.emailId.clear();
        this.emailId.sendKeys(emailId);
        return this;
    }

    private CreateCmfAdminPage setLocation(String location) {
        new Select(this.location).selectByValue(location);
        return this;
    }

    private CreateCmfAdminPage setPassword(String password) {
        this.password.clear();
        this.password.sendKeys(password);
        return this;
    }

    private CreateCmfAdminPage setConfirmPassword(String password) {
        this.confirmPassword.clear();
        this.confirmPassword.sendKeys(password);
        return this;
    }

    private CreateCmfAdminPage setUserId(String userId) {
        this.userId.clear();
        this.userId.sendKeys(userId);
        return this;
    }
}
