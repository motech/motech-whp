package org.motechproject.whp.functional.page.admin.listprovider;

import org.motechproject.whp.functional.page.HtmlSection;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import static org.junit.Assert.assertEquals;

public class ProviderResetPassword extends HtmlSection {

    @FindBy(how = How.ID, using = "resetPassword")
    protected WebElement resetPasswordButton;

    @FindBy(how = How.ID, using = "resetPasswordClose")
    protected WebElement resetPasswordCancelButton;

    @FindBy(how = How.ID, using = "resetPasswordUserName")
    protected WebElement resetPasswordUserName;

    private ProviderPagination providerPagination;

    public ProviderResetPassword(WebDriver webDriver, ProviderPagination providerPagination) {
        super(webDriver);
        this.providerPagination = providerPagination;
    }

    public boolean hasResetPasswordLink(String providerId) {
        WebElement resetPasswordLink = getResetPasswordLink(providerId);
        return resetPasswordLink != null && resetPasswordLink.isDisplayed();
    }

    private WebElement getResetPasswordLink(String providerId) {
        return safeFindElementIn(getProviderRow(providerId), By.xpath("*//a[@type='reset-password-link']"));
    }

    public void resetPassword(String providerId) {
        openResetPasswordModal(providerId);
        assertEquals(providerId.toLowerCase(), resetPasswordUserName.getText());
        resetPasswordButton.click();
        waitForElementToBeReloadedByAjax();
    }

    public void openResetPasswordModal(String providerId) {
        getResetPasswordLink(providerId).click();
    }

    public void cancelResetPasswordDialog() {
        resetPasswordCancelButton.click();
    }

    private WebElement getProviderRow(String providerId) {
        return providerPagination.getProviderRow();

    }
}
