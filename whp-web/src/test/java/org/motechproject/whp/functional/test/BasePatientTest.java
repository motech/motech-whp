package org.motechproject.whp.functional.test;

import org.motechproject.whp.functional.data.TestPatient;
import org.motechproject.whp.functional.data.TestProvider;
import org.motechproject.whp.functional.framework.BaseTest;
import org.motechproject.whp.functional.framework.MyPageFactory;
import org.motechproject.whp.functional.page.LoginPage;
import org.motechproject.whp.functional.page.admin.ListAllPatientsPage;
import org.motechproject.whp.functional.service.PatientDataService;
import org.motechproject.whp.functional.service.ProviderDataService;

public abstract class BasePatientTest extends BaseTest {

    protected TestProvider provider;

    protected TestPatient testPatient;

    public void setupTestProvider(String district) {
        ProviderDataService providerDataService = new ProviderDataService(webDriver);
        provider = providerDataService.createProvider(district);
    }

    public void setupTestPatientForDistrict(String districtName) {
        PatientDataService patientDataService = new PatientDataService(webDriver);
        testPatient = patientDataService.createPatient(provider.getProviderId(), "Foo", districtName);
    }

    public void setupProvider() {
        ProviderDataService providerDataService = new ProviderDataService(webDriver);
        provider = providerDataService.createProvider();
    }

    public void setupPatientForProvider() {
        PatientDataService patientDataService = new PatientDataService(webDriver);
        testPatient = patientDataService.createPatient(provider.getProviderId(), "Foo", provider.getDistrict());
    }

    protected ListAllPatientsPage loginAsCMFAdmin() {
        return MyPageFactory.initElements(webDriver, LoginPage.class).loginWithCorrectAdminUserNamePassword().navigateToShowAllPatients();
    }
}
