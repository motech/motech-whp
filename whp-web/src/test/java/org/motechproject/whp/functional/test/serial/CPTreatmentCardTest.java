package org.motechproject.whp.functional.test.serial;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.functional.data.TestProvider;
import org.motechproject.whp.functional.page.admin.TreatmentCardPage;
import org.motechproject.whp.functional.steps.provideradherence.SubmitAdherenceStep;
import org.motechproject.whp.functional.steps.treatmentcard.AdjustPhaseDatesStep;
import org.motechproject.whp.functional.steps.treatmentcard.OpenTreatmentCardStep;
import org.motechproject.whp.functional.steps.treatmentupdate.CloseTreatmentStep;
import org.motechproject.whp.functional.steps.treatmentupdate.TransferInTreatmentStep;
import org.motechproject.whp.functional.test.treatmentupdate.TreatmentUpdateTest;

public class CPTreatmentCardTest extends TreatmentUpdateTest {

    SubmitAdherenceStep submitAdherenceStep;
    CloseTreatmentStep closeTreatmentStep;
    TransferInTreatmentStep transferInTreatmentStep;
    OpenTreatmentCardStep openTreatmentCardStep;
    AdjustPhaseDatesStep adjustPhaseDatesStep;

    @Before
    public void setup() {
        submitAdherenceStep = new SubmitAdherenceStep(webDriver);
        closeTreatmentStep = new CloseTreatmentStep(webDriver);
        transferInTreatmentStep = new TransferInTreatmentStep(webDriver);
        openTreatmentCardStep = new OpenTreatmentCardStep(webDriver);
        adjustPhaseDatesStep = new AdjustPhaseDatesStep(webDriver);
    }

    @Test
    public void shouldBuildCPTreatmentCardForPatient() {
        adjustDateTime(8, 7, 2012);
        submitAdherenceStep.execute(testProvider, testPatient, 3);
        adjustDateTime(15, 7, 2012);
        submitAdherenceStep.execute(testProvider, testPatient, 1);
        adjustDateTime(29, 7, 2012);
        submitAdherenceStep.execute(testProvider, testPatient, 2);
        closeTreatmentStep.execute(testPatient, "28/07/2012");

        TestProvider newProvider = providerDataService.createProvider();
        testPatient.transferIn("newTbId", newProvider.getProviderId());
        transferInTreatmentStep.execute(newProvider, testPatient, "29/07/2012");
        adjustDateTime(5, 8, 2012);
        submitAdherenceStep.execute(newProvider, testPatient, 3);
        adjustDateTime(12, 8, 2012);
        submitAdherenceStep.execute(newProvider, testPatient, 3);

        adjustPhaseDatesStep.execute(testPatient, null, null, "03/08/2012");
        adjustPhaseDatesStep.patientDashboardPage.logout();

        openTreatmentCardStep.execute(testPatient);
        TreatmentCardPage treatmentCardPage = openTreatmentCardStep.treatmentCardPage;

        treatmentCardPage.logout();
    }

}