package org.motechproject.whp.functional.test.serial;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.common.WHPConstants;
import org.motechproject.whp.functional.data.CaseUpdate;
import org.motechproject.whp.functional.data.TestProvider;
import org.motechproject.whp.functional.page.provider.ProviderPage;
import org.motechproject.whp.functional.test.treatmentupdate.TreatmentUpdateTest;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.motechproject.whp.functional.page.Page.getLoginPage;

public class TransferInPatientTest extends TreatmentUpdateTest {

    @Test
    public void shouldTransferInPatientOnTransferInTreatmentUpdateRequest() {
        TestProvider provider1 = providerDataService.createProvider();
        providerDataService.activateProvider(provider1.getProviderId());

        TestProvider provider2 = providerDataService.createProvider();
        providerDataService.activateProvider(provider2.getProviderId());

        testPatient = patientDataService.createPatient(provider1.getProviderId(), "Foo", provider1.getDistrict());

        adjustDateTime(DateUtil.newDateTime(2012, 5, 8, 0, 0, 0));

        ProviderPage providerPage = getLoginPage(webDriver).loginAsProvider(provider1.getProviderId(), provider1.getPassword());

        assertTrue(providerPage.hasPatient(testPatient.getFirstName()));
        assertTrue(providerPage.hasTbId(testPatient.getTbId()));

        //record some adherence so that we can verify the same start date on transfer in - to distinguish between transfer in and provider change
        providerPage.clickEditAdherenceLink(testPatient.getCaseId()).setNumberOfDosesTaken(2).submit();

        String closeTreatmentRequest = CaseUpdate.CloseTreatmentRequest(testPatient.getCaseId(), "09/05/2012", testPatient.getTbId());
        caseDataService.updateCase(closeTreatmentRequest);
        providerPage.logout();
        providerPage = getLoginPage(webDriver).loginAsProvider(provider1.getProviderId(), provider1.getPassword());

        assertFalse(providerPage.hasPatient(testPatient.getFirstName()));
        assertFalse(providerPage.hasTbId(testPatient.getTbId()));

        String transferInTBId = "elevenDigit";
        String transferInPatientRequest = CaseUpdate.TransferInPatientRequest(testPatient.getCaseId(), "10/05/2012", transferInTBId, "01", provider2.getProviderId());
        caseDataService.updateCase(transferInPatientRequest);

        providerPage.logout();
        providerPage = getLoginPage(webDriver).loginAsProvider(provider1.getProviderId(), provider1.getPassword());

        assertFalse(providerPage.hasPatient(testPatient.getFirstName()));
        assertFalse(providerPage.hasTbId(testPatient.getTbId()));
        assertFalse(providerPage.hasTbId(transferInTBId));

        providerPage.logout();
        providerPage = getLoginPage(webDriver).loginAsProvider(provider2.getProviderId(), provider2.getPassword());

        assertTrue(providerPage.hasPatient(testPatient.getFirstName()));
        assertTrue(providerPage.hasTbId(transferInTBId));
        assertEquals("RNTCP Category 1", providerPage.getTreatmentCategoryText(testPatient.getCaseId()));
        assertEquals(new LocalDate(2012, 5, 2).toString(WHPConstants.DATE_FORMAT), providerPage.getTreatmentStartDateText(testPatient.getCaseId()));
    }
}
