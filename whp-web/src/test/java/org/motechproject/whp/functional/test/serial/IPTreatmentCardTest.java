package org.motechproject.whp.functional.test.serial;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.functional.assertions.treatmentcard.Adherence;
import org.motechproject.whp.functional.data.TestProvider;
import org.motechproject.whp.functional.page.admin.TreatmentCardPage;
import org.motechproject.whp.functional.steps.provideradherence.SubmitAdherenceStep;
import org.motechproject.whp.functional.steps.treatmentcard.OpenTreatmentCardStep;
import org.motechproject.whp.functional.steps.treatmentupdate.CloseTreatmentStep;
import org.motechproject.whp.functional.steps.treatmentupdate.PauseTreatmentStep;
import org.motechproject.whp.functional.steps.treatmentupdate.RestartTreatmentStep;
import org.motechproject.whp.functional.steps.treatmentupdate.TransferInTreatmentStep;
import org.motechproject.whp.functional.test.treatmentupdate.TreatmentUpdateTest;

public class IPTreatmentCardTest extends TreatmentUpdateTest {

    SubmitAdherenceStep submitAdherenceStep;
    PauseTreatmentStep pauseTreatmentStep;
    RestartTreatmentStep restartTreatmentStep;
    CloseTreatmentStep closeTreatmentStep;
    TransferInTreatmentStep transferInTreatmentStep;
    OpenTreatmentCardStep openTreatmentCardStep;

    @Before
    public void setup() {
        submitAdherenceStep = new SubmitAdherenceStep(webDriver);
        pauseTreatmentStep = new PauseTreatmentStep(webDriver);
        restartTreatmentStep = new RestartTreatmentStep(webDriver);
        closeTreatmentStep = new CloseTreatmentStep(webDriver);
        transferInTreatmentStep = new TransferInTreatmentStep(webDriver);
        openTreatmentCardStep = new OpenTreatmentCardStep(webDriver);
    }

    @Test
    public void shouldBuildIPTreatmentCardForPatient() {
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

        pauseTreatmentStep
                .withPatient(testPatient)
                .withPauseDate("13/07/2012")
                .withReason("paws")
                .execute();

        restartTreatmentStep
                .withPatient(testPatient)
                .withRestartDate("19/07/2012")
                .withReason("swap")
                .execute();

        adjustDateTime(29, 7, 2012);

        submitAdherenceStep
                .withProvider(testProvider)
                .withPatient(testPatient)
                .withDosesTaken(2)
                .execute();

        pauseTreatmentStep
                .withPatient(testPatient)
                .withPauseDate("25/07/2012")
                .withReason("paws")
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
                .withTransferDate("29/07/2012")
                .execute();

        adjustDateTime(5, 8, 2012);

        submitAdherenceStep
                .withProvider(newProvider)
                .withPatient(testPatient)
                .withDosesTaken(3)
                .execute();

        adjustDateTime(7, 8, 2012);

        pauseTreatmentStep
                .withPatient(testPatient)
                .withPauseDate("07/08/2012")
                .withReason("paws")
                .execute();

        adjustDateTime(12, 8, 2012);

        submitAdherenceStep
                .withProvider(newProvider)
                .withPatient(testPatient)
                .withDosesTaken(3)
                .execute();

        TreatmentCardPage treatmentCardPage = openTreatmentCardStep
                .withPatient(testPatient)
                .execute();

        Adherence.is(treatmentCardPage, new LocalDate(2012, 7, 2), "1", testProvider.getProviderId());
        Adherence.is(treatmentCardPage, new LocalDate(2012, 7, 4), "1", testProvider.getProviderId());
        Adherence.is(treatmentCardPage, new LocalDate(2012, 7, 6), "1", testProvider.getProviderId());

        Adherence.is(treatmentCardPage, new LocalDate(2012, 7, 9), "2", testProvider.getProviderId());
        Adherence.is(treatmentCardPage, new LocalDate(2012, 7, 11), "2", testProvider.getProviderId());
        Adherence.is(treatmentCardPage, new LocalDate(2012, 7, 13), "1", testProvider.getProviderId());

        Adherence.is(treatmentCardPage, new LocalDate(2012, 7, 23), "2", testProvider.getProviderId());
        Adherence.is(treatmentCardPage, new LocalDate(2012, 7, 25), "1", testProvider.getProviderId());
        Adherence.is(treatmentCardPage, new LocalDate(2012, 7, 27), "1", testProvider.getProviderId());

        Adherence.is(treatmentCardPage, new LocalDate(2012, 7, 30), "1", newProvider.getProviderId());
        Adherence.is(treatmentCardPage, new LocalDate(2012, 8, 1), "1", newProvider.getProviderId());
        Adherence.is(treatmentCardPage, new LocalDate(2012, 8, 3), "1", newProvider.getProviderId());

        Adherence.is(treatmentCardPage, new LocalDate(2012, 8, 6), "1", newProvider.getProviderId());
        Adherence.is(treatmentCardPage, new LocalDate(2012, 8, 8), "1", newProvider.getProviderId());
        Adherence.is(treatmentCardPage, new LocalDate(2012, 8, 10), "1", newProvider.getProviderId());

        /* Asserting on blank adherence data */
        Adherence.is(treatmentCardPage, new LocalDate(2012, 7, 16), "0", testProvider.getProviderId());
        Adherence.is(treatmentCardPage, new LocalDate(2012, 7, 18), "0", testProvider.getProviderId());
        Adherence.is(treatmentCardPage, new LocalDate(2012, 7, 20), "0", testProvider.getProviderId());

        /* Asserting on paused dates */
        Adherence.isPaused(treatmentCardPage, new LocalDate(2012, 7, 13));
        Adherence.isPaused(treatmentCardPage, new LocalDate(2012, 7, 16));
        Adherence.isPaused(treatmentCardPage, new LocalDate(2012, 7, 18));
        Adherence.isPaused(treatmentCardPage, new LocalDate(2012, 7, 25));
        Adherence.isPaused(treatmentCardPage, new LocalDate(2012, 7, 27));
        Adherence.isPaused(treatmentCardPage, new LocalDate(2012, 8, 8));
        Adherence.isPaused(treatmentCardPage, new LocalDate(2012, 8, 10));

        /* Asserting on dates not paused */
        Adherence.isNotPaused(treatmentCardPage, new LocalDate(2012, 7, 30));
        Adherence.isNotPaused(treatmentCardPage, new LocalDate(2012, 8, 1));
        Adherence.isNotPaused(treatmentCardPage, new LocalDate(2012, 8, 3));
        Adherence.isNotPaused(treatmentCardPage, new LocalDate(2012, 8, 6));

        /* Asserting on dates that are not editable */
        Adherence.isNotEditable(treatmentCardPage, new LocalDate(2012, 7, 17));
        Adherence.isNotEditable(treatmentCardPage, new LocalDate(2012, 7, 19));
        Adherence.isNotEditable(treatmentCardPage, new LocalDate(2012, 7, 22));

        Adherence.isNotEditable(treatmentCardPage, new LocalDate(2012, 7, 31));
        Adherence.isNotEditable(treatmentCardPage, new LocalDate(2012, 8, 9));
        Adherence.isNotEditable(treatmentCardPage, new LocalDate(2012, 8, 21));

        /* Asserting on dates that are not even present in the table*/
        Adherence.isNotPresent(treatmentCardPage, new LocalDate(2012, 6, 30));
        Adherence.isNotPresent(treatmentCardPage, new LocalDate(2013, 1, 1));
    }

}