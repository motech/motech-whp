package org.motechproject.whp.functional.page;

import org.motechproject.whp.functional.framework.MyPageFactory;
import org.motechproject.whp.functional.framework.WebDriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.seleniumhq.jetty7.security.SecurityHandler;

import java.util.Arrays;
import java.util.List;

import static org.motechproject.whp.functional.framework.WebDriverFactory.createWebElement;

public class LoggedInUserPage extends Page {

    private static final String CURRENT_PASSWORD_ID = "currentPassword";

    @FindBy(how = How.ID, using = "welcome-message")
    private WebElement welcomeDiv;

    @FindBy(how = How.LINK_TEXT, using = "Logout")
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
        currentPassword = createWebElement(currentPassword);
        newPassword = createWebElement(newPassword);
        confirmNewPassword = createWebElement(confirmNewPassword);
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
        currentPassword.submit();
        waitForElementToBeReloadedByAjax();
    }

    public List<String> getChangePasswordErrorMessages() {
        return Arrays.asList(changePasswordError.getText().split("\n"));
    }
    public String getServerErrorMessage(){
        return changePasswordServerSideError.getText();
    }

    @Override
    public void logout() {
        createWebElement(logoutLink).click();
    }
}
