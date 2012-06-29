package org.motechproject.whp.patient.domain;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.whp.refdata.domain.TherapyStatus;
import org.motechproject.whp.refdata.domain.TreatmentCategory;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.motechproject.util.DateUtil.now;
import static org.motechproject.util.DateUtil.today;

public class TherapyTest {

    @Test
    public void shouldCloseTherapy() {
        Therapy therapy = new Therapy();
        therapy.close(now());
        assertEquals(today(), therapy.getCloseDate());
        assertEquals(TherapyStatus.Closed, therapy.getStatus());
    }

    @Test
    public void shouldStartTherapy() {
        Therapy therapy = new Therapy();
        LocalDate today = today();

        therapy.start(today);

        assertEquals(today, therapy.getStartDate());
        assertEquals(today, therapy.getPhases().getByPhaseName(PhaseName.IP).getStartDate());
        assertEquals(TherapyStatus.Ongoing, therapy.getStatus());
    }

    @Test
    public void shouldFetchPhaseByName() {
        Therapy therapy = new Therapy();
        assertEquals(new Phase(PhaseName.IP), therapy.getPhase(PhaseName.IP));
    }

    @Test
    public void patientIsNearingTransitionIfRemainingDosesInCurrentPhaseIsLessThanEqualToDosesPerWeek() {
        Therapy therapy = new Therapy();
        TreatmentCategory category = new TreatmentCategory();
        category.setDosesPerWeek(3);
        category.setNumberOfDosesInIP(24);
        therapy.setTreatmentCategory(category);
        LocalDate today = today();

        therapy.start(today);

        //started on IP - 24 doses.
        therapy.getCurrentPhase().setNumberOfDosesTaken(21);

        assertTrue(therapy.isNearingPhaseTransition());
    }

    @Test
    public void shouldReturnTotalNumberOfDosesIncludingCurrentPhaseDosesForPatient() {
        Therapy therapy = new Therapy();
        TreatmentCategory category = new TreatmentCategory();
        category.setNumberOfDosesInIP(24);
        category.setNumberOfDosesInEIP(18);
        category.setNumberOfDosesInCP(28);
        therapy.setTreatmentCategory(category);
        LocalDate today = today();

        therapy.start(today);
        therapy.getCurrentPhase().setEndDate(today.plusDays(2));
        therapy.getPhases().getByPhaseName(PhaseName.CP).setStartDate(today.plusDays(3));
        therapy.getCurrentPhase().setEndDate(today.plusDays(4));

        assertEquals(Integer.valueOf(52), therapy.cumulativeNumberOfDosesSoFar());
    }

    @Test
    public void currentPhaseIsDoseCompleteIfDoseTakenCountForThatPhaseIsEqualToOrMoreThanSeededNumberOfDosesInThatPhase() {
        Therapy therapy = new Therapy();
        TreatmentCategory category = new TreatmentCategory();
        category.setNumberOfDosesInIP(24);
        therapy.setTreatmentCategory(category);
        LocalDate today = today();

        therapy.start(today);
        therapy.getCurrentPhase().setNumberOfDosesTaken(27);

        assertTrue(therapy.currentPhaseDoseComplete());
    }

    @Test
    public void shouldReturnRemainingDosesInPhase() {
        Therapy therapy = new Therapy();
        TreatmentCategory category = new TreatmentCategory();
        category.setNumberOfDosesInIP(24);
        therapy.setTreatmentCategory(category);
        LocalDate today = today();

        therapy.start(today);
        therapy.getCurrentPhase().setNumberOfDosesTaken(11);

        assertEquals(13, therapy.remainingDoses(therapy.getCurrentPhase()));
    }
}
