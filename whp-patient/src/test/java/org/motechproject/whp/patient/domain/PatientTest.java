package org.motechproject.whp.patient.domain;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.builder.TreatmentBuilder;
import org.motechproject.whp.refdata.domain.Gender;
import org.motechproject.whp.refdata.domain.TreatmentOutcome;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.motechproject.util.DateUtil.now;
import static org.motechproject.whp.patient.builder.PatientBuilder.patient;
import static org.motechproject.whp.patient.builder.TreatmentBuilder.treatment;

public class PatientTest {

    @Test
    public void shouldSetTreatmentAddedAsCurrentTreatment() {
        Patient patient = patient();
        Treatment treatment = treatment();
        patient.addTreatment(treatment, now());
        assertEquals(treatment, patient.getCurrentTreatment());
    }

    @Test
    public void shouldUpdateTreatmentHistoryWhenNewTreatmentIdAdded() {
        Patient patient = patient();
        Treatment firstTreatment = patient.getCurrentTreatment();
        patient.addTreatment(treatment(), now());

        assertArrayEquals(new Object[]{firstTreatment}, patient.getTreatments().toArray());
    }

    @Test
    public void shouldNotHaveAnyHistoryWhenTreatmentHasNeverBeenUpdated() {
        Patient patientWithOneTreatment = patient();
        assertTrue(patientWithOneTreatment.getTreatments().isEmpty());
    }

    @Test
    public void shouldAddCurrentTreatmentToHistoryWhenNewTreatmentIsAdded() {
        Patient patient = patient();
        Treatment treatment = patient.getCurrentTreatment();
        patient.addTreatment(treatment(), now());

        assertArrayEquals(new Object[]{treatment}, patient.getTreatments().toArray());
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
        String therapyDocId = "therapyDocId";
        patient.getCurrentTreatment().setTherapyDocId(therapyDocId);
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

        patient.nextPhaseName(PhaseName.EIP);

        assertEquals(patient.currentTherapy().getNextPhaseName(), PhaseName.EIP);
    }

    @Test
    public void shouldEndCurrentPhaseIfNotNull() {
        Patient patient = new PatientBuilder().withDefaults().build();
        LocalDate phaseEndDate = new LocalDate(2012, 4, 1);
        patient.startTherapy(new LocalDate(2012, 3, 1));

        patient.endCurrentPhase(phaseEndDate);

        assertEquals(phaseEndDate, patient.currentTherapy().getPhase(PhaseName.IP).getEndDate());
    }

    @Test
    public void startNextPhaseShouldSetStartDateOnNextPhaseAsNextCalendarDateOfEndDateOfLastCompletedPhase() {
        Patient patient = new PatientBuilder().withDefaults().build();
        LocalDate phaseEndDate = new LocalDate(2012, 4, 1);
        patient.startTherapy(new LocalDate(2012, 3, 1));
        patient.nextPhaseName(PhaseName.EIP);

        patient.endCurrentPhase(phaseEndDate);
        patient.startNextPhase();

        assertEquals(phaseEndDate.plusDays(1), patient.currentTherapy().getPhase(PhaseName.EIP).getStartDate());
        assertNull(patient.currentTherapy().getNextPhaseName());
    }

    @Test
    public void isTransitioningShouldReturnTrueIfCurrentPhaseIsNull() {
        Patient patient = new PatientBuilder().withDefaults().build();
        patient.startTherapy(new LocalDate(2012, 3, 1));
        patient.endCurrentPhase(new LocalDate(2012, 4, 1));

        assertTrue(patient.isTransitioning());
    }

    @Test
    public void hasPhaseToTransitionToShouldReturnTrueIfNextPhaseNameIsNotNull() {
        Patient patient = new PatientBuilder().withDefaults().build();
        patient.nextPhaseName(PhaseName.EIP);

        assertTrue(patient.hasPhaseToTransitionTo());
    }

    @Test
    public void shouldGetCurrentTreatment_WhenDateIsTheStartDayOfCurrentTreatment() {
        Patient patient = new PatientBuilder().withDefaults().withCurrentTreatmentStartDate(date(2011, 10, 1)).build();
        patient.closeCurrentTreatment(TreatmentOutcome.Defaulted, dateTime(2011, 12, 1));

        patient.addTreatment(new TreatmentBuilder().withDefaults().build(), dateTime(2012, 1, 1));
        patient.closeCurrentTreatment(TreatmentOutcome.Defaulted, dateTime(2012, 3, 15));

        Treatment currentTreatment = new TreatmentBuilder().withDefaults().withTbId("current").build();
        patient.addTreatment(currentTreatment, dateTime(2012, 4, 1));

        assertEquals(currentTreatment, patient.getTreatment(date(2012, 4, 1)));
    }

    @Test
    public void shouldGetCurrentTreatment_WhenDateIsAfterStartDayOfCurrentTreatment() {
        Patient patient = new PatientBuilder().withDefaults().withCurrentTreatmentStartDate(date(2011, 10, 1)).build();
        patient.closeCurrentTreatment(TreatmentOutcome.Defaulted, dateTime(2011, 12, 1));

        patient.addTreatment(new TreatmentBuilder().withDefaults().build(), dateTime(2012, 1, 1));
        patient.closeCurrentTreatment(TreatmentOutcome.Defaulted, dateTime(2012, 3, 15));

        Treatment currentTreatment = new TreatmentBuilder().withDefaults().withTbId("current").build();
        patient.addTreatment(currentTreatment, dateTime(2012, 4, 1));

        assertEquals(currentTreatment, patient.getTreatment(date(2012, 4, 2)));
    }

    @Test
    public void shouldGetFirstTreatment_WhenDateIsSameAsEndDate() {
        Patient patient = new PatientBuilder().withDefaults().withCurrentTreatmentStartDate(date(2011, 10, 1)).build();
        Treatment firstTreatment = patient.getCurrentTreatment();
        patient.closeCurrentTreatment(TreatmentOutcome.Defaulted, dateTime(2011, 12, 1));

        patient.addTreatment(new TreatmentBuilder().withDefaults().build(), dateTime(2012, 1, 1));
        patient.closeCurrentTreatment(TreatmentOutcome.Defaulted, dateTime(2012, 3, 15));

        patient.addTreatment(new TreatmentBuilder().withDefaults().withTbId("current").build(), dateTime(2012, 4, 1));

        assertEquals(firstTreatment, patient.getTreatment(date(2011, 12, 1)));
    }

    @Test
    public void shouldGetLatestTreatment_WhenTreatmentIsCloseAndOpenedOnSameDay() {
        Patient patient = new PatientBuilder().withDefaults().withCurrentTreatmentStartDate(date(2011, 10, 1)).build();
        patient.closeCurrentTreatment(TreatmentOutcome.Defaulted, dateTime(2011, 12, 1));

        Treatment secondTreatment = new TreatmentBuilder().withDefaults().build();
        patient.addTreatment(secondTreatment, dateTime(2011, 12, 1));
        patient.closeCurrentTreatment(TreatmentOutcome.Defaulted, dateTime(2012, 3, 15));

        patient.addTreatment(new TreatmentBuilder().withDefaults().withTbId("current").build(), dateTime(2012, 4, 1));

        assertEquals(secondTreatment, patient.getTreatment(date(2011, 12, 1)));
    }

    private LocalDate date(int year, int monthOfYear, int dayOfMonth) {
        return new LocalDate(year, monthOfYear, dayOfMonth);
    }

    private DateTime dateTime(int year, int monthOfYear, int dayOfMonth) {
        return new LocalDate(year, monthOfYear, dayOfMonth).toDateTimeAtCurrentTime();
    }

}
