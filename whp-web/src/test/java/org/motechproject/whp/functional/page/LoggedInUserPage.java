package org.motechproject.whp.functional.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.util.Arrays;
import java.util.List;

import static org.motechproject.whp.functional.framework.WebDriverFactory.createWebElement;

public class LoggedInUserPage extends Page {

    private static final String CURRENT_PASSWORD_ID = "currentPassword";

    @FindBy(how = How.ID, using = "welcome-message")
    private WebElement welcomeDiv;

    @FindBy(how = How.ID, using = "logout")
    private WebElement logoutLink;

    @FindBy(how = How.ID, using = "changePasswordLink")
    private WebElement changePasswordLink;

    @FindBy(how = How.ID, using = CURRENT_PASSWORD_ID)
    private WebElement currentPassword;

    @FindBy(how = How.ID, using = "changePassword")
    private WebElement changePassword;

    @FindBy(how = How.ID, using = "newPassword")
    private WebElement newPassword;

    @FindBy(how = How.ID, using = "confirmNewPassword")
    private WebElement confirmNewPassword;

    @FindBy(how = How.ID, using = "changePasswordError")
    private WebElement changePasswordError;

    @FindBy(how=How.ID, using = "changePasswordServerSideError")
    private WebElement changePasswordServerSideError;

    public LoggedInUserPage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    protected void waitForPageToLoad() {
        waitForElementWithIdToLoad("links");
        // Modified by Java script
        waitForElementWithIdToLoad("welcome-message");
    }

    @Override
    public void postInitialize() {
        welcomeDiv = createWebElement(welcomeDiv);
        logoutLink = createWebElement(logoutLink);
        changePasswordLink = createWebElement(changePasswordLink);
        currentPassword = createWebElement(currentPassword);
        newPassword = createWebElement(newPassword);
        confirmNewPassword = createWebElement(confirmNewPassword);
        changePassword = createWebElement(changePassword);
        changePasswordError = createWebElement(changePasswordError);
        changePasswordServerSideError = createWebElement(changePasswordServerSideError);
    }

    public String getWelcomeText() {
        return welcomeDiv.getText();
    }

    public LoggedInUserPage openChangePasswordModal() throws InterruptedException {
        changePasswordLink.click();
        waitForElementWithIdToLoad(CURRENT_PASSWORD_ID);

        /* Fade In time for modal */
        Thread.sleep(2000);

        return this;
    }

    public void changePassword(String currentPasswordText, String newPasswordText, String confirmNewPasswordText) {
        createWebElement(currentPassword).sendKeys(currentPasswordText);
        createWebElement(newPassword).sendKeys(newPasswordText);
        createWebElement(confirmNewPassword).sendKeys(confirmNewPasswordText);
        changePassword.click();
        waitForElementToBeReloadedByAjax();
    }

    public List<String> getChangePasswordErrorMessages() {
        return Arrays.asList(changePasswordError.getText().split("\n"));
    }
    public String getServerErrorMessage(){
        return changePasswordServerSideError.getText();
    }

}
