package org.motechproject.whp.functional.test.serial;

import org.joda.time.LocalDate;
import org.junit.Ignore;
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

        String closeTreatmentRequest = CaseUpdate.CloseTreatmentRequest(testPatient.getCaseId(), "28/07/2012", testPatient.getTbId());
        caseDataService.updateCase(closeTreatmentRequest);

        String transferInTBId = "elevenDigit";
        String transferInPatientRequest = CaseUpdate.TransferInPatientRequest(testPatient.getCaseId(), "29/07/2012", transferInTBId, "01", newProvider.getProviderId());
        caseDataService.updateCase(transferInPatientRequest);

        /* Logging adherence for patient as new provider */

        adjustDateTime(DateUtil.newDateTime(new LocalDate(2012, 8, 5)));

        updateAdherencePage = loginAsProvider(newProvider).clickEditAdherenceLink(testPatient.getCaseId());
        updateAdherencePage.setNumberOfDosesTaken(3);
        providerPage = updateAdherencePage.submit();
        providerPage.logout();

        adjustDateTime(DateUtil.newDateTime(new LocalDate(2012, 8, 7)));

        /* Pausing treatment */

        pauseTreatmentRequest = CaseUpdate.PauseTreatmentRequest(testPatient.getCaseId(), "07/08/2012", transferInTBId, "paws");
        caseDataService.updateCase(pauseTreatmentRequest);

        adjustDateTime(DateUtil.newDateTime(new LocalDate(2012, 8, 12)));

        updateAdherencePage = loginAsProvider(newProvider).clickEditAdherenceLink(testPatient.getCaseId());
        updateAdherencePage.setNumberOfDosesTaken(3);
        providerPage = updateAdherencePage.submit();
        providerPage.logout();

        ListPatientsPage listPatientsPage = loginAsCMFAdminAndListAllPatients();

        TreatmentCardPage treatmentCardPage = listPatientsPage.clickOnPatientWithStartedTherapy(testPatient.getCaseId());

        /* Asserting on adherence data given by provider */

        assertAdherenceDataOn(treatmentCardPage, "1", testProvider.getProviderId(), new LocalDate(2012, 7, 2));
        assertAdherenceDataOn(treatmentCardPage, "1", testProvider.getProviderId(), new LocalDate(2012, 7, 4));
        assertAdherenceDataOn(treatmentCardPage, "1", testProvider.getProviderId(), new LocalDate(2012, 7, 6));

        assertAdherenceDataOn(treatmentCardPage, "2", testProvider.getProviderId(), new LocalDate(2012, 7, 9));
        assertAdherenceDataOn(treatmentCardPage, "2", testProvider.getProviderId(), new LocalDate(2012, 7, 11));
        assertAdherenceDataOn(treatmentCardPage, "1", testProvider.getProviderId(), new LocalDate(2012, 7, 13));

        assertAdherenceDataOn(treatmentCardPage, "2", testProvider.getProviderId(), new LocalDate(2012, 7, 23));
        assertAdherenceDataOn(treatmentCardPage, "1", testProvider.getProviderId(), new LocalDate(2012, 7, 25));
        assertAdherenceDataOn(treatmentCardPage, "1", testProvider.getProviderId(), new LocalDate(2012, 7, 27));

        /* Asserting on adherence data given by newProvider */

        assertAdherenceDataOn(treatmentCardPage, "1", newProvider.getProviderId(), new LocalDate(2012, 7, 30));
        assertAdherenceDataOn(treatmentCardPage, "1", newProvider.getProviderId(), new LocalDate(2012, 8, 1));
        assertAdherenceDataOn(treatmentCardPage, "1", newProvider.getProviderId(), new LocalDate(2012, 8, 3));

        assertAdherenceDataOn(treatmentCardPage, "1", newProvider.getProviderId(), new LocalDate(2012, 8, 6));
        assertAdherenceDataOn(treatmentCardPage, "1", newProvider.getProviderId(), new LocalDate(2012, 8, 8));
        assertAdherenceDataOn(treatmentCardPage, "1", newProvider.getProviderId(), new LocalDate(2012, 8, 10));

        /* Asserting on blank adherence data */

        assertAdherenceDataOn(treatmentCardPage, "0", testProvider.getProviderId(), new LocalDate(2012, 7, 16));
        assertAdherenceDataOn(treatmentCardPage, "0", testProvider.getProviderId(), new LocalDate(2012, 7, 18));
        assertAdherenceDataOn(treatmentCardPage, "0", testProvider.getProviderId(), new LocalDate(2012, 7, 20));

        /* Asserting on paused dates */

        assertTreatmentPausedOn(treatmentCardPage, new LocalDate(2012, 7, 13));
        assertTreatmentPausedOn(treatmentCardPage, new LocalDate(2012, 7, 16));
        assertTreatmentPausedOn(treatmentCardPage, new LocalDate(2012, 7, 18));
        assertTreatmentPausedOn(treatmentCardPage, new LocalDate(2012, 7, 25));
        assertTreatmentPausedOn(treatmentCardPage, new LocalDate(2012, 7, 27));
        assertTreatmentPausedOn(treatmentCardPage, new LocalDate(2012, 8, 8));
        assertTreatmentPausedOn(treatmentCardPage, new LocalDate(2012, 8, 10));

        /* Asserting on dates not paused */

        assertTreatmentNotPausedOn(treatmentCardPage, new LocalDate(2012, 7, 30));
        assertTreatmentNotPausedOn(treatmentCardPage, new LocalDate(2012, 8, 1));
        assertTreatmentNotPausedOn(treatmentCardPage, new LocalDate(2012, 8, 3));
        assertTreatmentNotPausedOn(treatmentCardPage, new LocalDate(2012, 8, 6));

        /* Asserting on dates that are not editable */

        assertAdherenceDataNotEvenEditableOn(treatmentCardPage, new LocalDate(2012, 7, 17));
        assertAdherenceDataNotEvenEditableOn(treatmentCardPage, new LocalDate(2012, 7, 19));
        assertAdherenceDataNotEvenEditableOn(treatmentCardPage, new LocalDate(2012, 7, 22));

        assertAdherenceDataNotEvenEditableOn(treatmentCardPage, new LocalDate(2012, 7, 31));
        assertAdherenceDataNotEvenEditableOn(treatmentCardPage, new LocalDate(2012, 8, 9));
        assertAdherenceDataNotEvenEditableOn(treatmentCardPage, new LocalDate(2012, 8, 21));

        /* Asserting on dates that are not even present in the table*/

        assertDatesNotEvenPresent(treatmentCardPage, new LocalDate(2012, 6, 30));
        assertDatesNotEvenPresent(treatmentCardPage, new LocalDate(2013, 1, 1));
    }

    private void assertTreatmentNotPausedOn(TreatmentCardPage treatmentCardPage, LocalDate localDate) {
        assertFalse(treatmentCardPage.treatmentPausedOn(localDate));
    }

    private void assertTreatmentPausedOn(TreatmentCardPage treatmentCardPage, LocalDate localDate) {
        assertTrue(treatmentCardPage.treatmentPausedOn(localDate));
    }

    private void assertDatesNotEvenPresent(TreatmentCardPage treatmentCardPage, LocalDate localDate) {
        assertTrue(treatmentCardPage.dateNotPresent(localDate));
    }

    private void assertAdherenceDataNotEvenEditableOn(TreatmentCardPage treatmentCardPage, LocalDate localDate) {
        assertTrue(treatmentCardPage.nonEditableAdherenceOn(localDate));
    }

    private void assertAdherenceDataOn(TreatmentCardPage treatmentCardPage, String expectedAdherenceValue, String adherenceGivenBy, LocalDate doseDate) {
        assertEquals(expectedAdherenceValue, treatmentCardPage.adherenceStatusOn(doseDate));

        /* Meta data on Adherence Logs stores providerId in lower case */
        assertEquals(adherenceGivenBy.toLowerCase(), treatmentCardPage.adherenceOnProvidedBy(doseDate));
    }

    private ListPatientsPage loginAsCMFAdminAndListAllPatients() {
        return MyPageFactory.initElements(webDriver, LoginPage.class).loginWithCorrectAdminUserNamePassword().navigateToShowAllPatients();
    }
}