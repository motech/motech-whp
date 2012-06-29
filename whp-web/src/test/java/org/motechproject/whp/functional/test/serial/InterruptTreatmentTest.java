package org.motechproject.whp.functional.test.serial;

import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.functional.data.CaseUpdate;
import org.motechproject.whp.functional.page.provider.ProviderPage;
import org.motechproject.whp.functional.page.provider.UpdateAdherencePage;
import org.motechproject.whp.functional.test.treatmentupdate.TreatmentUpdateTest;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InterruptTreatmentTest extends TreatmentUpdateTest {

    @Test
    public void shouldDemarcateDaysForReportingAdherenceWhenTreatmentIsPaused() {
        ProviderPage providerPage = loginAsProvider(testProvider);
        assertTrue(providerPage.hasPatient(testPatient.getFirstName()));
        assertEquals("RNTCP Category 1", providerPage.getTreatmentCategoryText(testPatient.getCaseId()));

        String pauseTreatmentRequest = CaseUpdate.PauseTreatmentRequest(testPatient.getCaseId(), "07/05/2012", testPatient.getTbId(), "paws");
        caseDataService.updateCase(pauseTreatmentRequest);

        adjustDateTime(DateUtil.newDateTime(2012, 5, 8, 0, 0, 0));

        providerPage.logout();
        providerPage = loginAsProvider(testProvider);

        assertTrue(providerPage.isPatientTreatmentPaused(testPatient.getCaseId()));

        String restartTreatmentRequest = CaseUpdate.RestartTreatmentRequest(testPatient.getCaseId(), "09/05/2012", testPatient.getTbId(), "swap");
        caseDataService.updateCase(restartTreatmentRequest);

        adjustDateTime(DateUtil.newDateTime(2012, 5, 15, 0, 0, 0));

        providerPage.logout();
        providerPage = loginAsProvider(testProvider);

        assertFalse(providerPage.isPatientTreatmentPaused(testPatient.getCaseId()));

        UpdateAdherencePage updateAdherencePage = providerPage.clickEditAdherenceLink(testPatient.getCaseId());
        assertEquals("This patient has been restarted on medicines on 09-05-2012 after being paused on 07-05-2012. Reasons for pause: paws",
                updateAdherencePage.getAdherenceWarningText());
    }
}
