package org.motechproject.whp.functional.page.admin;

import org.motechproject.whp.functional.framework.WebDriverFactory;
import org.motechproject.whp.functional.page.LoggedInUserPage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.junit.Assert.assertEquals;
import static org.motechproject.whp.functional.framework.WebDriverFactory.createWebElement;

public class ListProvidersPage extends LoggedInUserPage {

    @FindBy(how = How.ID, using = "providerId")
    protected WebElement providerId;

    @FindBy(how = How.ID, using = "search")
    protected WebElement submit;

    @FindBy(how = How.CLASS_NAME, using = "warning")
    protected WebElement warning;

    @FindBy(how = How.ID, using = "resetPasswordUserNameLabel")
    protected WebElement resetPasswordUserName;

    @FindBy(how = How.ID, using = "resetPasswordNewPassword")
    protected WebElement newPassword;

    @FindBy(how = How.ID, using = "resetPasswordConfirmNewPassword")
    protected WebElement confirmNewPassword;

    @FindBy(how = How.ID, using = "resetPassword")
    protected WebElement resetPasswordButton;

    @FindBy(how = How.ID, using = "resetPasswordError")
    protected WebElement resetPasswordError;

    @FindBy(how = How.ID, using = "resetPasswordClose")
    protected WebElement resetPasswordCloseButton;

    public ListProvidersPage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    public void postInitialize() {
        super.postInitialize();
        providerId = WebDriverFactory.createWebElement(providerId);
        submit = WebDriverFactory.createWebElement(submit);
        warning = WebDriverFactory.createWebElement(warning);
    }

    @Override
    protected void waitForPageToLoad() {
        waitForElementWithIdToLoad("search");
    }

    public boolean hasProviderRow(String providerId) {
        return getProviderRow(providerId) != null;
    }

    private WebElement getProviderRow(String providerId) {
        return safeFindElement(By.xpath(String.format("//tr[@providerid='%s']", providerId.toLowerCase())));
    }

    public void searchBy(String district, String providerId, boolean expectingResult) {
        this.providerId.clear();
        this.providerId.sendKeys(providerId.toLowerCase());
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) webDriver;
        javascriptExecutor.executeScript("$('#district').val('" + district + "');"); // can't set select box directly as it is hidden
        this.submit.click();
        if (expectingResult) {
            waitForElementWithCSSToLoad("provider-row");
        } else {
            waitForElementWithCSSToLoad("warning");
        }
    }

    public String getWarningText() {
        return warning.getText();
    }

    public boolean isProviderActive(String providerId) {
        if (getActivateButton(providerId) == null && hasActiveStatus(providerId))
            return true;
        return false;
    }

    public boolean hasActiveStatus(String providerId) {
        return safeFindElementIn(getProviderRow(providerId), By.xpath(".//td[@type='status']")).getText().compareToIgnoreCase("Active") == 0;
    }

    public boolean hasActivateButton(String providerId) {
        return getActivateButton(providerId) != null;
    }
    public void activateProvider(String providerId,String password) throws InterruptedException {
        openResetPasswordModal(providerId);
        assertEquals(resetPasswordUserName.getText(), providerId.toLowerCase());
        createWebElement(newPassword).sendKeys(password);
        createWebElement(confirmNewPassword).sendKeys(password);
        createWebElement(resetPasswordButton).click();
        waitForElementToBeReloadedByAjax();
    }

    public ListProvidersPage validateEmptyResetPassword() {
        newPassword.clear();
        confirmNewPassword.clear();
        createWebElement(resetPasswordButton).click();
        assertEquals(asList("'New Password' cannot be empty","'Confirm New Password' cannot be empty"),getResetPasswordError());
        return this;
    }

    public ListProvidersPage validateForResetPasswordErrorFreeFlow(String password) {
        newPassword.clear();
        confirmNewPassword.clear();
        newPassword.sendKeys(password);
        confirmNewPassword.sendKeys(password);
        assertEquals(0,getResetPasswordError().size());
        return this;
    }

    public ListProvidersPage validateConfirmPasswordMatchWithNewPassword(String password) {
        newPassword.clear();
        confirmNewPassword.clear();
        newPassword.sendKeys(password);
        confirmNewPassword.sendKeys(password+"12");
        resetPasswordButton.click();
        assertEquals("'Confirm New Password' should match 'New Password'",getResetPasswordError().get(0));
        return this;
    }

    public ListProvidersPage validateResetPasswordLength() {
        newPassword.clear();
        confirmNewPassword.clear();
        newPassword.sendKeys("123");
        resetPasswordButton.click();
        assertEquals("'New Password' should be at least 4 characters long",getResetPasswordError().get(0));
        return this;
    }

    public void closeResetPasswordDialog() throws InterruptedException {
        resetPasswordCloseButton.click();
        Thread.sleep(2000);
    }

    public ListProvidersPage openResetPasswordModal(String providerId) throws InterruptedException {
        WebElement activateButton = getActivateButton(providerId);
        activateButton.click();
        Thread.sleep(2000);
        return this;
    }

    private List<String> getResetPasswordError() {

        List<WebElement> resetPasswordErrorElements = resetPasswordError.findElements(By.tagName("label"));
        List<String> errors = new ArrayList<>();
        for(WebElement error : resetPasswordErrorElements) {
            if(isNotBlank(error.getText()))
                errors.add(error.getText());
        }
        return errors;
    }

    private WebElement getActivateButton(String providerId) {
        return safeFindElementIn(getProviderRow(providerId), By.xpath(".//a[@type='activate-button']"));
    }
}
