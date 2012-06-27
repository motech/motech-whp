package org.motechproject.whp.functional.test.serial;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.common.WHPConstants;
import org.motechproject.whp.functional.data.TestProvider;
import org.motechproject.whp.functional.page.ProviderPage;
import org.motechproject.whp.functional.test.treatmentupdate.TreatmentUpdateTest;
import org.motechproject.whp.refdata.domain.DiseaseClass;
import org.motechproject.whp.webservice.builder.PatientWebRequestBuilder;
import org.motechproject.whp.webservice.request.PatientWebRequest;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TransferInPatientTest extends TreatmentUpdateTest {

    @Test
    public void shouldTransferInPatientOnTransferInTreatmentUpdateRequest() {
        TestProvider provider1 = providerDataService.createProvider();
        TestProvider provider2 = providerDataService.createProvider();

        testPatient = patientDataService.createPatient(provider1.getProviderId(), "Foo");

        adjustDateTime(DateUtil.newDateTime(2012, 5, 8, 0, 0, 0));

        ProviderPage providerPage = loginAsProvider(provider1);

        assertTrue(providerPage.hasPatient(testPatient.getFirstName()));
        assertTrue(providerPage.hasTbId(testPatient.getTbId()));

        //record some adherence so that we can verify the same start date on transfer in - to distinguish between transfer in and provider change
        providerPage.clickEditAdherenceLink(testPatient.getCaseId()).setNumberOfDosesTaken(2).submit();

        PatientWebRequest closeTreatmentUpdateRequest = new PatientWebRequestBuilder()
                .withDefaultsForCloseTreatment()
                .withTreatmentOutcome("TransferredOut")
                .withTbId(testPatient.getTbId())
                .withCaseId(testPatient.getCaseId())
                .withLastModifiedDate("17/03/1990 04:55:50")
                .build();
        patientWebService.updateCase(closeTreatmentUpdateRequest);
        providerPage.logout();
        providerPage = loginAsProvider(provider1);

        assertFalse(providerPage.hasPatient(testPatient.getFirstName()));
        assertFalse(providerPage.hasTbId(testPatient.getTbId()));

        PatientWebRequest transferInpatient = new PatientWebRequestBuilder()
                .withDefaultsForTransferIn()
                .withProviderId(provider2.getProviderId())
                .withTreatmentCategory("01")
                .withDiseaseClass(DiseaseClass.valueOf(testPatient.getDiseaseClass()))
                .withCaseId(testPatient.getCaseId())
                .build();
        patientWebService.updateCase(transferInpatient);

        providerPage.logout();
        providerPage = loginAsProvider(provider1);

        assertFalse(providerPage.hasPatient(testPatient.getFirstName()));
        assertFalse(providerPage.hasTbId(testPatient.getTbId()));
        assertFalse(providerPage.hasTbId(transferInpatient.getTb_id()));

        providerPage.logout();
        providerPage = loginAsProvider(provider2);

        assertTrue(providerPage.hasPatient(testPatient.getFirstName()));
        assertTrue(providerPage.hasTbId(transferInpatient.getTb_id()));
        assertEquals("RNTCP Category 1", providerPage.getTreatmentCategoryText(testPatient.getCaseId()));
        assertEquals(new LocalDate(2012, 5, 2).toString(WHPConstants.DATE_FORMAT), providerPage.getTreatmentStartDateText(testPatient.getCaseId()));
    }
}
