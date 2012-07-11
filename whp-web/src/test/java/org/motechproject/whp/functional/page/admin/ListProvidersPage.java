package org.motechproject.whp.functional.page.admin;

import org.motechproject.whp.functional.framework.MyPageFactory;
import org.motechproject.whp.functional.framework.WHPUrl;
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

    @FindBy(how = How.ID, using = "activateProviderUserNameLabel")
    protected WebElement activateProviderUserName;

    @FindBy(how = How.ID, using = "activateProviderNewPassword")
    protected WebElement newPassword;

    @FindBy(how = How.ID, using = "activateProviderConfirmNewPassword")
    protected WebElement confirmNewPassword;

    @FindBy(how = How.ID, using = "activateProvider")
    protected WebElement activateProviderButton;

    @FindBy(how = How.ID, using = "activateProviderError")
    protected WebElement activateProviderError;

    @FindBy(how = How.ID, using = "activateProviderClose")
    protected WebElement activateProviderCloseButton;

    @FindBy(how = How.ID, using = "resetPassword")
    protected WebElement resetPasswordButton;

    @FindBy(how = How.ID, using = "resetPasswordClose")
    protected WebElement resetPasswordCancelButton;

    @FindBy(how = How.ID, using = "resetPasswordUserName")
    protected WebElement resetPasswordUserName;

    @FindBy(how = How.CLASS_NAME, using = "provider-row")
    protected List<WebElement> providers;

    @FindBy(how = How.ID, using = "cmf-admins")
    private WebElement searchCmfAdminsLink;

    public ListProvidersPage(WebDriver webDriver) {
        super(webDriver);
    }

    public static ListProvidersPage fetch(WebDriver webDriver) {
        webDriver.get(WHPUrl.baseFor("provider/list"));
        return MyPageFactory.initElements(webDriver, ListProvidersPage.class);
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
        return safeFindElement(By.xpath(String.format("//tr[@providerId='%s']", providerId.toLowerCase())));
    }

    public ListProvidersPage searchBy(String district, String providerId, boolean expectingResult) {
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
        return this;
    }

    public String getWarningText() {
        return warning.getText();
    }

    public boolean isProviderActive(String providerId) {
        if (getActivateLink(providerId) == null && hasActiveStatus(providerId))
            return true;
        return false;
    }

    public boolean hasActiveStatus(String providerId) {
        return safeFindElementIn(getProviderRow(providerId), By.xpath(".//td[@type='status']")).getText().compareToIgnoreCase("Active") == 0;
    }

    public boolean hasActivateButton(String providerId) {
        return getActivateLink(providerId) != null;
    }
    public ListProvidersPage activateProvider(String providerId,String password) throws InterruptedException {
        openActivateProviderModal(providerId);
        assertEquals(activateProviderUserName.getText(), providerId.toLowerCase());
        createWebElement(newPassword).sendKeys(password);
        createWebElement(confirmNewPassword).sendKeys(password);
        createWebElement(activateProviderButton).click();
        waitForElementToBeReloadedByAjax();
        return this;
    }

    public ListProvidersPage validateEmptyPasswordOnActivation() {
        newPassword.clear();
        confirmNewPassword.clear();
        createWebElement(activateProviderButton).click();
        assertEquals(asList("'Password' cannot be empty","'Confirm Password' cannot be empty"), getActivateProviderError());
        return this;
    }

    public ListProvidersPage validateValidPasswordUponActivation(String password) {
        newPassword.clear();
        confirmNewPassword.clear();
        newPassword.sendKeys(password);
        confirmNewPassword.sendKeys(password);
        assertEquals(0, getActivateProviderError().size());
        return this;
    }

    public ListProvidersPage validateConfirmPasswordUponActivation(String password) {
        newPassword.clear();
        confirmNewPassword.clear();
        newPassword.sendKeys(password);
        confirmNewPassword.sendKeys(password+"12");
        activateProviderButton.click();
        assertEquals("'Confirm Password' should match 'Password'", getActivateProviderError().get(0));
        return this;
    }

    public ListProvidersPage validatePasswordLengthUponActivation() {
        newPassword.clear();
        confirmNewPassword.clear();
        newPassword.sendKeys("123");
        activateProviderButton.click();
        assertEquals("'Password' should be at least 4 characters long", getActivateProviderError().get(0));
        return this;
    }

    public void closeProviderActivationModal() throws InterruptedException {
        activateProviderCloseButton.click();
        Thread.sleep(2000);
    }

    public ListProvidersPage openActivateProviderModal(String providerId) throws InterruptedException {
        WebElement activateButton = getActivateLink(providerId);
        activateButton.click();
        Thread.sleep(2000);
        return this;
    }

    private List<String> getActivateProviderError() {

        List<WebElement> resetPasswordErrorElements = activateProviderError.findElements(By.tagName("label"));
        List<String> errors = new ArrayList<>();
        for(WebElement error : resetPasswordErrorElements) {
            if(isNotBlank(error.getText()))
                errors.add(error.getText());
        }
        return errors;
    }

    private WebElement getActivateLink(String providerId) {
        return safeFindElementIn(getProviderRow(providerId), By.xpath(".//a[@type='activate-link']"));
    }

    public boolean hasResetPasswordLink(String providerId) {
        return getResetPasswordLink(providerId) != null;
    }

    private WebElement getResetPasswordLink(String providerId) {
        return safeFindElementIn(getProviderRow(providerId), By.xpath(".//a[@type='reset-password-link']"));
    }

    public ListProvidersPage resetPassword(String providerId) {
        openResetPasswordModal(providerId);
        assertEquals(providerId.toLowerCase(),resetPasswordUserName.getText());
        resetPasswordButton.click();
        waitForElementToBeReloadedByAjax();
        return this;
    }

    public ListProvidersPage openResetPasswordModal(String providerId) {
        getResetPasswordLink(providerId).click();
        return this;
    }

    public ListProvidersPage cancelResetPasswordDialog() {
        resetPasswordCancelButton.click();
        return this;
    }
    public ListAllCmfAdminsPage navigateToSearchCmfAdmins() {
        searchCmfAdminsLink.click();
        return getListAllCmfAdminsPage(webDriver);
    }
}
