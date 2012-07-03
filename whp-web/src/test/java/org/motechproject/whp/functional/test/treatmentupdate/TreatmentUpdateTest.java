package org.motechproject.whp.functional.test.treatmentupdate;

import org.motechproject.whp.functional.data.TestPatient;
import org.motechproject.whp.functional.data.TestProvider;
import org.motechproject.whp.functional.framework.BaseTest;
import org.motechproject.whp.functional.framework.MyPageFactory;
import org.motechproject.whp.functional.page.LoginPage;
import org.motechproject.whp.functional.page.admin.AdminPage;
import org.motechproject.whp.functional.page.provider.ProviderPage;
import org.motechproject.whp.functional.service.CaseDataService;
import org.motechproject.whp.functional.service.PatientDataService;
import org.motechproject.whp.functional.service.ProviderDataService;

public abstract class TreatmentUpdateTest extends BaseTest {

    protected ProviderDataService providerDataService;
    protected PatientDataService patientDataService;
    protected CaseDataService caseDataService;
    protected TestProvider testProvider;
    protected TestPatient testPatient;

    @Override
    public void setUp() {
        super.setUp();
        setupProvider();
        setupPatientForProvider();
        caseDataService = new CaseDataService(webDriver);
    }

    public void setupProvider() {
        providerDataService = new ProviderDataService(webDriver);
        testProvider = providerDataService.createProvider();
    }

    public void setupPatientForProvider() {
        patientDataService = new PatientDataService(webDriver);
        testPatient = patientDataService.createPatient(testProvider.getProviderId(), "Foo", testProvider.getDistrict());
    }

    protected ProviderPage loginAsProvider(TestProvider provider) {
        return MyPageFactory.initElements(webDriver, LoginPage.class).loginWithProviderUserNamePassword(provider.getProviderId(), provider.getPassword());
    }

    protected AdminPage loginAsAdmin() {
        return MyPageFactory.initElements(webDriver, LoginPage.class).loginWithCorrectAdminUserNamePassword();
    }

}
