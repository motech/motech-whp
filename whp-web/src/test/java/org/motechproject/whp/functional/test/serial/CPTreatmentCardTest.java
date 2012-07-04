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

        closeTreatmentStep
                .withPatient(testPatient)
                .withCloseDate("28/07/2012")
                .execute();

        TestProvider newProvider = providerDataService.createProvider();
        testPatient.transferIn("newTbId", newProvider.getProviderId());

        transferInTreatmentStep
                .withProvider(newProvider)
                .withPatient(testPatient)
                .withTransferDate("29/07/2012");

        adjustDateTime(5, 8, 2012);

        submitAdherenceStep
                .withProvider(newProvider)
                .withPatient(testPatient)
                .withDosesTaken(3)
                .execute();

        adjustDateTime(12, 8, 2012);

        submitAdherenceStep
                .withProvider(newProvider)
                .withPatient(testPatient)
                .withDosesTaken(3)
                .execute();

        adjustPhaseDatesStep
                .withPatient(testPatient)
                .withIpStartDate(null)
                .withEipStartDate(null)
                .withCpStartDate("03/08/2012");
        adjustPhaseDatesStep
                .execute()
                .logout();

        TreatmentCardPage treatmentCardPage = openTreatmentCardStep.withPatient(testPatient).execute();
        treatmentCardPage.logout();
    }

}