package org.motechproject.whp.functional.test.serial;

import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.functional.page.ProviderPage;
import org.motechproject.whp.functional.page.UpdateAdherencePage;
import org.motechproject.whp.functional.test.treatmentupdate.TreatmentUpdateTest;
import org.motechproject.whp.patient.builder.PatientRequestBuilder;
import org.motechproject.whp.patient.command.UpdateScope;
import org.motechproject.whp.patient.contract.PatientRequest;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InterruptTreatmentTest extends TreatmentUpdateTest {

    @Test
    public void shouldDemarcateDaysForReportingAdherenceWhenTreatmentIsPaused() {
        ProviderPage providerPage = loginAsProvider(provider);
        assertTrue(providerPage.hasPatient(patientRequest.getFirst_name()));
        assertEquals(patientRequest.getTreatment_category().getName(), providerPage.getTreatmentCategoryText(patientRequest.getCase_id()));

        PatientRequest pauseTreatmentRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForPauseTreatment()
                .withTbId(patientRequest.getTb_id())
                .withCaseId(patientRequest.getCase_id())
                .withDateModified(DateUtil.newDateTime(2012, 5, 7, 0, 0, 0))
                .build();
        factory.updateFor(UpdateScope.pauseTreatment).apply(pauseTreatmentRequest);

        adjustDateTime(DateUtil.newDateTime(2012, 5, 8, 0, 0, 0));

        providerPage.logout();
        providerPage = loginAsProvider(provider);

        assertTrue(providerPage.isPatientTreatmentPaused(patientRequest.getCase_id()));

        PatientRequest resumeTreatmentRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForRestartTreatment()
                .withTbId(patientRequest.getTb_id())
                .withCaseId(patientRequest.getCase_id())
                .withDateModified(DateUtil.newDateTime(2012, 5, 9, 0, 0, 0))
                .build();

        factory.updateFor(UpdateScope.restartTreatment).apply(resumeTreatmentRequest);

        adjustDateTime(DateUtil.newDateTime(2012, 5, 15, 0, 0, 0));

        providerPage.logout();
        providerPage = loginAsProvider(provider);

        assertFalse(providerPage.isPatientTreatmentPaused(patientRequest.getCase_id()));

        UpdateAdherencePage updateAdherencePage = providerPage.clickEditAdherenceLink(patientRequest.getCase_id());
        assertEquals("This patient has been restarted on medicines on 09-05-2012 after being paused on 07-05-2012. Reasons for pause: paws",
                updateAdherencePage.getAdherenceWarningText());
    }
}
