package org.motechproject.whp.functional.page;

import org.motechproject.whp.functional.framework.MyPageFactory;
import org.motechproject.whp.functional.framework.WHPUrl;
import org.motechproject.whp.functional.framework.WebDriverFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class LoginPage extends Page {
    public static final String LOGIN_URL = WHPUrl.baseFor("login");
    public static final String USERNAME_ID = "j_username";
    public static final String PASSWORD_ID = "j_password";
    public static final String ERROR_MESSAGE = "loginError";
    public static final String INCORRECT_USERNAME = "Incorrect";
    public static final String INCORRECT_PASSWORD = "Incorrect";
    public static final String CORRECT_USERNAME = "admin";
    public static final String CORRECT_PASSWORD = "password";
    public static final String FAILURE_MESSAGE = "The username or password you entered is incorrect. Please enter the correct credentials.";

    @FindBy(how = How.ID, using = USERNAME_ID)
    private WebElement userName;
    @FindBy(how = How.ID, using = PASSWORD_ID)
    private WebElement password;
    @FindBy(how = How.ID, using = ERROR_MESSAGE)
    private WebElement errorMessage;

    public LoginPage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    protected void waitForPageToLoad() {
        waitForElementWithIdToLoad("loginForm");
    }

    public void postInitialize() {
        userName = WebDriverFactory.createWebElement(userName);
        password = WebDriverFactory.createWebElement(password);
        errorMessage = WebDriverFactory.createWebElement(errorMessage);
    }

    public LoginPage loginWithIncorrectAdminUserNamePassword() {
        login(INCORRECT_USERNAME, INCORRECT_PASSWORD);
        return this;
    }

    public AdminPage loginWithCorrectAdminUserNamePassword() {
        return loginWithCorrectAdminUserAnd(CORRECT_PASSWORD);
    }

    public AdminPage loginWithCorrectAdminUserAnd(String password) {
        login(CORRECT_USERNAME, password);
        return MyPageFactory.initElements(webDriver, AdminPage.class);
    }

    private void login(String userName, String password) {
        this.userName.sendKeys(userName);
        this.password.sendKeys(password);
        this.userName.submit();
    }

    public String errorMessage() {
        waitForElementWithIdToLoad(ERROR_MESSAGE);
        return errorMessage.getText();
    }

    public ProviderPage loginWithProviderUserNamePassword(String username, String password) {
        login(username, password);
        return MyPageFactory.initElements(webDriver, ProviderPage.class);
    }
}
