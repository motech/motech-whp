package org.motechproject.whp.functional.page.admin;

import org.motechproject.whp.functional.framework.MyPageFactory;
import org.motechproject.whp.functional.page.LoggedInUserPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class ItAdminPage extends LoggedInUserPage {

    @FindBy(how = How.ID, using = "list-providers")
    private WebElement searchProvidersLink;

    public ItAdminPage(WebDriver webDriver) {
        super(webDriver);
    }

    public ListProvidersPage navigateToSearchProviders() {
        searchProvidersLink.click();
        return MyPageFactory.initElements(webDriver, ListProvidersPage.class);
    }
}
