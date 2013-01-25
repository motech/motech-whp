package org.motechproject.whp.patient.domain;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.model.DayOfWeek;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.common.domain.Phase;
import org.motechproject.whp.common.domain.SmearTestResult;
import org.motechproject.whp.common.domain.SputumTrackingInstance;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.builder.TherapyBuilder;
import org.motechproject.whp.patient.builder.TreatmentBuilder;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.motechproject.util.DateUtil.now;
import static org.motechproject.util.DateUtil.today;
import static org.motechproject.whp.common.domain.Phase.EIP;
import static org.motechproject.whp.common.domain.Phase.IP;
import static org.motechproject.whp.common.domain.SmearTestResult.Positive;
import static org.motechproject.whp.common.domain.TreatmentWeekInstance.currentAdherenceCaptureWeek;
import static org.motechproject.whp.common.domain.TreatmentWeekInstance.week;
import static org.motechproject.whp.patient.builder.PatientBuilder.patient;
import static org.motechproject.whp.patient.builder.TreatmentBuilder.treatment;

public class PatientTest {

    @Test
    public void shouldSetTreatmentAddedAsCurrentTreatment() {
        Patient patient = patient();
        Treatment treatment = treatment();
        patient.addTreatment(treatment, now(), now());
        assertEquals(treatment, patient.getCurrentTreatment());
    }

    @Test
    public void shouldUpdateTreatmentHistoryWhenNewTreatmentIdAdded() {
        Patient patient = patient();
        Treatment firstTreatment = patient.getCurrentTreatment();
        patient.addTreatment(treatment(), now(), now());

        assertArrayEquals(new Object[]{firstTreatment}, patient.getTreatmentHistory().toArray());
    }

    @Test
    public void shouldNotHaveAnyHistoryWhenTreatmentHasNeverBeenUpdated() {
        Patient patientWithOneTreatment = patient();
        assertTrue(patientWithOneTreatment.getTreatmentHistory().isEmpty());
    }

    @Test
    public void shouldAddCurrentTreatmentToHistoryWhenNewTreatmentIsAdded() {
        Patient patient = patient();
        Treatment treatment = patient.getCurrentTreatment();
        patient.addTreatment(treatment(), now(), now());

        assertArrayEquals(new Object[]{treatment}, patient.getTreatmentHistory().toArray());
    }

    @Test
    public void shouldUpdateLastModifiedDateWhenTreatmentIsClosed() {
        DateTime now = now();
        Patient patient = patient();
        patient.closeCurrentTreatment(TreatmentOutcome.Cured, now);

        assertEquals(now, patient.getLastModifiedDate());
    }

    @Test
    public void shouldUpdateLastModifiedDateWhenTreatmentIsPaused() {
        DateTime now = now();
        Patient patient = patient();
        patient.pauseCurrentTreatment("paws", now);

        assertEquals(now, patient.getLastModifiedDate());
    }

    @Test
    public void shouldUpdateLastModifiedDateWhenTreatmentIsRestarted() {
        DateTime now = now();
        Patient patient = patient();
        patient.pauseCurrentTreatment("paws", now.minusHours(2));

        patient.restartCurrentTreatment("swap", now);

        assertEquals(now, patient.getLastModifiedDate());
    }

    @Test
    public void shouldStoreIdsInLowerCase() {
        Patient patient = new Patient();
        patient.setPatientId("QWER");
        assertEquals("qwer", patient.getPatientId());

        patient = new Patient("QWER", "", "", Gender.M, "");
        assertEquals("qwer", patient.getPatientId());

        patient.setPatientId(null);
        assertEquals(null, patient.getPatientId());
    }

    @Test
    public void shouldGetCurrentTreatmentIfGivenDateIsInCurrentTreatmentPeriod() {
        Patient patient = new PatientBuilder().withDefaults().build();
        LocalDate startDate = new LocalDate(2012, 1, 2);
        LocalDate endDate = new LocalDate(2012, 3, 15);
        patient.getCurrentTreatment().setStartDate(startDate);
        patient.getCurrentTreatment().setEndDate(endDate);
        assertEquals(patient.getCurrentTreatment(), patient.getTreatment(startDate));
        assertEquals(patient.getCurrentTreatment(), patient.getTreatment(endDate));
        assertEquals(patient.getCurrentTreatment(), patient.getTreatment(startDate.plusDays(15)));
    }

    @Test
    public void settingIdsShouldHandleNullValues() {
        Patient patient = new Patient("", "", "", Gender.F, "");
        patient.setPatientId(null);
        assertEquals(null, patient.getPatientId());

        patient = new Patient(null, "", "", Gender.F, "");
        assertEquals(null, patient.getPatientId());

    }

    @Test
    public void shouldSetNextPhaseOnCurrentTherapy() {
        Patient patient = new PatientBuilder().withDefaults().build();

        patient.nextPhaseName(Phase.EIP);

        assertEquals(patient.getCurrentTherapy().getPhases().getNextPhase(), Phase.EIP);
    }

    @Test
    public void shouldEndCurrentPhaseIfNotNull() {
        Patient patient = new PatientBuilder().withDefaults().build();
        LocalDate phaseEndDate = new LocalDate(2012, 4, 1);
        patient.startTherapy(new LocalDate(2012, 3, 1));

        patient.endLatestPhase(phaseEndDate);

        assertEquals(phaseEndDate, patient.getCurrentTherapy().getPhaseEndDate(IP));
    }

    @Test
    public void startNextPhaseShouldSetStartDateOnNextPhaseAsNextCalendarDateOfEndDateOfLastCompletedPhase() {
        Patient patient = new PatientBuilder().withDefaults().build();
        LocalDate phaseEndDate = new LocalDate(2012, 4, 1);
        patient.startTherapy(new LocalDate(2012, 3, 1));
        patient.nextPhaseName(EIP);

        patient.endLatestPhase(phaseEndDate);
        patient.startNextPhase();

        assertEquals(phaseEndDate.plusDays(1), patient.getCurrentTherapy().getPhaseStartDate(EIP));
        assertNull(patient.getCurrentTherapy().getPhases().getNextPhase());
    }

    @Test
    public void isTransitioningShouldReturnTrueIfCurrentPhaseIsNull() {
        Patient patient = new PatientBuilder().withDefaults().build();
        patient.startTherapy(new LocalDate(2012, 3, 1));
        patient.endLatestPhase(new LocalDate(2012, 4, 1));

        assertTrue(patient.isTransitioning());
    }

    @Test
    public void hasPhaseToTransitionToShouldReturnTrueIfNextPhaseNameIsNotNull() {
        Patient patient = new PatientBuilder().withDefaults().build();
        patient.nextPhaseName(Phase.EIP);

        assertTrue(patient.hasPhaseToTransitionTo());
    }

    @Test
    public void shouldGetCurrentTreatment_WhenDateIsTheStartDayOfCurrentTreatment() {
        Patient patient = new PatientBuilder().withDefaults().withCurrentTreatmentStartDate(date(2011, 10, 1)).build();
        patient.closeCurrentTreatment(TreatmentOutcome.Defaulted, dateTime(2011, 12, 1));

        patient.addTreatment(new TreatmentBuilder().withDefaults().build(), dateTime(2012, 1, 1), now());
        patient.closeCurrentTreatment(TreatmentOutcome.Defaulted, dateTime(2012, 3, 15));

        Treatment currentTreatment = new TreatmentBuilder().withDefaults().withTbId("current").build();
        patient.addTreatment(currentTreatment, dateTime(2012, 4, 1), now());

        assertEquals(currentTreatment, patient.getTreatment(date(2012, 4, 1)));
    }

    @Test
    public void shouldGetCurrentTreatment_WhenDateIsAfterStartDayOfCurrentTreatment() {
        Patient patient = new PatientBuilder().withDefaults().withCurrentTreatmentStartDate(date(2011, 10, 1)).build();
        patient.closeCurrentTreatment(TreatmentOutcome.Defaulted, dateTime(2011, 12, 1));

        patient.addTreatment(new TreatmentBuilder().withDefaults().build(), dateTime(2012, 1, 1), now());
        patient.closeCurrentTreatment(TreatmentOutcome.Defaulted, dateTime(2012, 3, 15));

        Treatment currentTreatment = new TreatmentBuilder().withDefaults().withTbId("current").build();
        patient.addTreatment(currentTreatment, dateTime(2012, 4, 1), now());

        assertEquals(currentTreatment, patient.getTreatment(date(2012, 4, 2)));
    }

    @Test
    public void shouldGetFirstTreatment_WhenDateIsSameAsEndDate() {
        Patient patient = new PatientBuilder().withDefaults().withCurrentTreatmentStartDate(date(2011, 10, 1)).build();
        Treatment firstTreatment = patient.getCurrentTreatment();
        patient.closeCurrentTreatment(TreatmentOutcome.Defaulted, dateTime(2011, 12, 1));

        patient.addTreatment(new TreatmentBuilder().withDefaults().build(), dateTime(2012, 1, 1), now());
        patient.closeCurrentTreatment(TreatmentOutcome.Defaulted, dateTime(2012, 3, 15));

        patient.addTreatment(new TreatmentBuilder().withDefaults().withTbId("current").build(), dateTime(2012, 4, 1), now());

        assertEquals(firstTreatment, patient.getTreatment(date(2011, 12, 1)));
    }

    @Test
    public void shouldGetLatestTreatment_WhenTreatmentIsCloseAndOpenedOnSameDay() {
        Patient patient = new PatientBuilder().withDefaults().withCurrentTreatmentStartDate(date(2011, 10, 1)).build();
        patient.closeCurrentTreatment(TreatmentOutcome.Defaulted, dateTime(2011, 12, 1));

        Treatment secondTreatment = new TreatmentBuilder().withDefaults().build();
        patient.addTreatment(secondTreatment, dateTime(2011, 12, 1), now());
        patient.closeCurrentTreatment(TreatmentOutcome.Defaulted, dateTime(2012, 3, 15));

        patient.addTreatment(new TreatmentBuilder().withDefaults().withTbId("current").build(), dateTime(2012, 4, 1), now());

        assertEquals(secondTreatment, patient.getTreatment(date(2011, 12, 1)));
    }

    @Test
    public void shouldSetOnActiveTreatmentWhenFirstTreatmentIsAdded() {
        Patient patient = patient();
        assertTrue(patient.isOnActiveTreatment());
    }

    @Test
    public void shouldSetOnActiveTreatmentWhenSubsequentTreatmentIsAdded() {
        Patient patient = patient();
        patient.setOnActiveTreatment(false);

        Treatment treatment = treatment();
        patient.addTreatment(treatment, now(), now());

        assertTrue(patient.isOnActiveTreatment());
    }

    @Test
    public void shouldUnSetOnActiveTreatmentWhenTreatmentIsClosed() {
        Patient patient = patient();
        patient.closeCurrentTreatment(TreatmentOutcome.TreatmentCompleted, now());

        assertFalse(patient.isOnActiveTreatment());
    }

    @Test
    public void shouldReturnNumberOfRemainingDosesInLastCompletedPhase() {
        Patient patient = PatientBuilder.patient();
        patient.startTherapy(today().minusMonths(5));

        patient.setNumberOfDosesTaken(Phase.IP, 22, today().minusMonths(5));

        patient.endLatestPhase(today().minusMonths(4));

        patient.nextPhaseName(Phase.EIP);
        patient.startNextPhase();

        assertEquals(2, patient.getRemainingDosesInLastCompletedPhase());
    }

    @Test
    public void shouldReturnNumberOfDosesForGivenPhase() {
        assertEquals(Integer.valueOf(24), patient().numberOfDosesForPhase(Phase.IP));
        assertEquals(Integer.valueOf(12), patient().numberOfDosesForPhase(Phase.EIP));
        assertEquals(Integer.valueOf(54), patient().numberOfDosesForPhase(Phase.CP));
    }

    @Test
    public void shouldReturnTrueIfCurrentPhaseIsDoseComplete() {
        Patient patient = PatientBuilder.patient();
        patient.startTherapy(today().minusMonths(5));

        // x >= 24.
        patient.setNumberOfDosesTaken(Phase.IP, 25, today().minusMonths(5));

        assertTrue(patient.currentPhaseDoseComplete());
    }

    @Test
    public void shouldReturnFalseIfCurrentPhaseIsNotDoseComplete() {
        Patient patient = PatientBuilder.patient();
        patient.startTherapy(today().minusMonths(5));

        patient.setNumberOfDosesTaken(Phase.IP, 22, today().minusMonths(5));

        assertFalse(patient.currentPhaseDoseComplete());
    }

    @Test
    public void shouldReturnNumberOfWeeksElapsedSinceStartOfTherapy() {
        Patient patient = PatientBuilder.patient();
        patient.startTherapy(today().minusWeeks(5));

        assertEquals(Integer.valueOf(5), patient.getWeeksElapsed());
    }

    @Test
    public void shouldReturnTotalDosesToHaveBeenTakenTillLastSunday() {
        //patient on 3 pills a day category
        Patient patient = PatientBuilder.patient();
        patient.startTherapy(currentAdherenceCaptureWeek().startDate());

        assertEquals(3, patient.getTotalDosesToHaveBeenTakenTillLastSunday());
    }

    @Test
    public void shouldReturnNumberOfDosesTakenTillLastSunday() {
        Patient patient = PatientBuilder.patient();
        patient.startTherapy(today().minusWeeks(4));

        patient.setNumberOfDosesTaken(Phase.IP, 12, today().minusWeeks(4));
        patient.setNumberOfDosesTaken(Phase.IP, 9, week(today().minusWeeks(4)).dateOf(DayOfWeek.Sunday));

        assertEquals(9, patient.getTotalNumberOfDosesTakenTillLastSunday(today().minusWeeks(4)));
    }

    @Test
    public void shouldReturnTheCumulativeNumberOfDosesNotTakenTillLastSunday() {
        Patient patient = PatientBuilder.patient();
        patient.startTherapy(currentAdherenceCaptureWeek().startDate().minusWeeks(20));

        patient.setNumberOfDosesTaken(Phase.IP, 24, currentAdherenceCaptureWeek().startDate().minusWeeks(20));

        patient.endLatestPhase(today().minusMonths(4));

        patient.nextPhaseName(Phase.EIP);
        patient.startNextPhase();

        patient.setNumberOfDosesTaken(Phase.EIP, 8, currentAdherenceCaptureWeek().startDate().minusWeeks(1));
        patient.setNumberOfDosesTaken(Phase.EIP, 9, currentAdherenceCaptureWeek().startDate());

        //20 weeks back + the current week (till "last" Sunday) * number of doses per week - (total number of taken doses till last sunday)
        assertEquals(21 * 3 - 24 - 9, patient.getCumulativeDosesNotTaken());

    }

    @Test
    public void shouldReturnZero_AndNotNegative_MissingDosesEvenIfAdherenceHasBeenRecordedBeforeEndOfWeek_ByAdmin() {
        Patient patient = PatientBuilder.patient();
        patient.startTherapy(currentAdherenceCaptureWeek().dateOf(DayOfWeek.Sunday));

        patient.setNumberOfDosesTaken(Phase.IP, 3, currentAdherenceCaptureWeek().startDate().minusWeeks(1));

        //0 - 3 = 0 (in the NamNam field)
        assertEquals(0, patient.getCumulativeDosesNotTaken());
    }

    @Test
    public void shouldReturnLongestDoseInterruption() {
        Patient patient = PatientBuilder.patient();
        patient.startTherapy(currentAdherenceCaptureWeek().startDate().minusWeeks(20));

        DoseInterruptions doseInterruptions = new DoseInterruptions();

        DoseInterruption doseInterruption1 = new DoseInterruption(new LocalDate(2012, 6, 28));
        doseInterruption1.endMissedPeriod(new LocalDate(2012, 6, 30));
        DoseInterruption doseInterruption2 = new DoseInterruption(new LocalDate(2012, 7, 2));
        doseInterruption2.endMissedPeriod(new LocalDate(2012, 7, 6));
        DoseInterruption doseInterruption3 = new DoseInterruption(new LocalDate(2012, 7, 8));
        doseInterruption3.endMissedPeriod(new LocalDate(2012, 7, 14));

        doseInterruptions.addAll(asList(doseInterruption1, doseInterruption2, doseInterruption3));

        patient.getCurrentTherapy().setDoseInterruptions(doseInterruptions);

        assertEquals(String.valueOf(1.0), patient.getLongestDoseInterruption());
    }

    @Test
    public void shouldReturnEmptyListOfDoseDatesIfTherapyHasNotStarted() {
        Patient patient = PatientBuilder.patient();
        List<LocalDate> doseDates = patient.getDoseDatesTill(today());
        assertThat(doseDates.size(), is(0));
    }


    @Test
    public void shouldReturnFalseIfAdherenceNotProvidedForLastWeek() {
        Patient patient = new PatientBuilder().withDefaults().build();
        assertFalse(patient.hasAdherenceForLastReportingWeekForCurrentTherapy());
    }

    @Test
    public void shouldReturnFalseIfHasCurrentTherapyAndAdherenceProvidedForOldTherapy() {
        Patient patient = new PatientBuilder().withDefaults().withAdherenceProvidedForLastWeek().build();
        patient.addTreatment(new Treatment(), new Therapy(), DateUtil.now(), now());
        assertFalse(patient.hasAdherenceForLastReportingWeekForCurrentTherapy());
    }

    @Test
    public void addingNewTherapyShouldResetLastAdherenceProvidedWeekStartDate() {
        Patient patient = patient();
        patient.setLastAdherenceWeekStartDate(new LocalDate(2012, 7, 7));
        patient.addTreatment(new Treatment(), new Therapy(), now(), now());
        assertNull(patient.getLastAdherenceWeekStartDate());
    }

    @Test
    public void shouldRevertAutoPhaseCompletion_whenCurrentPhaseBecomesIncomplete() {
        Patient patient = patient();
        patient.startTherapy(new LocalDate(2012, 3, 1));
        PhaseRecord previousPhase = patient.getCurrentPhase();
        patient.endLatestPhase(new LocalDate(2012, 5, 11));
        patient.revertAutoCompleteOfLastPhase();

        assertNotNull(patient.getCurrentPhase()); // Current phase is not closed
        assertNull(patient.getCurrentPhase().getEndDate()); // Current phase's end date is undefined as phase is still ongoing
        assertEquals(previousPhase, patient.getCurrentPhase()); // Phase transition shouldn't happen as it was reverted
    }

    @Test
    public void shouldGetPretreatmentCumulativeSmearTestResult() {
        Therapy therapy = mock(Therapy.class);
        when(therapy.getPreTreatmentSputumResult()).thenReturn(Positive);
        Patient patient = new PatientBuilder().withDefaults().withTherapy(therapy).build();

        SmearTestResult smearTestResult = patient.getPreTreatmentSputumResult();

        assertEquals(Positive, smearTestResult);
        verify(therapy).getPreTreatmentSputumResult();
    }

    @Test
    public void shouldGetPreTreatmentWeightRecord() {
        Therapy therapy = mock(Therapy.class);
        WeightStatisticsRecord weightStatisticsRecord = new WeightStatisticsRecord(SputumTrackingInstance.PreTreatment, 30.0, LocalDate.now());
        when(therapy.getPreTreatmentWeightRecord()).thenReturn(weightStatisticsRecord);
        Patient patient = new PatientBuilder().withDefaults().withTherapy(therapy).build();

        assertEquals(weightStatisticsRecord, patient.getPreTreatmentWeightRecord());
        verify(therapy).getPreTreatmentWeightRecord();
    }

    private LocalDate date(int year, int monthOfYear, int dayOfMonth) {
        return new LocalDate(year, monthOfYear, dayOfMonth);
    }

    private DateTime dateTime(int year, int monthOfYear, int dayOfMonth) {
        return new LocalDate(year, monthOfYear, dayOfMonth).toDateTimeAtCurrentTime();
    }

    @Test
    public void shouldGetTreatmentHistoryFromCurrentTherapy() {
        Patient patient = new PatientBuilder().withDefaults().withCurrentTreatmentStartDate(date(2011, 10, 1)).build();
        Treatment treatment1 = patient.getCurrentTreatment();
        patient.closeCurrentTreatment(TreatmentOutcome.Defaulted, dateTime(2011, 12, 1));

        Treatment treatment2 = new TreatmentBuilder().withDefaults().build();
        patient.addTreatment(treatment2, dateTime(2012, 1, 1), now());
        patient.closeCurrentTreatment(TreatmentOutcome.Defaulted, dateTime(2012, 3, 15));

        Treatment currentTreatment = new TreatmentBuilder().withDefaults().withTbId("current").build();
        patient.addTreatment(currentTreatment, dateTime(2012, 4, 1), now());

        assertThat(patient.getTreatmentHistory(), hasItems(treatment1, treatment2));
        assertThat(patient.getTreatmentHistory().size(), is(2));
    }

    @Test
    public void shouldGetAllTreatmentsFromCurrentTherapy() {
        Patient patient = new PatientBuilder().withDefaults().withCurrentTreatmentStartDate(date(2011, 10, 1)).build();
        Treatment treatment1 = patient.getCurrentTreatment();
        patient.closeCurrentTreatment(TreatmentOutcome.Defaulted, dateTime(2011, 12, 1));

        Treatment treatment2 = new TreatmentBuilder().withDefaults().build();
        patient.addTreatment(treatment2, dateTime(2012, 1, 1), now());
        patient.closeCurrentTreatment(TreatmentOutcome.Defaulted, dateTime(2012, 3, 15));

        Treatment currentTreatment = new TreatmentBuilder().withDefaults().withTbId("current").build();
        patient.addTreatment(currentTreatment, dateTime(2012, 4, 1), now());

        assertThat(patient.getAllTreatments(), hasItems(treatment1, treatment2, currentTreatment));
        assertThat(patient.getAllTreatments().size(), is(3));
    }

    @Test
    public void shouldGetTreatmentStartDateOfTreatmentWithTBId() {
        LocalDate today = DateUtil.today();

        Treatment treatment = new TreatmentBuilder().withDefaults().withStartDate(today).withTbId("tbId").build();
        Therapy therapy = new TherapyBuilder().withTreatment(treatment).build();
        Patient patient = new PatientBuilder().withDefaults().withTherapy(therapy).build();

        assertEquals(today, patient.getTreatmentStartDate("tbId"));
    }

    @Test
    public void shouldReturnNullAsTreatmentStartDateWhenTBIdIsUnknown() {
        LocalDate today = DateUtil.today();

        Treatment treatment = new TreatmentBuilder().withDefaults().withStartDate(today).withTbId("tbId").build();
        Therapy therapy = new TherapyBuilder().withTreatment(treatment).build();
        Patient patient = new PatientBuilder().withDefaults().withTherapy(therapy).build();

        assertEquals(null, patient.getTreatmentStartDate("unkownTbId"));
    }

    @Test
    public void shouldFetchTreatmentStartDateFromTreatmentOfAHistoricalTherapy() {
        LocalDate today = DateUtil.today();

        Treatment treatment = new TreatmentBuilder().withDefaults().withTbId("tbId").withStartDate(today.minusDays(1)).build();
        Therapy historicalTherapy = new TherapyBuilder().withTreatmentStartingOn(treatment, today.minusDays(1)).build();
        Therapy currentTherapy = new TherapyBuilder().build();

        Patient patient = new PatientBuilder().withDefaults().withTherapy(currentTherapy).build();
        patient.setTherapyHistory(asList(historicalTherapy));
        assertEquals(today.minusDays(1), patient.getTreatmentStartDate("tbId"));
    }

    @Test
    public void shouldCheckForTbIdAmongTreatmentsAcrossAllTherapies() {
        String currentTherapyCurrentTreatment = "currentTherapyCurrentTreatment";
        String currentTherapyPreviousTreatment = "CurrentTherapyPreviousTreatment";
        String closedTherapyCurrentTreatment = "ClosedTherapyCurrentTreatment";
        String closedTherapyPreviousTreatment = "ClosedTherapyPreviousTreatment";

        Patient patient = new PatientBuilder().withDefaults().build();

        // Current Therapy
        Treatment closedTreatment = new TreatmentBuilder().withDefaults().build();
        closedTreatment.setTbId(currentTherapyPreviousTreatment);
        closedTreatment.close(TreatmentOutcome.Cured, now());
        patient.getCurrentTherapy().addTreatment(closedTreatment, now());

        // Historised Therapy
        Therapy closedTherapy = new Therapy(new TreatmentCategory(), DiseaseClass.E, 25);
        Treatment currentTreatment = new TreatmentBuilder().withDefaults().build();
        currentTreatment.setTbId(closedTherapyCurrentTreatment);
        closedTreatment = new TreatmentBuilder().withDefaults().build();
        closedTreatment.setTbId(closedTherapyPreviousTreatment);
        closedTreatment.close(TreatmentOutcome.Cured, now());
        closedTherapy.addTreatment(closedTreatment, now());
        closedTherapy.addTreatment(currentTreatment, now());

        Treatment dummy = new TreatmentBuilder().withDefaults().build();
        patient.addTreatment(dummy, closedTherapy, now(), now());
        patient.getCurrentTreatment().setTbId(currentTherapyCurrentTreatment);

        //Assertions
        assertTrue(patient.hasTreatment(currentTherapyCurrentTreatment));
        assertTrue(patient.hasTreatment(currentTherapyPreviousTreatment));
        assertTrue(patient.hasTreatment(closedTherapyCurrentTreatment));
        assertTrue(patient.hasTreatment(closedTherapyPreviousTreatment));
    }

    @Test
    public void shouldGetTheTreatmentCategory() {
        Patient govtCategoryPatient = new PatientBuilder().withDefaults().build();
        assertTrue(govtCategoryPatient.getTreatmentCategory().isGovernmentCategory());

        Patient privateCategoryPatient = new PatientBuilder().withDefaults().withPrivateCategory().build();
        assertFalse(privateCategoryPatient.getTreatmentCategory().isGovernmentCategory());
    }

    @Test
    public void shouldFetchProviderIdFromCurrentTreatment() {
        String providerId = "providerid";
        Patient patient = new PatientBuilder().withDefaults().withTreatmentUnderProviderId(providerId).build();
        assertEquals(providerId, patient.getCurrentProviderId());
    }
}
