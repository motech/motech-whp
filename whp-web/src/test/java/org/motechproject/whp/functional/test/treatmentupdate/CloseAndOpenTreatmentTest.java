package org.motechproject.whp.functional.test.treatmentupdate;

import org.junit.Test;
import org.motechproject.whp.functional.data.CaseUpdate;
import org.motechproject.whp.functional.page.ProviderPage;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CloseAndOpenTreatmentTest extends TreatmentUpdateTest {

    @Test
    public void shouldUpdateTreatmentCategoryForPatientOnCloseOfCurrentTreatmentAndOpenOfNewTreatment() {
        ProviderPage providerPage = loginAsProvider(testProvider);
        assertTrue(providerPage.hasPatient(testPatient.getFirstName()));
        assertEquals("RNTCP Category 1", providerPage.getTreatmentCategoryText(testPatient.getCaseId()));

        String closeTreatmentRequest = CaseUpdate.CloseTreatmentRequest(testPatient.getCaseId(), testPatient.getTbId());
        caseDataService.updateCase(closeTreatmentRequest);

        providerPage.logout();
        providerPage = loginAsProvider(testProvider);

        assertFalse(providerPage.hasPatient(testPatient.getFirstName()));

        String openNewTreatmentRequest = CaseUpdate.OpenNewTreatmentRequest(testPatient.getCaseId(), testPatient.getProviderId());
        caseDataService.updateCase(openNewTreatmentRequest);

        providerPage.logout();
        providerPage = loginAsProvider(testProvider);

        assertTrue(providerPage.hasPatient(testPatient.getFirstName()));
        assertEquals("Commercial/Private Category 2", providerPage.getTreatmentCategoryText(testPatient.getCaseId()));
    }
}
