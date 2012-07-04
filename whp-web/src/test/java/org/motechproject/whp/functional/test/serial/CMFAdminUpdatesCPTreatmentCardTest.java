package org.motechproject.whp.functional.test.serial;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.functional.page.admin.TreatmentCardPage;
import org.motechproject.whp.functional.steps.provideradherence.SubmitAdherenceStep;
import org.motechproject.whp.functional.steps.treatmentcard.AdjustPhaseDatesStep;
import org.motechproject.whp.functional.steps.treatmentcard.OpenTreatmentCardStep;
import org.motechproject.whp.functional.steps.treatmentupdate.CloseTreatmentStep;
import org.motechproject.whp.functional.steps.treatmentupdate.OpenNewTreatmentStep;
import org.motechproject.whp.functional.steps.treatmentupdate.TransferInTreatmentStep;
import org.motechproject.whp.functional.test.treatmentupdate.TreatmentUpdateTest;

public class CMFAdminUpdatesCPTreatmentCardTest extends TreatmentUpdateTest {

    SubmitAdherenceStep submitAdherenceStep;
    CloseTreatmentStep closeTreatmentStep;
    TransferInTreatmentStep transferInTreatmentStep;
    OpenTreatmentCardStep openTreatmentCardStep;
    AdjustPhaseDatesStep adjustPhaseDatesStep;
    OpenNewTreatmentStep openNewTreatmentStep;

    @Before
    public void setup() {
        submitAdherenceStep = new SubmitAdherenceStep(webDriver);
        closeTreatmentStep = new CloseTreatmentStep(webDriver);
        transferInTreatmentStep = new TransferInTreatmentStep(webDriver);
        openTreatmentCardStep = new OpenTreatmentCardStep(webDriver);
        adjustPhaseDatesStep = new AdjustPhaseDatesStep(webDriver);
        openNewTreatmentStep = new OpenNewTreatmentStep(webDriver);
    }

    @Test
    public void shouldBuildCPTreatmentCardForPatient() {
        adjustDateTime(8, 7, 2012);

        submitAdherenceStep
                .withProvider(testProvider)
                .withPatient(testPatient)
                .withDosesTaken(3)
                .execute();

        adjustDateTime(15, 7, 2012);

        submitAdherenceStep
                .withProvider(testProvider)
                .withPatient(testPatient)
                .withDosesTaken(1)
                .execute();

        adjustDateTime(29, 7, 2012);

        submitAdherenceStep
                .withProvider(testProvider)
                .withPatient(testPatient)
                .withDosesTaken(2)
                .execute();

        adjustPhaseDatesStep
                .withPatient(testPatient)
                .withIpStartDate(null)
                .withEipStartDate(null)
                .withCpStartDate("07/08/2012");

        adjustPhaseDatesStep
                .execute()
                .logout();

        TreatmentCardPage treatmentCardPage = openTreatmentCardStep.withPatient(testPatient).execute();

        treatmentCardPage.logout();
    }
}