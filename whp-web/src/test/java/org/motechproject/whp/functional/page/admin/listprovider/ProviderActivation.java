package org.motechproject.whp.functional.page.admin.listprovider;

import org.motechproject.whp.functional.page.HtmlSection;
import org.openqa.selenium.By;
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

public class ProviderActivation extends HtmlSection {

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

    private ProviderPagination providerPagination;

    public ProviderActivation(WebDriver webDriver, ProviderPagination providerPagination) {
        super(webDriver);
        this.providerPagination = providerPagination;
    }

    public boolean hasActivateButton(String providerId) {
        return getActivateLink(providerId) != null;
    }

    public void openActivateProviderModal(String providerId) throws InterruptedException {
        WebElement activateButton = getActivateLink(providerId);
        activateButton.click();
        Thread.sleep(2000);
    }

    public void activateProvider(String providerId, String password) throws InterruptedException {
        openActivateProviderModal(providerId);
        assertEquals(activateProviderUserName.getText(), providerId.toLowerCase());
        createWebElement(newPassword).sendKeys(password);
        createWebElement(confirmNewPassword).sendKeys(password);
        createWebElement(activateProviderButton).click();
        waitForElementToBeReloadedByAjax();
    }

    public boolean isProviderActive(String providerId) {
        if (getActivateLink(providerId) != null && hasActiveStatus(providerId))
            return true;
        return false;
    }

    public boolean hasActiveStatus(String providerId) {
        return safeFindElementIn(getProviderRow(), By.xpath(".//td[@type='status']")).getText().compareToIgnoreCase("Active") == 0;
    }

    public void validateEmptyPasswordOnActivation() {
        newPassword.clear();
        confirmNewPassword.clear();
        createWebElement(activateProviderButton).click();
        assertEquals(asList("'Password' cannot be empty", "'Confirm Password' cannot be empty"), getActivateProviderError());
    }

    public void validateConfirmPasswordUponActivation(String password) {
        newPassword.clear();
        confirmNewPassword.clear();
        newPassword.sendKeys(password);
        confirmNewPassword.sendKeys(password + "12");
        activateProviderButton.click();
        assertEquals("'Confirm Password' should match 'Password'", getActivateProviderError().get(0));
    }

    public void validatePasswordLengthUponActivation() {
        newPassword.clear();
        confirmNewPassword.clear();
        newPassword.sendKeys("123");
        activateProviderButton.click();
        assertEquals("'Password' should be at least 4 characters long", getActivateProviderError().get(0));
    }

    public void closeProviderActivationModal() throws InterruptedException {
        activateProviderCloseButton.click();
        Thread.sleep(2000);
    }

    public void validateValidPasswordUponActivation(String password) {
        newPassword.clear();
        confirmNewPassword.clear();
        newPassword.sendKeys(password);
        confirmNewPassword.sendKeys(password);
        assertEquals(0, getActivateProviderError().size());
    }

    private WebElement getActivateLink(String providerId) {
        return safeFindElementIn(getProviderRow(), By.xpath(".//a[@type='activate-link']"));
    }

    private WebElement getProviderRow() {
        return providerPagination.getProviderRow();
    }

    private List<String> getActivateProviderError() {
        List<WebElement> resetPasswordErrorElements = activateProviderError.findElements(By.tagName("label"));
        List<String> errors = new ArrayList<>();
        for (WebElement error : resetPasswordErrorElements) {
            if (isNotBlank(error.getText()))
                errors.add(error.getText());
        }
        return errors;
    }
}
