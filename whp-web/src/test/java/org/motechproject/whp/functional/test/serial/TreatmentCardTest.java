package org.motechproject.whp.functional.test.serial;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.model.DayOfWeek;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.functional.data.TestProvider;
import org.motechproject.whp.functional.framework.BaseTest;
import org.motechproject.whp.functional.framework.MyPageFactory;
import org.motechproject.whp.functional.page.*;
import org.motechproject.whp.functional.service.ProviderDataService;
import org.motechproject.whp.patient.builder.PatientRequestBuilder;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.TreatmentCategory;
import org.motechproject.whp.patient.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/applicationFunctionalTestContext.xml")
public class TreatmentCardTest extends BaseTest {

    @Autowired
    PatientService patientService;

    ProviderDataService providerDataService;

    PatientRequest patientRequest;
    TestProvider provider;

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

    @Test
    public void shouldBuildIPTreatmentCardForPatient() {
        /*Log Adherence data for patient */

        adjustDateTime(DateUtil.newDateTime(new LocalDate(2012, 7, 8)));

        UpdateAdherencePage updateAdherencePage = loginAsProvider().clickEditAdherenceLink(patientRequest.getCase_id());
        updateAdherencePage.setNumberOfDosesTaken(3);
        ProviderPage providerPage = updateAdherencePage.submit();

        adjustDateTime(DateUtil.newDateTime(new LocalDate(2012, 7, 15)));

        updateAdherencePage = providerPage.clickEditAdherenceLink(patientRequest.getCase_id());
        updateAdherencePage.setNumberOfDosesTaken(1);
        providerPage = updateAdherencePage.submit();

        adjustDateTime(DateUtil.newDateTime(new LocalDate(2012, 7, 29)));

        updateAdherencePage = providerPage.clickEditAdherenceLink(patientRequest.getCase_id());
        updateAdherencePage.setNumberOfDosesTaken(2);
        providerPage = updateAdherencePage.submit();
        providerPage.logout();

        ListPatientsPage listPatientsPage = loginAsCMFAdminAndListAllPatients();

        PatientDashboardPage patientDashboardPage = listPatientsPage.clickEditAdherenceForPatient(patientRequest.getCase_id());

         /* Asserting on adherence data given by provider */

        assertAdherenceDataOn(patientDashboardPage, "1", provider.getProviderId(), new LocalDate(2012, 7, 2));
        assertAdherenceDataOn(patientDashboardPage, "1", provider.getProviderId(), new LocalDate(2012, 7, 4));
        assertAdherenceDataOn(patientDashboardPage, "1", provider.getProviderId(), new LocalDate(2012, 7, 6));

        assertAdherenceDataOn(patientDashboardPage, "2", provider.getProviderId(), new LocalDate(2012, 7, 9));
        assertAdherenceDataOn(patientDashboardPage, "2", provider.getProviderId(), new LocalDate(2012, 7, 11));
        assertAdherenceDataOn(patientDashboardPage, "1", provider.getProviderId(), new LocalDate(2012, 7, 13));

        assertAdherenceDataOn(patientDashboardPage, "2", provider.getProviderId(), new LocalDate(2012, 7, 23));
        assertAdherenceDataOn(patientDashboardPage, "1", provider.getProviderId(), new LocalDate(2012, 7, 25));
        assertAdherenceDataOn(patientDashboardPage, "1", provider.getProviderId(), new LocalDate(2012, 7, 27));

        /* Asserting on blank adherence data */

        assertAdherenceDataOn(patientDashboardPage, "0", provider.getProviderId(), new LocalDate(2012, 7, 16));
        assertAdherenceDataOn(patientDashboardPage, "0", provider.getProviderId(), new LocalDate(2012, 7, 18));
        assertAdherenceDataOn(patientDashboardPage, "0", provider.getProviderId(), new LocalDate(2012, 7, 20));

        /* Asserting on dates that are not editable */

        assertAdherenceDataNotEvenEditableOn(patientDashboardPage, new LocalDate(2012, 7, 17));
        assertAdherenceDataNotEvenEditableOn(patientDashboardPage, new LocalDate(2012, 7, 19));
        assertAdherenceDataNotEvenEditableOn(patientDashboardPage, new LocalDate(2012, 7, 22));

        /* Asserting on dates that are not even present in the table*/

        assertDatesNotEvenPresent(patientDashboardPage, new LocalDate(2012, 6, 30));
        assertDatesNotEvenPresent(patientDashboardPage, new LocalDate(2012, 12, 1));
    }

    private void assertDatesNotEvenPresent(PatientDashboardPage patientDashboardPage, LocalDate localDate) {
        assertTrue(patientDashboardPage.dateNotPresent(localDate));
    }

    private void assertAdherenceDataNotEvenEditableOn(PatientDashboardPage patientDashboardPage, LocalDate localDate) {
        assertTrue(patientDashboardPage.nonEditableAdherenceOn(localDate));
    }

    private void assertAdherenceDataOn(PatientDashboardPage patientDashboardPage, String expectedAdherenceValue, String adherenceGivenBy, LocalDate doseDate) {
        assertEquals(expectedAdherenceValue, patientDashboardPage.adherenceStatusOn(doseDate));

        /* Meta data on Adherence Logs stores providerId in lower case */
        assertEquals(adherenceGivenBy.toLowerCase(), patientDashboardPage.adherenceOnProvidedBy(doseDate));
    }

    private ListPatientsPage loginAsCMFAdminAndListAllPatients() {
        return MyPageFactory.initElements(webDriver, LoginPage.class).loginWithCorrectAdminUserNamePassword().navigateToShowAllPatients();
    }

    private ProviderPage loginAsProvider() {
        return MyPageFactory.initElements(webDriver, LoginPage.class).loginWithProviderUserNamePassword(provider.getProviderId(), provider.getPassword());
    }

    @After
    public void tearDown() throws IOException {
        super.tearDown();
    }
}