package org.motechproject.whp.patient.domain;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.builder.TherapyBuilder;
import org.motechproject.whp.refdata.domain.Phase;
import org.motechproject.whp.refdata.domain.SampleInstance;
import org.motechproject.whp.refdata.domain.TherapyStatus;
import org.motechproject.whp.refdata.domain.TreatmentCategory;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.*;
import static org.motechproject.util.DateUtil.*;
import static org.motechproject.whp.refdata.domain.Phase.*;
import static org.motechproject.whp.refdata.domain.SmearTestResult.Positive;

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
        assertEquals(today, therapy.getPhases().getIPStartDate());
        assertEquals(TherapyStatus.Ongoing, therapy.getStatus());
    }

    @Test
    public void shouldReturnFalseForHasStartedWhenStartDateIsNotSet() {
        Therapy therapy = new Therapy();

        assertFalse(therapy.hasStarted());
    }

    @Test
    public void shouldReturnTrueForHasStartedWhenStartDateIsSet() {
        Therapy therapy = new Therapy();
        therapy.setStartDate(new LocalDate());

        assertTrue(therapy.hasStarted());
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
        therapy.getCurrentPhase().setNumberOfDosesTaken(21, today);

        assertTrue(therapy.isNearingPhaseTransition());
    }

    @Test
    public void patientShouldNotBeInTransitioningStateWhenOnCP() {
        Patient patient = new PatientBuilder().withDefaults().build();
        patient.startTherapy(today());
        patient.endLatestPhase(today().plusMonths(1));
        patient.nextPhaseName(Phase.CP);
        patient.startNextPhase();
        patient.setNumberOfDosesTaken(patient.getCurrentPhase().getName(), 100, today());

        assertFalse(patient.getCurrentTherapy().isTransitioning());
    }

    @Test
    public void currentPhaseIsDoseCompleteIfDoseTakenCountForThatPhaseIsEqualToOrMoreThanSeededNumberOfDosesInThatPhase() {
        Therapy therapy = new Therapy();
        TreatmentCategory category = new TreatmentCategory();
        category.setNumberOfDosesInIP(24);
        therapy.setTreatmentCategory(category);
        LocalDate today = today();

        therapy.start(today);
        therapy.getCurrentPhase().setNumberOfDosesTaken(27, today);

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
    public void shouldNotBeOnIPPhaseByDefault() {
        Therapy therapy = new Therapy();
        assertFalse(therapy.isOrHasBeenOnIP());
    }

    @Test
    public void shouldNotBeOnCPPhaseByDefault() {
        Therapy therapy = new Therapy();
        assertFalse(therapy.hasBeenOnCP());
    }

    @Test
    public void shouldReturnTheNumberOfDosesTakenInTheIPPhase() {
        Therapy therapy = new Therapy();
        therapy.getPhases().setIPStartDate(today());

        therapy.setNumberOfDosesTaken(IP, 2, today());
        assertEquals(2, therapy.getNumberOfDosesTaken(IP));
    }

    @Test
    public void shouldReturnTheNumberOfDosesInTakenTheEIPPhase() {
        Therapy therapy = new Therapy();
        therapy.getPhases().setIPStartDate(today());
        therapy.getPhases().setEIPStartDate(today().plusDays(1));

        therapy.setNumberOfDosesTaken(EIP, 3, today());
        assertEquals(3, therapy.getNumberOfDosesTaken(EIP));
    }

    @Test
    public void shouldReturnTheNumberOfDosesTakenInTheCPPhase() {
        Therapy therapy = new Therapy();
        therapy.getPhases().setIPStartDate(today());
        therapy.getPhases().setEIPStartDate(today().plusDays(1));
        therapy.getPhases().setCPStartDate(today().plusDays(2));

        therapy.setNumberOfDosesTaken(CP, 4, today());
        assertEquals(4, therapy.getNumberOfDosesTaken(CP));
    }

    @Test
    public void shouldReturnZeroAsTheNumberOfDosesTakenInThePhaseWhenItWasNotStarted() {
        Therapy therapy = new Therapy();
        assertEquals(0, therapy.getNumberOfDosesTaken(IP));
    }

    @Test
    public void shouldNotSetTheNumberOfDosesTakenInPhaseWhenItHasNotStarted() {
        Therapy therapyWithoutIp = new Therapy();
        therapyWithoutIp.setNumberOfDosesTaken(IP, 4, today());
        assertEquals(0, therapyWithoutIp.getNumberOfDosesTaken(IP));
    }

    @Test
    public void shouldReturnTheTotalNumberOfDosesInTheIPPhase() {
        Therapy therapy = new TherapyBuilder().build();
        therapy.getPhases().setIPStartDate(today());
        assertEquals(24, therapy.getTotalDoesIn(IP));
    }

    @Test
    public void shouldReturnTheTotalNumberOfDosesInTheCPPhase() {
        Therapy therapy = new TherapyBuilder().build();
        therapy.getPhases().setCPStartDate(today());
        assertEquals(54, therapy.getTotalDoesIn(CP));
    }

    @Test
    public void shouldNotReturnTheTotalNumberOfDosesInTheIPPhaseWhenTherapyWasNeverOnIP() {
        Therapy therapy = new TherapyBuilder().build();
        assertEquals(0, therapy.getTotalDoesIn(IP));
    }

    @Test
    public void shouldAddDoseInterruptionToDoseInterruptionsWhenDoseIsMissed() {
        Therapy therapy = new TherapyBuilder().build();
        LocalDate startDate = new LocalDate();
        therapy.dosesMissedSince(startDate);

        DoseInterruption addedDoseInterruption = therapy.getDoseInterruptions().latestInterruption();
        assertEquals(startDate, addedDoseInterruption.startDate());
    }

    @Test
    public void shouldSetEndDateWhenDosesAreResumedOnLastDoseInterruption() {
        Therapy therapy = new TherapyBuilder().build();
        LocalDate startDate = new LocalDate();
        LocalDate endDate = startDate.plusDays(3);
        therapy.dosesMissedSince(startDate);
        therapy.dosesResumedOnAfterBeingInterrupted(endDate);

        DoseInterruption doseInterruption = therapy.getDoseInterruptions().latestInterruption();

        assertEquals(endDate, doseInterruption.endDate());
    }

    @Test
    public void shouldComputeTotalDosesToHaveBeenTakenOnlyTillCPEndDate() {
        Patient patient = new PatientBuilder().withDefaults().build();
        patient.startTherapy(newDate(2012, 3, 1));
        patient.nextPhaseName(Phase.CP);
        patient.endLatestPhase(newDate(2012, 3, 31));
        patient.startNextPhase();
        patient.setNumberOfDosesTaken(Phase.CP, 1, newDate(2012, 4, 30));
        patient.endLatestPhase(newDate(2012, 4, 30));

        assertEquals(newDate(2012, 4, 1), patient.getLastCompletedPhase().getStartDate());
        assertEquals(26, patient.getCurrentTherapy().getTotalDosesToHaveBeenTakenTillLastSunday());
    }

    @Test
    public void shouldReturnPretreatmentSputumResult(){
        Treatment currentTreatment = mock(Treatment.class);
        when(currentTreatment.getPreTreatmentSmearTestResult()).thenReturn(Positive);
        //when(currentTreatment.hasPreTreatmentResult()).thenReturn(true);

        Treatment olderTreatment = mock(Treatment.class);
        when(olderTreatment.getPreTreatmentSmearTestResult()).thenReturn(null);
        when(olderTreatment.hasPreTreatmentResult()).thenReturn(false);

        Therapy therapy = new TherapyBuilder().withTreatment(olderTreatment).withTreatment(currentTreatment).build();

        assertEquals(Positive, therapy.getPreTreatmentSputumResult());

        //verify(currentTreatment).hasPreTreatmentResult();
        verify(currentTreatment).getPreTreatmentSmearTestResult();
        verify(olderTreatment).hasPreTreatmentResult();
    }
    @Test
    public void shouldReturnPretreatmentWeightRecord(){
        Treatment currentTreatment = mock(Treatment.class);
        WeightStatisticsRecord weightStatisticsRecord = new WeightStatisticsRecord(SampleInstance.PreTreatment, 30.0, LocalDate.now());
        WeightStatisticsRecord olderTreatmentWeightStatisticsRecord = new WeightStatisticsRecord(SampleInstance.PreTreatment, 30.0, LocalDate.now());
        when(currentTreatment.getPreTreatmentWeightRecord()).thenReturn(weightStatisticsRecord);

        Treatment olderTreatment = mock(Treatment.class);
        when(olderTreatment.getPreTreatmentWeightRecord()).thenReturn(olderTreatmentWeightStatisticsRecord);
        when(olderTreatment.hasPreTreatmentWeightRecord()).thenReturn(false);

        Therapy therapy = new TherapyBuilder().withTreatment(olderTreatment).withTreatment(currentTreatment).build();

        assertEquals(weightStatisticsRecord, therapy.getPreTreatmentWeightRecord());

        verify(currentTreatment).getPreTreatmentWeightRecord();
        verify(olderTreatment).hasPreTreatmentWeightRecord();
    }
}
