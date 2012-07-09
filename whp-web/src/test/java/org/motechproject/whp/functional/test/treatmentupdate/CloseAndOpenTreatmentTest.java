package org.motechproject.whp.functional.test.treatmentupdate;

import org.junit.Test;
import org.motechproject.whp.functional.data.CaseUpdate;
import org.motechproject.whp.functional.page.provider.ProviderPage;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.motechproject.whp.functional.page.Page.getLoginPage;

public class CloseAndOpenTreatmentTest extends TreatmentUpdateTest {

    @Test
    public void shouldUpdateTreatmentCategoryForPatientOnCloseOfCurrentTreatmentAndOpenOfNewTreatment() {
        ProviderPage providerPage = getLoginPage(webDriver).loginAsProvider(testProvider.getProviderId(), testProvider.getPassword());
        assertTrue(providerPage.hasPatient(testPatient.getFirstName()));
        assertEquals("RNTCP Category 1", providerPage.getTreatmentCategoryText(testPatient.getCaseId()));

        String closeTreatmentRequest = CaseUpdate.CloseTreatmentRequest(testPatient.getCaseId(), "09/05/2012", testPatient.getTbId());
        caseDataService.updateCase(closeTreatmentRequest);

        providerPage.logout();
        providerPage = getLoginPage(webDriver).loginAsProvider(testProvider.getProviderId(), testProvider.getPassword());

        assertFalse(providerPage.hasPatient(testPatient.getFirstName()));

        String openNewTreatmentRequest = CaseUpdate.OpenNewTreatmentRequest(testPatient.getCaseId(), testPatient.getProviderId());
        caseDataService.updateCase(openNewTreatmentRequest);

        providerPage.logout();
        providerPage = getLoginPage(webDriver).loginAsProvider(testProvider.getProviderId(), testProvider.getPassword());

        assertTrue(providerPage.hasPatient(testPatient.getFirstName()));
        assertEquals("Commercial/Private Category 2", providerPage.getTreatmentCategoryText(testPatient.getCaseId()));
    }
}
