package org.motechproject.whp.functional.page.admin;

import org.motechproject.whp.functional.framework.MyPageFactory;
import org.motechproject.whp.functional.framework.WHPUrl;
import org.motechproject.whp.functional.framework.WebDriverFactory;
import org.motechproject.whp.functional.page.LoggedInUserPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.motechproject.whp.functional.framework.WebDriverFactory.createWebElement;

public class ListAllCmfAdminsPage extends LoggedInUserPage{
    @FindBy(how = How.ID, using = "resetPassword")
    protected WebElement resetPasswordButton;

    @FindBy(how = How.ID, using = "resetPasswordClose")
    protected WebElement resetPasswordCancelButton;

    @FindBy(how = How.ID, using = "resetPasswordUserName")
    protected WebElement resetPasswordUserName;

    @FindBy(how = How.ID, using = "createCmfAdmin-button")
    private WebElement createCmfAdminButton;

    @FindBy(how = How.ID, using = "list-providers")
    private WebElement searchProvidersLink;

    public ListAllCmfAdminsPage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    public void postInitialize() {
        super.postInitialize();
        createCmfAdminButton = WebDriverFactory.createWebElement(createCmfAdminButton);
    }

    public ListAllCmfAdminsPage resetPassword(String userId) {
        openResetPasswordDialog(userId);
        assertEquals(userId.toLowerCase(),resetPasswordUserName.getText());
        resetPasswordButton.click();
        waitForElementToBeReloadedByAjax();
        return this;
    }

    public WebElement getCmfAdminRow(String userId) {
        return safeFindElement(By.xpath(String.format("//tr[@rowId='%s']", userId.toLowerCase())));
    }

    private WebElement getResetPasswordLink(String userId) {
        return safeFindElementIn(getCmfAdminRow(userId), By.xpath(".//a[@type='reset-password-link']"));
    }

    public ListAllCmfAdminsPage openResetPasswordDialog(String providerId) {
        getResetPasswordLink(providerId).click();
        return this;
    }

    public ListAllCmfAdminsPage cancelResetPasswordDialog() {
        resetPasswordCancelButton.click();
        return this;
    }

    public static ListAllCmfAdminsPage fetch(WebDriver webDriver) {
        webDriver.get(WHPUrl.baseFor("cmfadmin/list"));
        return MyPageFactory.initElements(webDriver, ListAllCmfAdminsPage.class);
    }

    public CreateCmfAdminPage navigateToCreateCmfAdminPage() {
        createCmfAdminButton.click();
        return getCreateCmfPage(webDriver);
    }

    public EditCmfAdminPage getEditCmfAdminPage(String userId) {
        clickOnCmfAdminRow(userId.toLowerCase());
        return EditCmfAdminPage.fetch(webDriver);
    }

    private void clickOnCmfAdminRow(String userId) {
        waitForElementWithCSSToLoad("cmfadmin-row");
        List<WebElement> userIds = webDriver.findElements(By.className("userId"));
        int index = -1;
        for (int i = 0; i < userIds.size(); i++) {
            if (userIds.get(i).getText().equals(userId)) {
                index = i;
                break;
            }
        }
        List<WebElement> links = webDriver.findElements(By.className("cmfadmin-row"));
        createWebElement(links.get(index).findElement(By.className("staffName"))).click();
    }


}
