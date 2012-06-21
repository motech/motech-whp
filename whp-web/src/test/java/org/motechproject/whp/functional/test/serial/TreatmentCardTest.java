package org.motechproject.whp.functional.test.serial;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.builder.PatientWebRequestBuilder;
import org.motechproject.whp.contract.PatientWebRequest;
import org.motechproject.whp.functional.data.TestProvider;
import org.motechproject.whp.functional.framework.MyPageFactory;
import org.motechproject.whp.functional.page.*;
import org.motechproject.whp.functional.test.treatmentupdate.TreatmentUpdateTest;
import org.motechproject.whp.patient.builder.PatientRequestBuilder;
import org.motechproject.whp.patient.command.UpdateScope;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/applicationFunctionalTestContext.xml")
public class TreatmentCardTest extends TreatmentUpdateTest {

    @Test
    @Ignore
    public void shouldBuildIPTreatmentCardForPatient() {

        /*Log Adherence data for patient */

        adjustDateTime(DateUtil.newDateTime(new LocalDate(2012, 7, 8)));

        UpdateAdherencePage updateAdherencePage = loginAsProvider(provider).clickEditAdherenceLink(patientRequest.getCase_id());
        updateAdherencePage.setNumberOfDosesTaken(3);
        ProviderPage providerPage = updateAdherencePage.submit();

        adjustDateTime(DateUtil.newDateTime(new LocalDate(2012, 7, 15)));

        updateAdherencePage = providerPage.clickEditAdherenceLink(patientRequest.getCase_id());
        updateAdherencePage.setNumberOfDosesTaken(1);
        providerPage = updateAdherencePage.submit();

        /* Pausing and restarting treatment for patient */

        PatientRequest pauseTreatmentRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForPauseTreatment()
                .withTbId(patientRequest.getTb_id())
                .withCaseId(patientRequest.getCase_id())
                .withDateModified(DateUtil.newDateTime(2012, 7, 13, 0, 0, 0))
                .build();
        factory.updateFor(UpdateScope.pauseTreatment).apply(pauseTreatmentRequest);

        PatientRequest resumeTreatmentRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForRestartTreatment()
                .withTbId(patientRequest.getTb_id())
                .withCaseId(patientRequest.getCase_id())
                .withDateModified(DateUtil.newDateTime(2012, 7, 19, 0, 0, 0))
                .build();

        factory.updateFor(UpdateScope.restartTreatment).apply(resumeTreatmentRequest);

        adjustDateTime(DateUtil.newDateTime(new LocalDate(2012, 7, 29)));

        updateAdherencePage = providerPage.clickEditAdherenceLink(patientRequest.getCase_id());
        updateAdherencePage.setNumberOfDosesTaken(2);
        providerPage = updateAdherencePage.submit();
        providerPage.logout();

        /* Pausing treatment again */

        pauseTreatmentRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForPauseTreatment()
                .withTbId(patientRequest.getTb_id())
                .withCaseId(patientRequest.getCase_id())
                .withDateModified(DateUtil.newDateTime(2012, 7, 25, 0, 0, 0))
                .build();
        factory.updateFor(UpdateScope.pauseTreatment).apply(pauseTreatmentRequest);

        /* Transferring patient out to another provider (NB: treatment is still paused and has not been restarted)*/

        TestProvider newProvider = providerDataService.createProvider();

        PatientWebRequest closeTreatmentUpdateRequest = new PatientWebRequestBuilder()
                .withDefaultsForCloseTreatment()
                .withTreatmentOutcome("TransferredOut")
                .withTbId(patientRequest.getTb_id())
                .withCaseId(patientRequest.getCase_id())
                .withLastModifiedDate("28/07/2012 04:55:50")
                .build();
        patientWebService.updateCase(closeTreatmentUpdateRequest);

        PatientWebRequest transferInPatientRequest = new PatientWebRequestBuilder()
                .withDefaultsForTransferIn()
                .withProviderId(newProvider.getProviderId())
                .withTreatmentCategory("01")
                .withDiseaseClass(patientRequest.getDisease_class())
                .withCaseId(patientRequest.getCase_id())
                .withLastModifiedDate("29/07/2012 04:55:50")
                .build();
        patientWebService.updateCase(transferInPatientRequest);

        /* Logging adherence for patient as new provider */

        adjustDateTime(DateUtil.newDateTime(new LocalDate(2012, 8, 5)));

        updateAdherencePage = loginAsProvider(newProvider).clickEditAdherenceLink(patientRequest.getCase_id());
        updateAdherencePage.setNumberOfDosesTaken(3);
        providerPage = updateAdherencePage.submit();
        providerPage.logout();

        adjustDateTime(DateUtil.newDateTime(new LocalDate(2012, 8, 7)));

        /* Pausing treatment */

        pauseTreatmentRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForPauseTreatment()
                .withTbId(patientRequest.getTb_id())
                .withCaseId(patientRequest.getCase_id())
                .withDateModified(DateUtil.newDateTime(2012, 8, 7, 0, 0, 0))
                .build();
        factory.updateFor(UpdateScope.pauseTreatment).apply(pauseTreatmentRequest);

        adjustDateTime(DateUtil.newDateTime(new LocalDate(2012, 8, 12)));

        updateAdherencePage = loginAsProvider(newProvider).clickEditAdherenceLink(patientRequest.getCase_id());
        updateAdherencePage.setNumberOfDosesTaken(3);
        providerPage = updateAdherencePage.submit();
        providerPage.logout();

        ListPatientsPage listPatientsPage = loginAsCMFAdminAndListAllPatients();

        PatientDashboardPage patientDashboardPage = listPatientsPage.clickOnPatient(patientRequest.getCase_id());

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

        /* Asserting on adherence data given by newProvider */

        assertAdherenceDataOn(patientDashboardPage, "1", newProvider.getProviderId(), new LocalDate(2012, 7, 30));
        assertAdherenceDataOn(patientDashboardPage, "1", newProvider.getProviderId(), new LocalDate(2012, 8, 1));
        assertAdherenceDataOn(patientDashboardPage, "1", newProvider.getProviderId(), new LocalDate(2012, 8, 3));

        assertAdherenceDataOn(patientDashboardPage, "1", newProvider.getProviderId(), new LocalDate(2012, 8, 6));
        assertAdherenceDataOn(patientDashboardPage, "1", newProvider.getProviderId(), new LocalDate(2012, 8, 8));
        assertAdherenceDataOn(patientDashboardPage, "1", newProvider.getProviderId(), new LocalDate(2012, 8, 10));

        /* Asserting on blank adherence data */

        assertAdherenceDataOn(patientDashboardPage, "0", provider.getProviderId(), new LocalDate(2012, 7, 16));
        assertAdherenceDataOn(patientDashboardPage, "0", provider.getProviderId(), new LocalDate(2012, 7, 18));
        assertAdherenceDataOn(patientDashboardPage, "0", provider.getProviderId(), new LocalDate(2012, 7, 20));

        assertAdherenceDataOn(patientDashboardPage, "0", newProvider.getProviderId(), new LocalDate(2012, 8, 13));
        assertAdherenceDataOn(patientDashboardPage, "0", newProvider.getProviderId(), new LocalDate(2012, 8, 15));
        assertAdherenceDataOn(patientDashboardPage, "0", newProvider.getProviderId(), new LocalDate(2012, 8, 17));

        /* Asserting on paused dates */

        assertTreatmentPausedOn(patientDashboardPage, new LocalDate(2012, 7, 13));
        assertTreatmentPausedOn(patientDashboardPage, new LocalDate(2012, 7, 16));
        assertTreatmentPausedOn(patientDashboardPage, new LocalDate(2012, 7, 18));
        assertTreatmentPausedOn(patientDashboardPage, new LocalDate(2012, 7, 25));
        assertTreatmentPausedOn(patientDashboardPage, new LocalDate(2012, 7, 27));
        assertTreatmentPausedOn(patientDashboardPage, new LocalDate(2012, 8, 8));
        assertTreatmentPausedOn(patientDashboardPage, new LocalDate(2012, 8, 10));

        /* Asserting on dates not paused */

        assertTreatmentNotPausedOn(patientDashboardPage, new LocalDate(2012, 7, 30));
        assertTreatmentNotPausedOn(patientDashboardPage, new LocalDate(2012, 8, 1));
        assertTreatmentNotPausedOn(patientDashboardPage, new LocalDate(2012, 8, 3));
        assertTreatmentNotPausedOn(patientDashboardPage, new LocalDate(2012, 8, 6));

        /* Asserting on dates that are not editable */

        assertAdherenceDataNotEvenEditableOn(patientDashboardPage, new LocalDate(2012, 7, 17));
        assertAdherenceDataNotEvenEditableOn(patientDashboardPage, new LocalDate(2012, 7, 19));
        assertAdherenceDataNotEvenEditableOn(patientDashboardPage, new LocalDate(2012, 7, 22));

        assertAdherenceDataNotEvenEditableOn(patientDashboardPage, new LocalDate(2012, 7, 31));
        assertAdherenceDataNotEvenEditableOn(patientDashboardPage, new LocalDate(2012, 8, 9));
        assertAdherenceDataNotEvenEditableOn(patientDashboardPage, new LocalDate(2012, 8, 21));

        /* Asserting on dates that are not even present in the table*/

        assertDatesNotEvenPresent(patientDashboardPage, new LocalDate(2012, 6, 30));
        assertDatesNotEvenPresent(patientDashboardPage, new LocalDate(2012, 12, 1));

    }

    private void assertTreatmentNotPausedOn(PatientDashboardPage patientDashboardPage, LocalDate localDate) {
        assertFalse(patientDashboardPage.treatmentPausedOn(localDate));
    }

    private void assertTreatmentPausedOn(PatientDashboardPage patientDashboardPage, LocalDate localDate) {
        assertTrue(patientDashboardPage.treatmentPausedOn(localDate));
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

    @After
    public void tearDown() throws IOException {
        super.tearDown();
    }
}