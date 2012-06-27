package org.motechproject.whp.functional.test.serial;

import org.joda.time.DateTime;
import org.junit.Test;
import org.motechproject.whp.functional.data.TestPatient;
import org.motechproject.whp.functional.data.TestProvider;
import org.motechproject.whp.functional.framework.BaseTest;
import org.motechproject.whp.functional.framework.MyPageFactory;
import org.motechproject.whp.functional.page.LoginPage;
import org.motechproject.whp.functional.page.ProviderPage;
import org.motechproject.whp.functional.page.UpdateAdherencePage;
import org.motechproject.whp.functional.service.PatientDataService;
import org.motechproject.whp.functional.service.ProviderDataService;

import static org.junit.Assert.assertTrue;

public class UpdateAdherenceTest extends BaseTest {

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
    public void shouldAllowUpdateAdherenceOnSunday() {
        adjustDateTime(DateTime.now().withDayOfWeek(7));

        UpdateAdherencePage updateAdherencePage = loginAsProvider().clickEditAdherenceLink(testPatient.getCaseId());
        assertTrue(updateAdherencePage.getAdherenceCaption().contains(testPatient.getCaseId()));
        updateAdherencePage.setNumberOfDosesTaken(1);
        boolean isTaken = updateAdherencePage.submit().clickEditAdherenceLink(testPatient.getCaseId()).isDosesTaken(1);
        assertTrue(isTaken);
    }

    @Test
    public void shouldNotAllowUpdateAdherenceOnWednesday() {
        adjustDateTime(DateTime.now().withDayOfWeek(3));

        boolean isReadOnly = loginAsProvider().clickEditAdherenceLink(testPatient.getCaseId()).isReadOnly();
        assertTrue(isReadOnly);
    }

    ProviderPage loginAsProvider() {
        return MyPageFactory.initElements(webDriver, LoginPage.class).loginWithProviderUserNamePassword(provider.getProviderId(), provider.getPassword());
    }
}