package org.motechproject.whp.functional.builder;


import org.motechproject.whp.functional.page.admin.listprovider.ListProvidersPage;
import org.openqa.selenium.WebDriver;

import static org.motechproject.whp.functional.framework.MyPageFactory.initElements;

public class ListAllProviderPageBuilder {

    private WebDriver driver;

    public ListAllProviderPageBuilder(WebDriver driver) {
        this.driver = driver;
    }

    public ListProvidersPage build(String providerId, boolean shouldReset) {
        ListProvidersPage providersPage = new ListProvidersPage(driver, providerId, true);
        initElements(driver, providersPage);
        return providersPage;
    }
}
