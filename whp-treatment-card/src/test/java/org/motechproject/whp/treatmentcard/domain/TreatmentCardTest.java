package org.motechproject.whp.treatmentcard.domain;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.builder.TherapyBuilder;
import org.motechproject.whp.patient.builder.TreatmentBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Therapy;
import org.motechproject.whp.patient.domain.Treatment;
import org.motechproject.whp.refdata.domain.Phase;
import org.motechproject.whp.treatmentcard.builder.AdherenceDataBuilder;

import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.motechproject.util.DateUtil.newDate;
import static org.motechproject.util.DateUtil.today;
import static org.motechproject.whp.patient.builder.PatientBuilder.patient;

public class TreatmentCardTest {

    private final LocalDate today = today();

    @Test
    public void shouldLimitIPSectionTillTodayForPatientOnIP() {
        Patient patient = patient();
        patient.startTherapy(today.minusMonths(5));

        assertEquals(today, new TreatmentCard(patient).ipBoxAdherenceEndDate());
    }

    @Test
    public void shouldReturnTreatmentHistory() {
        Patient patient = new PatientBuilder().withPatientId("patientId").build();
        LocalDate firstTreatmentStartDate = new LocalDate(2011, 12, 1);
        Therapy therapy = new TherapyBuilder().withStartDate(firstTreatmentStartDate).build();
        LocalDate firstTreatmentEndDate = new LocalDate(2011, 12, 31);
        Treatment firstTreatment = new TreatmentBuilder().withStartDate(firstTreatmentStartDate).withEndDate(firstTreatmentEndDate).withProviderId("p1").build();
        LocalDate secondTreatmentStartDate = new LocalDate(2012, 1, 1);
        LocalDate secondTreatmentEndDate = new LocalDate(2012, 1, 31);
        Treatment pastTreatment2 = new TreatmentBuilder().withStartDate(secondTreatmentStartDate).withEndDate(secondTreatmentEndDate).withProviderId("p2").build();
        LocalDate currentTreatmentStartDate = new LocalDate(2012, 2, 1);
        Treatment currentTreatment = new TreatmentBuilder().withStartDate(currentTreatmentStartDate).withProviderId("p1").build();

        patient.addTreatment(firstTreatment, therapy, firstTreatmentStartDate.toDateTimeAtCurrentTime());
        patient.addTreatment(pastTreatment2, secondTreatmentStartDate.toDateTimeAtCurrentTime());
        patient.addTreatment(currentTreatment, currentTreatmentStartDate.toDateTimeAtCurrentTime());

        TreatmentCard treatmentCard = new TreatmentCard(patient);
        List<TreatmentHistory> treatmentHistories = treatmentCard.getTreatmentHistories();
        List<TreatmentHistory> expectedTreatmentHistories = asList(
                new TreatmentHistory("p1", firstTreatmentStartDate, firstTreatmentEndDate),
                new TreatmentHistory("p2", secondTreatmentStartDate, secondTreatmentEndDate),
                new TreatmentHistory("p1", currentTreatmentStartDate, null));

        assertThat(treatmentHistories, is(expectedTreatmentHistories));
    }

    @Test
    public void shouldReturnEmptyListIfThereIsNoTreatmentHistory() {
        Patient patient = new PatientBuilder().withPatientId("patientId").build();
        LocalDate currentTreatmentStartDate = new LocalDate(2012, 2, 1);
        Therapy therapy = new TherapyBuilder().withStartDate(currentTreatmentStartDate).build();

        Treatment currentTreatment = new TreatmentBuilder().withStartDate(currentTreatmentStartDate).withProviderId("p1").build();

        patient.addTreatment(currentTreatment, therapy, currentTreatmentStartDate.toDateTimeAtCurrentTime());

        assertTrue(new TreatmentCard(patient).getTreatmentHistories().isEmpty());
    }

    @Test
    public void shouldReturnPausedTreatmentPeriods() {
        Patient patient = new PatientBuilder().withPatientId("patientId").build();
        LocalDate firstTreatmentStartDate = new LocalDate(2011, 12, 1);
        Therapy therapy = new TherapyBuilder().withStartDate(firstTreatmentStartDate).build();
        LocalDate firstTreatmentEndDate = new LocalDate(2011, 12, 31);
        Treatment firstTreatment = new TreatmentBuilder().withStartDate(firstTreatmentStartDate).withEndDate(firstTreatmentEndDate).withProviderId("p1").build();

        LocalDate firstTreatmentPause1Date = firstTreatmentStartDate.plusDays(5);
        LocalDate firstTreatmentResume1Date = firstTreatmentStartDate.plusDays(10);
        firstTreatment.pause("testing", firstTreatmentPause1Date.toDateTimeAtCurrentTime());
        firstTreatment.resume("testing", firstTreatmentResume1Date.toDateTimeAtCurrentTime());

        LocalDate firstTreatmentPause2Date = firstTreatmentStartDate.plusDays(15);
        LocalDate firstTreatmentResume2Date = firstTreatmentStartDate.plusDays(20);
        firstTreatment.pause("testing", firstTreatmentPause2Date.toDateTimeAtCurrentTime());
        firstTreatment.resume("testing", firstTreatmentResume2Date.toDateTimeAtCurrentTime());


        LocalDate currentTreatmentStartDate = new LocalDate(2012, 2, 1);
        Treatment currentTreatment = new TreatmentBuilder().withStartDate(currentTreatmentStartDate).withProviderId("p1").build();

        LocalDate currentTreatmentPauseDate = currentTreatmentStartDate.plusDays(5);
        LocalDate currentTreatmentResumeDate = currentTreatmentStartDate.plusDays(10);
        currentTreatment.pause("testing", currentTreatmentPauseDate.toDateTimeAtCurrentTime());
        currentTreatment.resume("testing", currentTreatmentResumeDate.toDateTimeAtCurrentTime());

        patient.addTreatment(firstTreatment, therapy, firstTreatmentStartDate.toDateTimeAtCurrentTime());
        patient.addTreatment(currentTreatment, currentTreatmentStartDate.toDateTimeAtCurrentTime());

        TreatmentCard treatmentCard = new TreatmentCard(patient);
        assertThat(treatmentCard.getTreatmentPausePeriods(), is(asList(
                new TreatmentPausePeriod(firstTreatmentPause1Date, firstTreatmentResume1Date),
                new TreatmentPausePeriod(firstTreatmentPause2Date, firstTreatmentResume2Date),
                new TreatmentPausePeriod(currentTreatmentPauseDate, currentTreatmentResumeDate)
        )));
    }

    @Test
    public void shouldExtendIPSectionTillTodayWhenPatientCompletedIPAndNextPhaseIsUnknown() {
        Patient patient = patient();
        patient.startTherapy(today.minusMonths(5));
        patient.endCurrentPhase(today.minusMonths(4));
        patient.nextPhaseName(Phase.EIP);

        assertEquals(today, new TreatmentCard(patient).ipBoxAdherenceEndDate());
    }

    @Test
    public void shouldReturnTodaysDateIfPatientIsOnEIP() {
        Patient patient = patient();
        patient.startTherapy(today.minusMonths(5));
        patient.endCurrentPhase(today.minusMonths(4));
        patient.nextPhaseName(Phase.EIP);
        patient.startNextPhase();

        assertEquals(today, new TreatmentCard(patient).ipBoxAdherenceEndDate());
    }

    @Test
    public void shouldExtendEIPSectionTillTodayWhenEIPHasCompletedAndNextPhaseIsUnknown() {
        Patient patient = patient();
        patient.startTherapy(today.minusMonths(5));
        patient.endCurrentPhase(today.minusMonths(4));
        patient.nextPhaseName(Phase.EIP);
        patient.startNextPhase();
        patient.endCurrentPhase(today.minusMonths(3));

        assertEquals(today, new TreatmentCard(patient).ipBoxAdherenceEndDate());
    }

    @Test
    public void shouldReturnTodaysDateIfPatientIsStillOnCP() {
        Patient patient = patient();
        patient.startTherapy(today.minusMonths(5));
        patient.endCurrentPhase(today.minusMonths(4));
        patient.nextPhaseName(Phase.CP);
        patient.startNextPhase();

        assertEquals(today, new TreatmentCard(patient).cpBoxAdherenceEndDate());
    }

    @Test
    public void shouldShowDosesTakenAfterCompletionOfCP() {
        Patient patient = patient();
        patient.startTherapy(today.minusMonths(5));
        patient.endCurrentPhase(today.minusMonths(4));
        patient.nextPhaseName(Phase.CP);
        patient.startNextPhase();
        patient.endCurrentPhase(today.minusMonths(2));

        assertEquals(today, new TreatmentCard(patient).cpBoxAdherenceEndDate());
    }

    @Test
    public void ipBoxLastDateShouldBe5MonthsFromStartDate() {
        Patient patient = patient();
        patient.startTherapy(today.minusMonths(5));

        assertEquals(today.minusDays(1), new TreatmentCard(patient).ipBoxLastDoseDate());
    }

    @Test
    public void cpBoxLastDateShouldBe7MonthsFromStartDate() {
        Patient patient = patient();
        patient.startTherapy(newDate(2012, 1, 1));
        patient.endCurrentPhase(newDate(2012, 2, 1));
        patient.nextPhaseName(Phase.CP);
        patient.startNextPhase();
        patient.endCurrentPhase(newDate(2012, 4, 1));

        assertEquals(newDate(2012, 9, 1), new TreatmentCard(patient).cpBoxLastDoseDate());
    }

    @Test
    public void shouldNotHaveCPAdherenceSectionIfCPStartDateIsNull() {
        Patient patient = patient();
        patient.startTherapy(newDate(2012, 1, 1));
        patient.endCurrentPhase(newDate(2012, 2, 1));
        patient.nextPhaseName(Phase.CP);

        assertFalse(new TreatmentCard(patient).isCPAdherenceSectionValid());
    }

    @Test
    public void shouldHaveCPAdherenceSectionIfCPStartDateIsInThePast() {
        Patient patient = patient();
        LocalDate fiveDaysAgo = today.minusDays(5);

        patient.startTherapy(fiveDaysAgo);
        patient.endCurrentPhase(fiveDaysAgo.plusDays(1));
        patient.nextPhaseName(Phase.CP);
        patient.startNextPhase();

        assertTrue(new TreatmentCard(patient).isCPAdherenceSectionValid());
    }

    @Test
    public void shouldHaveCPAdherenceSectionIfCPStartDateIsInTheFuture() {
        Patient patient = patient();
        LocalDate fiveDaysFromNow = today.plusDays(5);

        patient.startTherapy(fiveDaysFromNow);
        patient.endCurrentPhase(fiveDaysFromNow.plusDays(1));
        patient.nextPhaseName(Phase.CP);
        patient.startNextPhase();

        assertTrue(new TreatmentCard(patient).isCPAdherenceSectionValid());
    }

    @Test
    public void shouldHaveCPAdherenceSectionIfCPStartDateIsExactlyToday() {
        Patient patient = patient();
        LocalDate yesterday = today.minusDays(1);

        patient.startTherapy(yesterday);
        patient.endCurrentPhase(yesterday);
        patient.nextPhaseName(Phase.CP);
        patient.startNextPhase();

        assertTrue(new TreatmentCard(patient).isCPAdherenceSectionValid());
    }

    @Test
    public void shouldNotHaveIpAdherenceSectionIfStartDateOfIPIsNull() {
        Patient patient = patient();
        assertFalse(new TreatmentCard(patient).isIPAdherenceSectionValid());
    }

    @Test
    public void shouldNotHaveIpAdherenceSectionIfStartDateOfIPIsInThePast() {
        Patient patient = patient();
        patient.startTherapy(today.minusDays(1));
        assertTrue(new TreatmentCard(patient).isIPAdherenceSectionValid());
    }

    @Test
    public void shouldNotHaveIpAdherenceSectionIfStartDateOfIPIsNow() {
        Patient patient = patient();
        patient.startTherapy(today);
        assertTrue(new TreatmentCard(patient).isIPAdherenceSectionValid());
    }

    @Test
    public void shouldNotHaveIpAdherenceSectionIfStartDateOfIPIsInTheFuture() {
        Patient patient = patient();
        patient.startTherapy(today.plusDays(1));
        assertTrue(new TreatmentCard(patient).isIPAdherenceSectionValid());
    }

    @Test
    public void shouldIgnoreAdherenceDataAfterEndDate() {
        Patient patient = patient();
        LocalDate yesterday = today.minusDays(1);

        patient.startTherapy(yesterday);
        patient.endCurrentPhase(yesterday);
        patient.nextPhaseName(Phase.CP);
        patient.startNextPhase();

        TreatmentCard treatmentCard = new TreatmentCard(patient);
        List<Adherence> adherenceExtendingBeyondCP = new AdherenceDataBuilder()
                .forPatient(patient)
                .from(yesterday.plusDays(1))
                .till(yesterday.plusMonths(8))
                .build();

        treatmentCard.initCPSection(adherenceExtendingBeyondCP);
        assertTrue(treatmentCard.isCPAdherenceSectionValid());
    }
}
