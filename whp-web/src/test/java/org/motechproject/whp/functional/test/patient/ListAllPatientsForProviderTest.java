package org.motechproject.whp.functional.test.patient;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.functional.data.TestPatient;
import org.motechproject.whp.functional.data.TestProvider;
import org.motechproject.whp.functional.framework.BaseTest;
import org.motechproject.whp.functional.framework.MyPageFactory;
import org.motechproject.whp.functional.page.LoginPage;
import org.motechproject.whp.functional.page.ProviderPage;
import org.motechproject.whp.functional.service.PatientDataService;
import org.motechproject.whp.functional.service.ProviderDataService;
import org.motechproject.whp.patient.builder.PatientRequestBuilder;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ListAllPatientsForProviderTest extends BaseTest {

    TestProvider provider;

    TestPatient testPatient;

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

    @Test
    public void shouldLoginAsProviderAndListAllPatientsForProvider() {
        ProviderPage providerPage = loginAsProvider();
        assertTrue(providerPage.hasPatient(testPatient.getFirstName()));
        assertEquals("Male", providerPage.getGenderText(testPatient.getCaseId()));
        assertEquals("village", providerPage.getVillageText(testPatient.getCaseId()));
    }

    ProviderPage loginAsProvider() {
        return MyPageFactory.initElements(webDriver, LoginPage.class).loginWithProviderUserNamePassword(provider.getProviderId(), provider.getPassword());
    }

    @After
    public void tearDown() throws IOException {
        super.tearDown();
    }
}
