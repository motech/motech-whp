package org.motechproject.whp.functional.service;

import org.motechproject.whp.functional.data.TestProvider;
import org.motechproject.whp.functional.page.ProviderActivatePage;
import org.motechproject.whp.functional.page.ProviderCreatePage;
import org.openqa.selenium.WebDriver;

import java.util.UUID;

public class ProviderDataService {

    private WebDriver webDriver;

    public ProviderDataService(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    // Leaves the browser in the Login Page
    public TestProvider createProvider() {
        return createProvider("Chambal");
    }

    public TestProvider createProvider(String district) {
        TestProvider testProvider = new TestProvider(generateId(), "password", district, "1234567890", "1234567890", "1234567890");
        ProviderCreatePage providerCreatePage = ProviderCreatePage.fetch(webDriver);
        providerCreatePage.createProviderWithLogin(testProvider);

        ProviderActivatePage providerActivatePage = ProviderActivatePage.fetch(webDriver);
        providerActivatePage.activateProvider(testProvider.getProviderId());
        return testProvider;
    }

    private String generateId() {
        return "testProvider-" + UUID.randomUUID();
    }
}
