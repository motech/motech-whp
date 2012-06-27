package org.motechproject.whp.functional.test.patient;

import org.junit.Test;
import org.motechproject.whp.functional.data.TestPatient;
import org.motechproject.whp.functional.data.TestProvider;
import org.motechproject.whp.functional.framework.BaseTest;
import org.motechproject.whp.functional.framework.MyPageFactory;
import org.motechproject.whp.functional.page.LoginPage;
import org.motechproject.whp.functional.page.ProviderPage;
import org.motechproject.whp.functional.service.PatientDataService;
import org.motechproject.whp.functional.service.ProviderDataService;

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
}
