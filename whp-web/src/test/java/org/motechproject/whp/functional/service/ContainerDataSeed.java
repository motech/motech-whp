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

import static java.util.Arrays.asList;

public class ContainerDataSeed extends BaseFunctionalTestSeed {

    public ContainerDataSeed() {
        WebDriver webDriver = WebDriverFactory.getInstance();
        List<TestProvider> providers = providers(webDriver);
        for (TestProvider provider : providers) {
            patient(provider, webDriver);
        }
        registerContainer(new TestContainers().allRegisteredContainers(), providers.get(0), webDriver);
        registerContainer(asList(new TestContainers().containerWhichDoesNotMatchAutoCompleteField()), providers.get(1), webDriver);
        closeWebDriver(webDriver);
    }

    private void closeWebDriver(WebDriver webDriver) {
        webDriver.manage().deleteAllCookies();
        webDriver.close();
        webDriver.quit();
    }

    public TestContainers allContainers() {
        return new TestContainers();
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

    private void registerContainer(List<TestContainer> containers, TestProvider testProvider, WebDriver webDriver) {
        for (TestContainer testContainer : containers) {
            adjustDate(testContainer.getContainerIssuedDate(), "dd/MM/YYYY");
            AdminPage adminPage = LoginPage.fetch(webDriver).loginAsAdmin();
            ContainerRegistrationPage containerRegistrationPage = adminPage.navigateToContainerRegistrationPage();
            containerRegistrationPage.registerContainer(testContainer.getContainerId(), testProvider.getProviderId(), "Pre-treatment");
            containerRegistrationPage.logout();
        }
    }
}
