package org.motechproject.whp.functional.test.serial;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.model.DayOfWeek;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.functional.data.TestProvider;
import org.motechproject.whp.functional.page.ProviderPage;
import org.motechproject.whp.functional.test.treatmentupdate.TreatmentUpdateTest;
import org.motechproject.whp.patient.builder.PatientRequestBuilder;
import org.motechproject.whp.patient.domain.TreatmentCategory;
import org.motechproject.whp.patient.repository.AllTreatmentCategories;
import org.motechproject.whp.refdata.domain.WHPConstants;
import org.motechproject.whp.webservice.builder.PatientWebRequestBuilder;
import org.motechproject.whp.webservice.request.PatientWebRequest;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TransferInPatientTest extends TreatmentUpdateTest {

    @Autowired
    AllTreatmentCategories allTreatmentCategories;

    private TreatmentCategory treatmentCategory;

    @Before
    public void setUp(){
        super.setUp();
        treatmentCategory = new TreatmentCategory("RNTCP Category 1", "01", 3, 8, 18, 24, 54, Arrays.asList(DayOfWeek.Monday, DayOfWeek.Wednesday, DayOfWeek.Friday));
        allTreatmentCategories.add(treatmentCategory);
    }

    @Test
    public void shouldTransferInPatientOnTransferInTreatmentUpdateRequest() {
        TestProvider provider1 = providerDataService.createProvider();
        TestProvider provider2 = providerDataService.createProvider();

        adjustDateTime(DateUtil.newDateTime(2012, 5, 8, 0, 0, 0));

        patientRequest = new PatientRequestBuilder()
                .withDefaults()
                .withLastModifiedDate(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                .withTreatmentCategory(treatmentCategory)
                .withCaseId(UUID.randomUUID().toString())
                .withProviderId(provider1.getProviderId())
                .build();
        patientService.createPatient(patientRequest);

        ProviderPage providerPage = loginAsProvider(provider1);

        assertTrue(providerPage.hasPatient(patientRequest.getFirst_name()));
        assertTrue(providerPage.hasTbId(patientRequest.getTb_id()));

        //record some adherence so that we can verify the same start date on transfer in - to distinguish between transfer in and provider change
        providerPage.clickEditAdherenceLink(patientRequest.getCase_id()).setNumberOfDosesTaken(2).submit();

        PatientWebRequest closeTreatmentUpdateRequest = new PatientWebRequestBuilder()
                .withDefaultsForCloseTreatment()
                .withTreatmentOutcome("TransferredOut")
                .withTbId(patientRequest.getTb_id())
                .withCaseId(patientRequest.getCase_id())
                .withLastModifiedDate("17/03/1990 04:55:50")
                .build();
        patientWebService.updateCase(closeTreatmentUpdateRequest);
        providerPage.logout();
        providerPage = loginAsProvider(provider1);

        assertFalse(providerPage.hasPatient(patientRequest.getFirst_name()));
        assertFalse(providerPage.hasTbId(patientRequest.getTb_id()));

        PatientWebRequest transferInPatientRequest = new PatientWebRequestBuilder()
                .withDefaultsForTransferIn()
                .withProviderId(provider2.getProviderId())
                .withTreatmentCategory("01")
                .withDiseaseClass(patientRequest.getDisease_class())
                .withCaseId(patientRequest.getCase_id())
                .build();
        patientWebService.updateCase(transferInPatientRequest);

        providerPage.logout();
        providerPage = loginAsProvider(provider1);

        assertFalse(providerPage.hasPatient(patientRequest.getFirst_name()));
        assertFalse(providerPage.hasTbId(patientRequest.getTb_id()));
        assertFalse(providerPage.hasTbId(transferInPatientRequest.getTb_id()));

        providerPage.logout();
        providerPage = loginAsProvider(provider2);

        assertTrue(providerPage.hasPatient(patientRequest.getFirst_name()));
        assertTrue(providerPage.hasTbId(transferInPatientRequest.getTb_id()));
        assertEquals(patientRequest.getTreatment_category().getName(), providerPage.getTreatmentCategoryText(patientRequest.getCase_id()));
        assertEquals(new LocalDate(2012, 5, 2).toString(WHPConstants.DATE_FORMAT), providerPage.getTreatmentStartDateText(patientRequest.getCase_id()));
    }

    @After
    public void tearDown() throws IOException {
        super.tearDown();
        allTreatmentCategories.remove(treatmentCategory);
    }
}
