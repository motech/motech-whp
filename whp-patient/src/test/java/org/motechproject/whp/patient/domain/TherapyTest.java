package org.motechproject.whp.patient.domain;

import org.hamcrest.core.Is;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.builder.TherapyBuilder;
import org.motechproject.whp.patient.builder.TreatmentBuilder;
import org.motechproject.whp.refdata.domain.Phase;
import org.motechproject.whp.refdata.domain.TherapyStatus;
import org.motechproject.whp.refdata.domain.TreatmentCategory;

import static junit.framework.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.motechproject.util.DateUtil.*;
import static org.motechproject.whp.refdata.domain.Phase.*;
import static org.motechproject.whp.refdata.domain.SampleInstance.*;
import static org.motechproject.whp.refdata.domain.SmearTestResult.Negative;
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
    public void shouldReturnLatestPretreatmentSputumResult() {
        SmearTestResults smearTestResultsForTreatment1 = new SmearTestResults();
        smearTestResultsForTreatment1.add(new SmearTestRecord(PreTreatment, DateUtil.today(), Positive, DateUtil.today(), Positive));
        smearTestResultsForTreatment1.add(new SmearTestRecord(ExtendedIP, DateUtil.today(), Positive, DateUtil.today(), Positive));

        SmearTestResults smearTestResultsForTreatment2 = new SmearTestResults();
        smearTestResultsForTreatment2.add(new SmearTestRecord(EndIP, DateUtil.today(), Positive, DateUtil.today(), Positive));
        smearTestResultsForTreatment2.add(new SmearTestRecord(ExtendedIP, DateUtil.today(), Positive, DateUtil.today(), Positive));

        SmearTestResults smearTestResultsForCurrentTreatment = new SmearTestResults();
        smearTestResultsForCurrentTreatment.add(new SmearTestRecord(PreTreatment, DateUtil.today(), Negative, DateUtil.today(), Negative));
        smearTestResultsForCurrentTreatment.add(new SmearTestRecord(TwoMonthsIntoCP, DateUtil.today(), Positive, DateUtil.today(), Positive));

        Treatment treatment1 = new TreatmentBuilder().withDefaults().withSmearTestResults(smearTestResultsForTreatment1).build();
        Treatment treatment2 = new TreatmentBuilder().withDefaults().withSmearTestResults(smearTestResultsForTreatment2).build();
        Treatment currentTreatment = new TreatmentBuilder().withDefaults().withSmearTestResults(smearTestResultsForCurrentTreatment).build();

        Therapy therapy = new TherapyBuilder().withTreatment(treatment1).withTreatment(treatment2).withTreatment(currentTreatment).build();

        assertThat(therapy.getPreTreatmentSputumResult(), is(Negative));
    }


    @Test
    public void shouldReturnPretreatmentWeightRecord() {
        WeightStatistics weightStatisticsForTreatment1 = new WeightStatistics();
        weightStatisticsForTreatment1.add(new WeightStatisticsRecord(PreTreatment, 50.0, DateUtil.today()));
        weightStatisticsForTreatment1.add(new WeightStatisticsRecord(ExtendedIP, 55.0, DateUtil.today()));

        WeightStatistics weightStatisticsForTreatment2 = new WeightStatistics();
        weightStatisticsForTreatment2.add(new WeightStatisticsRecord(EndIP, 60.0, DateUtil.today()));
        weightStatisticsForTreatment2.add(new WeightStatisticsRecord(ExtendedIP, 65.0, DateUtil.today()));

        WeightStatistics weightStatisticsForCurrentTreatment = new WeightStatistics();
        weightStatisticsForCurrentTreatment.add(new WeightStatisticsRecord(PreTreatment, 70.0, DateUtil.today()));
        weightStatisticsForCurrentTreatment.add(new WeightStatisticsRecord(TwoMonthsIntoCP, 75.0, DateUtil.today()));

        Treatment treatment1 = new TreatmentBuilder().withDefaults().withWeightStatistics(weightStatisticsForTreatment1).build();
        Treatment treatment2 = new TreatmentBuilder().withDefaults().withWeightStatistics(weightStatisticsForTreatment2).build();
        Treatment currentTreatment = new TreatmentBuilder().withDefaults().withWeightStatistics(weightStatisticsForCurrentTreatment).build();

        Therapy therapy = new TherapyBuilder().withTreatment(treatment1).withTreatment(treatment2).withTreatment(currentTreatment).build();

        assertThat(therapy.getPreTreatmentWeightRecord(), is(weightStatisticsForCurrentTreatment.resultForInstance(PreTreatment)));
    }

    @Test
    public void shouldGetAllTreatmentsWithCurrentTreatment() {
        Treatment treatment1 = new TreatmentBuilder().withDefaults().build();

        Treatment treatment2 = new TreatmentBuilder().withDefaults().build();

        Treatment currentTreatment = new TreatmentBuilder().withDefaults().withTbId("current").build();
        Therapy therapy = new TherapyBuilder().withTreatment(treatment1).withTreatment(treatment2).withTreatment(currentTreatment).build();

        assertThat(therapy.getAllTreatments(), hasItems(treatment1, treatment2, currentTreatment));
        assertThat(therapy.getAllTreatments().size(), Is.is(3));
    }

    @Test
    public void shouldGetAggregateSmearTestResults() {
        SmearTestResults smearTestResultsForTreatment1 = new SmearTestResults();
        smearTestResultsForTreatment1.add(new SmearTestRecord(PreTreatment, DateUtil.today(), Positive, DateUtil.today(), Positive));
        smearTestResultsForTreatment1.add(new SmearTestRecord(ExtendedIP, DateUtil.today(), Positive, DateUtil.today(), Positive));

        SmearTestResults smearTestResultsForTreatment2 = new SmearTestResults();
        smearTestResultsForTreatment2.add(new SmearTestRecord(EndIP, DateUtil.today(), Positive, DateUtil.today(), Positive));
        smearTestResultsForTreatment2.add(new SmearTestRecord(ExtendedIP, DateUtil.today(), Positive, DateUtil.today(), Positive));

        SmearTestResults smearTestResultsForCurrentTreatment = new SmearTestResults();
        smearTestResultsForCurrentTreatment.add(new SmearTestRecord(PreTreatment, DateUtil.today(), Negative, DateUtil.today(), Negative));
        smearTestResultsForCurrentTreatment.add(new SmearTestRecord(TwoMonthsIntoCP, DateUtil.today(), Positive, DateUtil.today(), Positive));

        Treatment treatment1 = new TreatmentBuilder().withDefaults().withSmearTestResults(smearTestResultsForTreatment1).build();
        Treatment treatment2 = new TreatmentBuilder().withDefaults().withSmearTestResults(smearTestResultsForTreatment2).build();
        Treatment currentTreatment = new TreatmentBuilder().withDefaults().withSmearTestResults(smearTestResultsForCurrentTreatment).build();

        Therapy therapy = new TherapyBuilder().withTreatment(treatment1).withTreatment(treatment2).withTreatment(currentTreatment).build();

        SmearTestResults smearTestResults = therapy.getAggregatedSmearTestResults();
        assertThat(smearTestResults.resultForInstance(PreTreatment), is(smearTestResultsForCurrentTreatment.resultForInstance(PreTreatment)));
        assertThat(smearTestResults.resultForInstance(ExtendedIP), is(smearTestResultsForTreatment2.resultForInstance(ExtendedIP)));
        assertThat(smearTestResults.resultForInstance(TwoMonthsIntoCP), is(smearTestResultsForCurrentTreatment.resultForInstance(TwoMonthsIntoCP)));
        assertThat(smearTestResults.resultForInstance(EndIP), is(smearTestResultsForTreatment2.resultForInstance(EndIP)));

        assertThat(smearTestResults.size(), is(4));
    }

    @Test
    public void shouldGetAggregatedWeightStatistics() {
        WeightStatistics weightStatisticsForTreatment1 = new WeightStatistics();
        weightStatisticsForTreatment1.add(new WeightStatisticsRecord(PreTreatment, 50.0, DateUtil.today()));
        weightStatisticsForTreatment1.add(new WeightStatisticsRecord(ExtendedIP, 55.0, DateUtil.today()));

        WeightStatistics weightStatisticsForTreatment2 = new WeightStatistics();
        weightStatisticsForTreatment2.add(new WeightStatisticsRecord(EndIP, 60.0, DateUtil.today()));
        weightStatisticsForTreatment2.add(new WeightStatisticsRecord(ExtendedIP, 65.0, DateUtil.today()));

        WeightStatistics weightStatisticsForCurrentTreatment = new WeightStatistics();
        weightStatisticsForCurrentTreatment.add(new WeightStatisticsRecord(PreTreatment, 70.0, DateUtil.today()));
        weightStatisticsForCurrentTreatment.add(new WeightStatisticsRecord(TwoMonthsIntoCP, 75.0, DateUtil.today()));

        Treatment treatment1 = new TreatmentBuilder().withDefaults().withWeightStatistics(weightStatisticsForTreatment1).build();
        Treatment treatment2 = new TreatmentBuilder().withDefaults().withWeightStatistics(weightStatisticsForTreatment2).build();
        Treatment currentTreatment = new TreatmentBuilder().withDefaults().withWeightStatistics(weightStatisticsForCurrentTreatment).build();

        Therapy therapy = new TherapyBuilder().withTreatment(treatment1).withTreatment(treatment2).withTreatment(currentTreatment).build();

        WeightStatistics weightStatistics = therapy.getAggregatedWeightStatistics();
        assertThat(weightStatistics.resultForInstance(PreTreatment), is(weightStatisticsForCurrentTreatment.resultForInstance(PreTreatment)));
        assertThat(weightStatistics.resultForInstance(ExtendedIP), is(weightStatisticsForTreatment2.resultForInstance(ExtendedIP)));
        assertThat(weightStatistics.resultForInstance(TwoMonthsIntoCP), is(weightStatisticsForCurrentTreatment.resultForInstance(TwoMonthsIntoCP)));
        assertThat(weightStatistics.resultForInstance(EndIP), is(weightStatisticsForTreatment2.resultForInstance(EndIP)));

        assertThat(weightStatistics.size(), is(4));
    }

    @Test
    public void shouldFetchStartDateOfTreatment() {
        LocalDate today = DateUtil.today();

        Treatment currentTreatment = new TreatmentBuilder().withDefaults().withStartDate(today).withTbId("tbId").build();

        Therapy therapy = new TherapyBuilder().withTreatment(currentTreatment).build();
        assertEquals(today, therapy.getTreatmentStartDate("tbId"));
    }

    @Test
    public void shouldReturnNullIfTbIdIsUnknown() {
        LocalDate today = DateUtil.today();

        Treatment currentTreatment = new TreatmentBuilder().withDefaults().withStartDate(today).withTbId("tbId").build();

        Therapy therapy = new TherapyBuilder().withTreatment(currentTreatment).build();
        assertNull(therapy.getTreatmentStartDate("unknownTbId"));
    }

    @Test
    public void shouldFetchStartDateOfAHistoricalTreatment() {
        LocalDate today = DateUtil.today();

        Treatment historicalTreatment = new TreatmentBuilder().withDefaults().withStartDate(today.minusDays(1)).withTbId("tbId1").build();
        Treatment currentTreatment = new TreatmentBuilder().withDefaults().withStartDate(today).withTbId("tbId2").build();

        Therapy therapy = new TherapyBuilder().withTreatmentStartingOn(historicalTreatment, historicalTreatment.getStartDate()).build();
        therapy.addTreatment(currentTreatment, now());

        assertEquals(today.minusDays(1), therapy.getTreatmentStartDate("tbId1"));
    }
}
