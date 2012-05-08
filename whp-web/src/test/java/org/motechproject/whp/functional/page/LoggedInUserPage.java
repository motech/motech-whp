package org.motechproject.whp.functional.page;

import org.motechproject.whp.functional.framework.WebDriverFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.util.Arrays;
import java.util.List;

public class LoggedInUserPage extends Page {

    private static final String CURRENT_PASSWORD_ID = "currentPassword";

    @FindBy(how = How.ID, using = "links")
    private WebElement welcomeDiv;

    @FindBy(how = How.ID, using = "logout")
    private WebElement logoutLink;

    @FindBy(how = How.ID, using = "changePasswordLink")
    private WebElement changePasswordLink;

    @FindBy(how = How.ID, using = CURRENT_PASSWORD_ID)
    private WebElement currentPassword;

    @FindBy(how = How.ID, using = "newPassword")
    private WebElement newPassword;

    @FindBy(how = How.ID, using = "confirmNewPassword")
    private WebElement confirmNewPassword;

    @FindBy(how = How.ID, using = "changePasswordError")
    private WebElement changePasswordError;

    public LoggedInUserPage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    protected void waitForPageToLoad() {
        waitForElementWithIdToLoad("links");
    }

    @Override
    public void postInitialize() {
        currentPassword = WebDriverFactory.createWebElement(currentPassword);
        newPassword = WebDriverFactory.createWebElement(newPassword);
        confirmNewPassword = WebDriverFactory.createWebElement(confirmNewPassword);
    }

    public String getWelcomeText() {
        return welcomeDiv.getText();
    }

    public LoggedInUserPage openChangePasswordModal() {
        changePasswordLink.click();
        waitForElementWithIdToLoad(CURRENT_PASSWORD_ID);
        return this;
    }

    public void changePassword(String currentPasswordText, String newPasswordText, String confirmNewPasswordText) {
        currentPassword.sendKeys(currentPasswordText);
        newPassword.sendKeys(newPasswordText);
        confirmNewPassword.sendKeys(confirmNewPasswordText);
        currentPassword.submit();
    }

    public List<String> getChangePasswordErrorMessages() {
        return Arrays.asList(changePasswordError.getText().split("\n"));
    }

    @Override
    public void logout() {
        logoutLink.click();
    }
}
