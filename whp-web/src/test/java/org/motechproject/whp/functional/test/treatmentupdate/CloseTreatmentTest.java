package org.motechproject.whp.functional.test.treatmentupdate;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.model.DayOfWeek;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.builder.PatientWebRequestBuilder;
import org.motechproject.whp.functional.data.TestProvider;
import org.motechproject.whp.functional.framework.BaseTest;
import org.motechproject.whp.functional.framework.MyPageFactory;
import org.motechproject.whp.functional.page.LoginPage;
import org.motechproject.whp.functional.page.ProviderPage;
import org.motechproject.whp.functional.service.ProviderDataService;
import org.motechproject.whp.patient.builder.PatientRequestBuilder;
import org.motechproject.whp.patient.builder.TreatmentUpdateRequestBuilder;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.contract.TreatmentUpdateRequest;
import org.motechproject.whp.patient.domain.TreatmentCategory;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.request.PatientWebRequest;
import org.motechproject.whp.webservice.PatientWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CloseTreatmentTest extends TreatmentUpdateTest {

    @Test
    public void shouldUpdateTreatmentCategoryForPatientOnCloseOfCurrentTreatmentAndOpenOfNewTreatment() {
        ProviderPage providerPage = loginAsProvider(provider);
        assertTrue(providerPage.hasPatient(patientRequest.getFirst_name()));
        assertEquals(patientRequest.getTreatment_category().getName(), providerPage.getTreatmentCategoryText(patientRequest.getCase_id()));

        TreatmentUpdateRequest closeTreatmentUpdateRequest = TreatmentUpdateRequestBuilder.startRecording()
                                                                                          .withMandatoryFieldsForCloseTreatment()
                                                                                          .withTbId(patientRequest.getTb_id())
                                                                                          .withCaseId(patientRequest.getCase_id())
                                                                                          .withDateModified(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                                                                                          .build();
        patientService.performTreatmentUpdate(closeTreatmentUpdateRequest);
        providerPage.logout();
        providerPage = loginAsProvider(provider);

        assertFalse(providerPage.hasPatient(patientRequest.getFirst_name()));

        TreatmentCategory newCategory = new TreatmentCategory("Do Not Copy", "10", 3, 8, 18, Arrays.asList(DayOfWeek.Monday, DayOfWeek.Wednesday, DayOfWeek.Friday));

        TreatmentUpdateRequest openNewTreatmentUpdateRequest = TreatmentUpdateRequestBuilder.startRecording()
                                                                                            .withMandatoryFieldsForOpenNewTreatment()
                                                                                            .withCaseId(patientRequest.getCase_id())
                                                                                            .withTreatmentCategory(newCategory)
                                                                                            .withProviderId(patientRequest.getProvider_id())
                                                                                            .withDateModified(DateUtil.newDateTime(2012, 3, 17, 4, 55, 50))
                                                                                            .withTbId("elevenDigit")
                                                                                            .build();
        patientService.performTreatmentUpdate(openNewTreatmentUpdateRequest);

        providerPage.logout();
        providerPage = loginAsProvider(provider);

        assertTrue(providerPage.hasPatient(patientRequest.getFirst_name()));
        assertEquals(openNewTreatmentUpdateRequest.getTreatment_category().getName(), providerPage.getTreatmentCategoryText(patientRequest.getCase_id()));
    }
}
