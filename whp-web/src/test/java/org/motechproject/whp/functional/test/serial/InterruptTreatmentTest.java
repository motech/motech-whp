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
        ProviderPage providerPage = loginAsProvider(testProvider);
        assertTrue(providerPage.hasPatient(testPatient.getFirstName()));
        assertEquals("RNTCP Category 1", providerPage.getTreatmentCategoryText(testPatient.getCaseId()));

        PatientRequest pauseTreatmentRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForPauseTreatment()
                .withTbId(testPatient.getTbId())
                .withCaseId(testPatient.getCaseId())
                .withDateModified(DateUtil.newDateTime(2012, 5, 7, 0, 0, 0))
                .build();
        factory.updateFor(UpdateScope.pauseTreatment).apply(pauseTreatmentRequest);

        adjustDateTime(DateUtil.newDateTime(2012, 5, 8, 0, 0, 0));

        providerPage.logout();
        providerPage = loginAsProvider(testProvider);

        assertTrue(providerPage.isPatientTreatmentPaused(testPatient.getCaseId()));

        PatientRequest resumeTreatmentRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForRestartTreatment()
                .withTbId(testPatient.getTbId())
                .withCaseId(testPatient.getCaseId())
                .withDateModified(DateUtil.newDateTime(2012, 5, 9, 0, 0, 0))
                .build();

        factory.updateFor(UpdateScope.restartTreatment).apply(resumeTreatmentRequest);

        adjustDateTime(DateUtil.newDateTime(2012, 5, 15, 0, 0, 0));

        providerPage.logout();
        providerPage = loginAsProvider(testProvider);

        assertFalse(providerPage.isPatientTreatmentPaused(testPatient.getCaseId()));

        UpdateAdherencePage updateAdherencePage = providerPage.clickEditAdherenceLink(testPatient.getCaseId());
        assertEquals("This patient has been restarted on medicines on 09-05-2012 after being paused on 07-05-2012. Reasons for pause: paws",
                updateAdherencePage.getAdherenceWarningText());
    }
}
