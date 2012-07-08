package org.motechproject.whp.patient.domain;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.whp.refdata.domain.TherapyStatus;
import org.motechproject.whp.refdata.domain.TreatmentCategory;

import static junit.framework.Assert.*;
import static org.motechproject.util.DateUtil.now;
import static org.motechproject.util.DateUtil.today;
import static org.motechproject.whp.refdata.domain.PhaseName.*;

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
        assertEquals(today, therapy.getPhases().getByPhaseName(IP).getStartDate());
        assertEquals(TherapyStatus.Ongoing, therapy.getStatus());
    }

    @Test
    public void shouldFetchPhaseByName() {
        Therapy therapy = new Therapy();
        assertEquals(new PhaseRecord(IP), therapy.getPhase(IP));
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
    public void shouldReturnNumberOfWeeksElapsed_WhenTreatmentHasBeenStarted() {
        Therapy therapy = new Therapy();
        TreatmentCategory category = new TreatmentCategory();
        category.setNumberOfDosesInIP(24);
        therapy.setTreatmentCategory(category);

        therapy.start(today().minusDays(7));
        assertEquals(Integer.valueOf(1), therapy.getWeeksElapsed());
    }

    @Test
    public void shouldReturnNumberOfWeeksElapsed_WhenTreatmentHasNotBeenStarted() {
        Therapy therapy = new Therapy();
        TreatmentCategory category = new TreatmentCategory();
        category.setNumberOfDosesInIP(24);
        therapy.setTreatmentCategory(category);

        assertNull(therapy.getWeeksElapsed());
    }

    @Test
    public void shouldHaveIPPhase() {
        Therapy therapy = new Therapy();
        assertNotNull(therapy.getPhase(IP));
    }

    @Test
    public void shouldHaveEIPPhase() {
        Therapy therapy = new Therapy();
        assertNotNull(therapy.getPhase(EIP));
    }

    @Test
    public void shouldHaveCPPhase() {
        Therapy therapy = new Therapy();
        assertNotNull(therapy.getPhase(CP));
    }

    @Test
    public void shouldNotBeOnIPPhaseByDefault() {
        Therapy therapy = new Therapy();
        assertFalse(therapy.isOrHasBeenOnIP());
    }

    @Test
    public void shouldNotBeOnCPPhaseByDefault() {
        Therapy therapy = new Therapy();
        assertFalse(therapy.isOrHasBeenOnCP());
    }

}
