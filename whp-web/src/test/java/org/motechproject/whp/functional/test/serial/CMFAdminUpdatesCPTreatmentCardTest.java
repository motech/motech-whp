package org.motechproject.whp.functional.test.serial;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.functional.assertions.treatmentcard.Adherence;
import org.motechproject.whp.functional.page.admin.TreatmentCardPage;
import org.motechproject.whp.functional.steps.provideradherence.SubmitAdherenceStep;
import org.motechproject.whp.functional.steps.treatmentcard.AdjustPhaseDatesStep;
import org.motechproject.whp.functional.steps.treatmentcard.OpenTreatmentCardStep;
import org.motechproject.whp.functional.steps.treatmentupdate.CloseTreatmentStep;
import org.motechproject.whp.functional.steps.treatmentupdate.OpenNewTreatmentStep;
import org.motechproject.whp.functional.steps.treatmentupdate.TransferInTreatmentStep;
import org.motechproject.whp.functional.test.treatmentupdate.TreatmentUpdateTest;

import static org.motechproject.model.DayOfWeek.Monday;
import static org.motechproject.util.DateUtil.newDateTime;
import static org.motechproject.whp.functional.data.AdherenceValue.Value.Tick;
import static org.motechproject.whp.functional.steps.treatmentcard.EnterAdherenceOnTreatmentCardStep.onTreatmentCardPage;

public class CMFAdminUpdatesCPTreatmentCardTest extends TreatmentUpdateTest {

    private SubmitAdherenceStep submitAdherenceStep;
    private CloseTreatmentStep closeTreatmentStep;
    private TransferInTreatmentStep transferInTreatmentStep;
    private OpenTreatmentCardStep openTreatmentCardStep;
    private AdjustPhaseDatesStep adjustPhaseDatesStep;
    private OpenNewTreatmentStep openNewTreatmentStep;
    private LocalDate today = new LocalDate(2012, 8, 12);

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
        provideAdherence();

        adjustPhaseDatesSoThatPatientIsOnCP();

        TreatmentCardPage treatmentCardPage = openTreatmentCardStep.withPatient(testPatient).execute();

        treatmentCardPage = onTreatmentCardPage(treatmentCardPage, webDriver)
                .inSection("CPTreatmentCard")
                .enter(Tick)
                .asAdherenceValueOn(Monday)
                .ofWeekEndingOn(today.getYear(), today.getMonthOfYear(), today.getDayOfMonth())
                .execute();

        Adherence.is(treatmentCardPage, new LocalDate(2012, 8, 6), "1", testProvider.getProviderId(), "CPTreatmentCard");
        treatmentCardPage.logout();
    }

    private void provideAdherence() {
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
    }

    private void adjustPhaseDatesSoThatPatientIsOnCP() {
        adjustDateTime(newDateTime(today));

        adjustPhaseDatesStep
                .withPatient(testPatient)
                .withIpStartDate(null)
                .withEipStartDate(null)
                .withCpStartDate("01/08/2012");

        adjustPhaseDatesStep
                .execute()
                .logout();
    }
}