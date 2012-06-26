package org.motechproject.whp.functional.test.treatmentupdate;

import org.junit.runner.RunWith;
import org.motechproject.model.DayOfWeek;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.functional.data.TestProvider;
import org.motechproject.whp.functional.framework.BaseTest;
import org.motechproject.whp.functional.framework.MyPageFactory;
import org.motechproject.whp.functional.page.LoginPage;
import org.motechproject.whp.functional.page.ProviderPage;
import org.motechproject.whp.functional.service.ProviderDataService;
import org.motechproject.whp.patient.builder.PatientRequestBuilder;
import org.motechproject.whp.patient.command.UpdateCommandFactory;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.TreatmentCategory;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.webservice.service.PatientWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/applicationFunctionalTestContext.xml")
public abstract class TreatmentUpdateTest extends BaseTest {

    @Autowired
    protected PatientService patientService;
    @Autowired
    protected PatientWebService patientWebService;
    @Autowired
    protected UpdateCommandFactory factory;

    protected ProviderDataService providerDataService;
    protected PatientRequest patientRequest;
    protected TestProvider provider;

    @Override
    public void setUp() {
        super.setUp();
        setupProvider();
        setupPatientForProvider();
    }

    public void setupProvider() {
        providerDataService = new ProviderDataService(webDriver);
        provider = providerDataService.createProvider();
    }

    public void setupPatientForProvider() {
        TreatmentCategory oldCategory = new TreatmentCategory("RNTCP Category 1", "01", 3, 8, 18, 24, 54, Arrays.asList(DayOfWeek.Monday, DayOfWeek.Wednesday, DayOfWeek.Friday));
        patientRequest = new PatientRequestBuilder()
                .withDefaults()
                .withLastModifiedDate(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                .withTreatmentCategory(oldCategory)
                .withCaseId(UUID.randomUUID().toString())
                .withProviderId(provider.getProviderId())
                .build();
        patientService.createPatient(patientRequest);
    }

    protected ProviderPage loginAsProvider(TestProvider provider) {
        return MyPageFactory.initElements(webDriver, LoginPage.class).loginWithProviderUserNamePassword(provider.getProviderId(), provider.getPassword());
    }
}
