package org.motechproject.whp.functional.test.treatmentupdate;

import org.junit.runner.RunWith;
import org.motechproject.whp.functional.data.TestPatient;
import org.motechproject.whp.functional.data.TestProvider;
import org.motechproject.whp.functional.framework.BaseTest;
import org.motechproject.whp.functional.framework.MyPageFactory;
import org.motechproject.whp.functional.page.LoginPage;
import org.motechproject.whp.functional.page.ProviderPage;
import org.motechproject.whp.functional.service.CaseDataService;
import org.motechproject.whp.functional.service.PatientDataService;
import org.motechproject.whp.functional.service.ProviderDataService;
import org.motechproject.whp.patient.command.UpdateCommandFactory;
import org.motechproject.whp.webservice.service.PatientWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/applicationFunctionalTestContext.xml")
public abstract class TreatmentUpdateTest extends BaseTest {

    @Autowired
    protected PatientWebService patientWebService;
    @Autowired
    protected UpdateCommandFactory factory;

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
        testPatient = patientDataService.createPatient(testProvider.getProviderId(), "Foo");
    }

    protected ProviderPage loginAsProvider(TestProvider provider) {
        return MyPageFactory.initElements(webDriver, LoginPage.class).loginWithProviderUserNamePassword(provider.getProviderId(), provider.getPassword());
    }

}
