package org.motechproject.whp.functional.service;

import org.motechproject.whp.functional.data.TestContainer;
import org.motechproject.whp.functional.data.TestContainers;
import org.motechproject.whp.functional.data.TestPatient;
import org.motechproject.whp.functional.data.TestProvider;
import org.motechproject.whp.functional.framework.WebDriverFactory;
import org.motechproject.whp.functional.page.LoginPage;
import org.motechproject.whp.functional.page.admin.AdminPage;
import org.motechproject.whp.functional.page.admin.ContainerRegistrationPage;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;

public class ContainerDataSeed {

    private List<TestContainer> allContainers;

    public ContainerDataSeed() {
        allContainers = new ArrayList<>();
        WebDriver webDriver = WebDriverFactory.getInstance();
        List<TestProvider> providers = providers(webDriver);
        for (TestProvider provider : providers) {
            patient(provider, webDriver);
        }
        allContainers.addAll(registerContainer(providers.get(0), webDriver));
        closeWebDriver(webDriver);
    }

    private void closeWebDriver(WebDriver webDriver) {
        webDriver.manage().deleteAllCookies();
        webDriver.close();
        webDriver.quit();
    }

    public List<TestContainer> allContainers() {
        return allContainers;
    }

    private List<TestProvider> providers(WebDriver webDriver) {
        List<TestProvider> providers = new ArrayList<>();
        ProviderDataService providerDataService = new ProviderDataService(webDriver);
        providers.add(providerDataService.createProvider("Begusarai"));
        providers.add(providerDataService.createProvider("Vaishali"));
        return providers;
    }

    private TestPatient patient(TestProvider provider, WebDriver webDriver) {
        PatientDataService patientDataService = new PatientDataService(webDriver);
        return patientDataService.createPatient(provider.getProviderId(), "name", provider.getDistrict());
    }

    private List<TestContainer> registerContainer(TestProvider testProvider, WebDriver webDriver) {
        List<TestContainer> containers = new ArrayList<>();
        for (TestContainer testContainer : new TestContainers().allRegisteredContainers()) {
            AdminPage adminPage = LoginPage.fetch(webDriver).loginAsAdmin();
            ContainerRegistrationPage containerRegistrationPage = adminPage.navigateToContainerRegistrationPage();
            containerRegistrationPage.registerContainer(testContainer.getContainerId(), testProvider.getProviderId(), "Pre-treatment");
            containerRegistrationPage.logout();
        }
        return containers;
    }
}
