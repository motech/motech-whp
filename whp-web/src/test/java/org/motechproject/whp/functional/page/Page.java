package org.motechproject.whp.functional.page;

import org.motechproject.whp.functional.framework.MyPageFactory;
import org.motechproject.whp.functional.page.admin.CreateCmfAdminPage;
import org.motechproject.whp.functional.page.admin.ListAllCmfAdminsPage;
import org.motechproject.whp.functional.page.provider.ProviderPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.motechproject.whp.functional.framework.WebDriverFactory.createWebElement;

public abstract class Page extends HtmlSection {

    private Logger logger = LoggerFactory.getLogger(Page.class);

    @FindBy(how = How.ID, using = "logout")
    private WebElement logoutLink;

    public Page(WebDriver webDriver) {
        super(webDriver);
        this.waitForPageToLoad();
    }

    protected abstract void waitForPageToLoad();

    public LoginPage logout() {
        createWebElement(logoutLink).click();
        waitUntilElementIsNotPresent(By.id("logout"));
        return getLoginPage(webDriver);
    }

    public static LoginPage getLoginPage(WebDriver webDriver) {
        return MyPageFactory.initElements(webDriver, LoginPage.class);
    }

    public static ProviderPage getProviderPage(WebDriver webDriver) {
        return MyPageFactory.initElements(webDriver, ProviderPage.class);
    }

    public static CreateCmfAdminPage getCreateCmfPage(WebDriver webDriver) {
        return MyPageFactory.initElements(webDriver, CreateCmfAdminPage.class);
    }

    public static ListAllCmfAdminsPage getListAllCmfAdminsPage(WebDriver webDriver) {
        return MyPageFactory.initElements(webDriver, ListAllCmfAdminsPage.class);
    }

}
