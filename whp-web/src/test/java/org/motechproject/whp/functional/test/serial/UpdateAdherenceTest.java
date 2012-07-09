package org.motechproject.whp.functional.test.serial;

import org.joda.time.DateTime;
import org.junit.Test;
import org.motechproject.whp.functional.data.TestPatient;
import org.motechproject.whp.functional.data.TestProvider;
import org.motechproject.whp.functional.framework.BaseTest;
import org.motechproject.whp.functional.framework.MyPageFactory;
import org.motechproject.whp.functional.page.LoginPage;
import org.motechproject.whp.functional.page.provider.ProviderPage;
import org.motechproject.whp.functional.page.provider.UpdateAdherencePage;
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
        providerDataService.activateProvider(provider.getProviderId());
    }

    public void setupPatientForProvider() {
        PatientDataService patientDataService = new PatientDataService(webDriver);
        testPatient = patientDataService.createPatient(provider.getProviderId(), "Foo", provider.getDistrict());
    }

    @Test
    public void shouldAllowUpdateAdherenceOnSunday() {
        adjustDateTime(DateTime.now().withDayOfWeek(7));

        UpdateAdherencePage updateAdherencePage = loginAsProvider().clickEditAdherenceLink(testPatient.getCaseId());
        updateAdherencePage.setNumberOfDosesTaken(3);

        ProviderPage providerPage = updateAdherencePage.submit();
        assertTrue(providerPage.adherenceLoggedForCurrentWeek(testPatient.getCaseId()));

        updateAdherencePage = providerPage.clickEditAdherenceLink(testPatient.getCaseId());
        boolean isTaken = updateAdherencePage.isDosesTaken(3);
        assertTrue(isTaken);
    }

    @Test
    public void shouldAllowMultipleUpdateToAdherence() {
        adjustDateTime(DateTime.now().withDayOfWeek(7));

        UpdateAdherencePage updateAdherencePage = loginAsProvider().clickEditAdherenceLink(testPatient.getCaseId());
        updateAdherencePage.setNumberOfDosesTaken(3);

        updateAdherencePage = updateAdherencePage.submit().clickEditAdherenceLink(testPatient.getCaseId());
        boolean isTaken = updateAdherencePage.isDosesTaken(3);
        assertTrue(isTaken);

        updateAdherencePage.setNumberOfDosesTaken(0);
        updateAdherencePage.submit().clickEditAdherenceLink(testPatient.getCaseId());
        isTaken = updateAdherencePage.isDosesTaken(0);

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