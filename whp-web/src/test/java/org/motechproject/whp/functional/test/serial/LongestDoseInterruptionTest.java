package org.motechproject.whp.functional.test.serial;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.functional.page.admin.TreatmentCardPage;
import org.motechproject.whp.functional.steps.provideradherence.SubmitAdherenceStep;
import org.motechproject.whp.functional.steps.treatmentcard.AdjustPhaseDatesStep;
import org.motechproject.whp.functional.steps.treatmentcard.OpenTreatmentCardStep;
import org.motechproject.whp.functional.test.treatmentupdate.TreatmentUpdateTest;

import static junit.framework.Assert.assertEquals;
import static org.motechproject.model.DayOfWeek.Wednesday;
import static org.motechproject.whp.functional.data.AdherenceValue.Value.Tick;
import static org.motechproject.whp.functional.steps.treatmentcard.EnterAdherenceOnTreatmentCardStep.onTreatmentCardPage;

public class LongestDoseInterruptionTest extends TreatmentUpdateTest {

    private SubmitAdherenceStep submitAdherenceStep;
    private OpenTreatmentCardStep openTreatmentCardStep;
    private AdjustPhaseDatesStep adjustPhaseDatesStep;
    private LocalDate today = new LocalDate(2012, 8, 12);

    @Before
    public void setup() {
        submitAdherenceStep = new SubmitAdherenceStep(webDriver);
        openTreatmentCardStep = new OpenTreatmentCardStep(webDriver);
        adjustPhaseDatesStep = new AdjustPhaseDatesStep(webDriver);
    }

    @Test
    public void shouldBuildCPTreatmentCardForPatient() {
        provideAdherence();

        assertLongestTreatmentInterruption("4.7");

        TreatmentCardPage treatmentCardPage = openTreatmentCardStep.withPatient(testPatient).execute();

        treatmentCardPage = onTreatmentCardPage(treatmentCardPage, webDriver)
                .inSection("IPTreatmentCard")
                .enter(Tick)
                .asAdherenceValueOn(Wednesday)
                .ofWeekEndingOn(2012, 7, 29)
                .execute();

        treatmentCardPage.logout();

        assertLongestTreatmentInterruption("3.3");
    }

    private void assertLongestTreatmentInterruption(String duration) {
        TreatmentCardPage treatmentCardPage = openTreatmentCardStep.withPatient(testPatient).execute();
        assertEquals(duration, treatmentCardPage.getLongestTreatmentInterruption());
        treatmentCardPage.logout();
    }

    private void provideAdherence() {
        adjustDateTime(3, 6, 2012);
        submitAdherenceStep
                .withProvider(testProvider)
                .withPatient(testPatient)
                .withDosesTaken(2)
                .execute();

        adjustDateTime(17, 6, 2012);

        submitAdherenceStep
                .withProvider(testProvider)
                .withPatient(testPatient)
                .withDosesTaken(3)
                .execute();

        adjustDateTime(24, 7, 2012);

        submitAdherenceStep
                .withProvider(testProvider)
                .withPatient(testPatient)
                .withDosesTaken(0)
                .execute();

        adjustDateTime(1, 7, 2012);

        submitAdherenceStep
                .withProvider(testProvider)
                .withPatient(testPatient)
                .withDosesTaken(2)
                .execute();

        adjustDateTime(5, 8, 2012);

        submitAdherenceStep
                .withProvider(testProvider)
                .withPatient(testPatient)
                .withDosesTaken(1)
                .execute();
    }

}