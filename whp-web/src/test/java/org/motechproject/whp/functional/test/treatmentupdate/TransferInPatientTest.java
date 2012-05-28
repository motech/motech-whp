package org.motechproject.whp.functional.test.treatmentupdate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.model.DayOfWeek;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.builder.PatientWebRequestBuilder;
import org.motechproject.whp.functional.data.TestProvider;
import org.motechproject.whp.functional.page.ProviderPage;
import org.motechproject.whp.patient.builder.PatientRequestBuilder;
import org.motechproject.whp.patient.command.UpdateScope;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.TreatmentCategory;
import org.motechproject.whp.patient.repository.AllTreatmentCategories;
import org.motechproject.whp.request.PatientWebRequest;
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
        treatmentCategory = new TreatmentCategory("RNTCP Category 1", "01", 3, 8, 18, Arrays.asList(DayOfWeek.Monday, DayOfWeek.Wednesday, DayOfWeek.Friday));
        allTreatmentCategories.add(treatmentCategory);
    }

    @Test
    public void shouldTransferInPatientOnTransferInTreatmentUpdateRequest() {
        TestProvider provider1 = providerDataService.createProvider();
        TestProvider provider2 = providerDataService.createProvider();

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

        PatientWebRequest closeTreatmentUpdateRequest = new PatientWebRequestBuilder()
                .withDefaultsForCloseTreatment()
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
    }

    @After
    public void tearDown() throws IOException {
        super.tearDown();
        allTreatmentCategories.remove(treatmentCategory);
    }
}
