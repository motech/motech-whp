package org.motechproject.whp.functional.test;

import org.motechproject.whp.functional.data.TestPatient;
import org.motechproject.whp.functional.data.TestProvider;
import org.motechproject.whp.functional.framework.BaseTest;
import org.motechproject.whp.functional.framework.MyPageFactory;
import org.motechproject.whp.functional.page.provider.ListPatientsPage;
import org.motechproject.whp.functional.page.LoginPage;
import org.motechproject.whp.functional.service.PatientDataService;
import org.motechproject.whp.functional.service.ProviderDataService;

public abstract class BasePatientTest extends BaseTest {

    protected TestProvider provider;

    protected TestPatient testPatient;

    @Override
    public void setUp() {
        super.setUp();
        setupProvider();
        setupPatientForProvider();
    }

    public void setupProvider() {
        ProviderDataService providerDataService = new ProviderDataService(webDriver);
        provider = providerDataService.createProvider();
    }

    public void setupPatientForProvider() {
        PatientDataService patientDataService = new PatientDataService(webDriver);
        testPatient = patientDataService.createPatient(provider.getProviderId(), "Foo");
    }

    protected ListPatientsPage loginAsCMFAdmin() {
        return MyPageFactory.initElements(webDriver, LoginPage.class).loginWithCorrectAdminUserNamePassword().navigateToShowAllPatients();
    }
}
