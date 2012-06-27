package org.motechproject.whp.functional.test.serial;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.functional.data.CaseUpdate;
import org.motechproject.whp.functional.data.TestProvider;
import org.motechproject.whp.functional.framework.MyPageFactory;
import org.motechproject.whp.functional.page.*;
import org.motechproject.whp.functional.test.treatmentupdate.TreatmentUpdateTest;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class TreatmentCardTest extends TreatmentUpdateTest {

    @Test
    public void shouldBuildIPTreatmentCardForPatient() {

        adjustDateTime(DateUtil.newDateTime(new LocalDate(2012, 7, 8)));

        UpdateAdherencePage updateAdherencePage = loginAsProvider(testProvider).clickEditAdherenceLink(testPatient.getCaseId());
        updateAdherencePage.setNumberOfDosesTaken(3);
        ProviderPage providerPage = updateAdherencePage.submit();

        adjustDateTime(DateUtil.newDateTime(new LocalDate(2012, 7, 15)));

        updateAdherencePage = providerPage.clickEditAdherenceLink(testPatient.getCaseId());
        updateAdherencePage.setNumberOfDosesTaken(1);
        providerPage = updateAdherencePage.submit();

        /* Pausing and restarting treatment for patient */

        String pauseTreatmentRequest = CaseUpdate.PauseTreatmentRequest(testPatient.getCaseId(), "13/07/2012", testPatient.getTbId(), "paws");
        caseDataService.updateCase(pauseTreatmentRequest);

        String restartTreatmentRequest = CaseUpdate.RestartTreatmentRequest(testPatient.getCaseId(), "19/07/2012", testPatient.getTbId(), "swap");
        caseDataService.updateCase(restartTreatmentRequest);

        adjustDateTime(DateUtil.newDateTime(new LocalDate(2012, 7, 29)));

        updateAdherencePage = loginAsProvider(testProvider).clickEditAdherenceLink(testPatient.getCaseId());
        updateAdherencePage.setNumberOfDosesTaken(2);
        providerPage = updateAdherencePage.submit();
        providerPage.logout();

        /* Pausing treatment again */

        pauseTreatmentRequest = CaseUpdate.PauseTreatmentRequest(testPatient.getCaseId(), "25/07/2012", testPatient.getTbId(), "paws");
        caseDataService.updateCase(pauseTreatmentRequest);

        /* Transferring patient out to another provider (NB: treatment is still paused and has not been restarted)*/

        TestProvider newProvider = providerDataService.createProvider();

        String closeTreatmentRequest = CaseUpdate.CloseTreatmentRequest(testPatient.getCaseId(), testPatient.getTbId());
        caseDataService.updateCase(closeTreatmentRequest);

        String transferInTBId = "elevenDigit";
        String transferInPatientRequest = CaseUpdate.TransferInPatientRequest(testPatient.getCaseId(), transferInTBId, "01", newProvider.getProviderId());
        caseDataService.updateCase(transferInPatientRequest);

        /* Logging adherence for patient as new provider */

        adjustDateTime(DateUtil.newDateTime(new LocalDate(2012, 8, 5)));

        updateAdherencePage = loginAsProvider(newProvider).clickEditAdherenceLink(testPatient.getCaseId());
        updateAdherencePage.setNumberOfDosesTaken(3);
        providerPage = updateAdherencePage.submit();
        providerPage.logout();

        adjustDateTime(DateUtil.newDateTime(new LocalDate(2012, 8, 7)));

        /* Pausing treatment */

        pauseTreatmentRequest = CaseUpdate.PauseTreatmentRequest(testPatient.getCaseId(), "07/08/2012", testPatient.getTbId(), "paws");
        caseDataService.updateCase(pauseTreatmentRequest);

        adjustDateTime(DateUtil.newDateTime(new LocalDate(2012, 8, 12)));

        updateAdherencePage = loginAsProvider(newProvider).clickEditAdherenceLink(testPatient.getCaseId());
        updateAdherencePage.setNumberOfDosesTaken(3);
        providerPage = updateAdherencePage.submit();
        providerPage.logout();

        ListPatientsPage listPatientsPage = loginAsCMFAdminAndListAllPatients();

        PatientDashboardPage patientDashboardPage = listPatientsPage.clickOnPatient(testPatient.getCaseId());

        /* Asserting on adherence data given by provider */

        assertAdherenceDataOn(patientDashboardPage, "1", testProvider.getProviderId(), new LocalDate(2012, 7, 2));
        assertAdherenceDataOn(patientDashboardPage, "1", testProvider.getProviderId(), new LocalDate(2012, 7, 4));
        assertAdherenceDataOn(patientDashboardPage, "1", testProvider.getProviderId(), new LocalDate(2012, 7, 6));

        assertAdherenceDataOn(patientDashboardPage, "2", testProvider.getProviderId(), new LocalDate(2012, 7, 9));
        assertAdherenceDataOn(patientDashboardPage, "2", testProvider.getProviderId(), new LocalDate(2012, 7, 11));
        assertAdherenceDataOn(patientDashboardPage, "1", testProvider.getProviderId(), new LocalDate(2012, 7, 13));

        assertAdherenceDataOn(patientDashboardPage, "2", testProvider.getProviderId(), new LocalDate(2012, 7, 23));
        assertAdherenceDataOn(patientDashboardPage, "1", testProvider.getProviderId(), new LocalDate(2012, 7, 25));
        assertAdherenceDataOn(patientDashboardPage, "1", testProvider.getProviderId(), new LocalDate(2012, 7, 27));

        /* Asserting on adherence data given by newProvider */

        assertAdherenceDataOn(patientDashboardPage, "1", newProvider.getProviderId(), new LocalDate(2012, 7, 30));
        assertAdherenceDataOn(patientDashboardPage, "1", newProvider.getProviderId(), new LocalDate(2012, 8, 1));
        assertAdherenceDataOn(patientDashboardPage, "1", newProvider.getProviderId(), new LocalDate(2012, 8, 3));

        assertAdherenceDataOn(patientDashboardPage, "1", newProvider.getProviderId(), new LocalDate(2012, 8, 6));
        assertAdherenceDataOn(patientDashboardPage, "1", newProvider.getProviderId(), new LocalDate(2012, 8, 8));
        assertAdherenceDataOn(patientDashboardPage, "1", newProvider.getProviderId(), new LocalDate(2012, 8, 10));

        /* Asserting on blank adherence data */

        assertAdherenceDataOn(patientDashboardPage, "0", testProvider.getProviderId(), new LocalDate(2012, 7, 16));
        assertAdherenceDataOn(patientDashboardPage, "0", testProvider.getProviderId(), new LocalDate(2012, 7, 18));
        assertAdherenceDataOn(patientDashboardPage, "0", testProvider.getProviderId(), new LocalDate(2012, 7, 20));

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
        assertDatesNotEvenPresent(patientDashboardPage, new LocalDate(2013, 1, 1));
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
}