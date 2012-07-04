package org.motechproject.whp.treatmentcard.domain;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.refdata.domain.PhaseName;

import static junit.framework.Assert.*;
import static org.motechproject.util.DateUtil.newDate;
import static org.motechproject.util.DateUtil.today;
import static org.motechproject.whp.patient.builder.PatientBuilder.patient;

public class TreatmentCardTest {

    @Test
    public void shouldReturnTodaysDateIfPatientIsOnIP() {
        Patient patient = patient();
        patient.startTherapy(today().minusMonths(5));

        assertEquals(today(), new TreatmentCard(patient).ipBoxAdherenceEndDate());
    }

    @Test
    public void shouldReturnIPEndDateIfPatientHasCompletedIPAndNextPhaseIsUnknown() {
        Patient patient = patient();
        patient.startTherapy(today().minusMonths(5));
        patient.endCurrentPhase(today().minusMonths(4));

        assertEquals(today().minusMonths(4), new TreatmentCard(patient).ipBoxAdherenceEndDate());
    }

    @Test
    public void shouldReturnIPEndDateIfPatientHasCompletedIPAndNextPhaseIsKnown() {
        Patient patient = patient();
        patient.startTherapy(today().minusMonths(5));
        patient.endCurrentPhase(today().minusMonths(4));
        patient.nextPhaseName(PhaseName.EIP);

        assertEquals(today().minusMonths(4), new TreatmentCard(patient).ipBoxAdherenceEndDate());
    }

    @Test
    public void shouldReturnTodaysDateIfPatientIsOnEIP() {
        Patient patient = patient();
        patient.startTherapy(today().minusMonths(5));
        patient.endCurrentPhase(today().minusMonths(4));
        patient.nextPhaseName(PhaseName.EIP);
        patient.startNextPhase();

        assertEquals(today(), new TreatmentCard(patient).ipBoxAdherenceEndDate());
    }

    @Test
    public void shouldReturnEIPEnDateIfPatientHasCompletedEIP() {
        Patient patient = patient();
        patient.startTherapy(today().minusMonths(5));
        patient.endCurrentPhase(today().minusMonths(4));
        patient.nextPhaseName(PhaseName.EIP);
        patient.startNextPhase();
        patient.endCurrentPhase(today().minusMonths(3));

        assertEquals(today().minusMonths(3), new TreatmentCard(patient).ipBoxAdherenceEndDate());
    }

    @Test
    public void shouldReturnTodaysDateIfPatientIsStillOnCP() {
        Patient patient = patient();
        patient.startTherapy(today().minusMonths(5));
        patient.endCurrentPhase(today().minusMonths(4));
        patient.nextPhaseName(PhaseName.CP);
        patient.startNextPhase();

        assertEquals(today(), new TreatmentCard(patient).cpBoxAdherenceEndDate());
    }

    @Test
    public void shouldReturnCPEndDateIfPatientHasCompletedCP() {
        Patient patient = patient();
        patient.startTherapy(today().minusMonths(5));
        patient.endCurrentPhase(today().minusMonths(4));
        patient.nextPhaseName(PhaseName.CP);
        patient.startNextPhase();
        patient.endCurrentPhase(today().minusMonths(2));

        assertEquals(today().minusMonths(2), new TreatmentCard(patient).cpBoxAdherenceEndDate());
    }

    @Test
    public void ipBoxLastDateShouldBe5MonthsFromStartDate() {
        Patient patient = patient();
        patient.startTherapy(today().minusMonths(5));

        assertEquals(today().minusDays(1), new TreatmentCard(patient).ipBoxLastDoseDate());
    }

    @Test
    public void cpBoxLastDateShouldBe7MonthsFromStartDate() {
        Patient patient = patient();
        patient.startTherapy(newDate(2012, 1, 1));
        patient.endCurrentPhase(newDate(2012, 2, 1));
        patient.nextPhaseName(PhaseName.CP);
        patient.startNextPhase();
        patient.endCurrentPhase(newDate(2012, 4, 1));

        assertEquals(newDate(2012, 9, 1), new TreatmentCard(patient).cpBoxLastDoseDate());
    }

    @Test
    public void shouldNotHaveCPAdherenceSectionIfCPStartDateIsNull() {
        Patient patient = patient();
        patient.startTherapy(newDate(2012, 1, 1));
        patient.endCurrentPhase(newDate(2012, 2, 1));
        patient.nextPhaseName(PhaseName.CP);

        assertFalse(new TreatmentCard(patient).isCPAdherenceSectionValid());
    }

    @Test
    public void shouldHaveCPAdherenceSectionIfCPStartDateIsInThePast() {
        Patient patient = patient();
        LocalDate fiveDaysAgo = today().minusDays(5);

        patient.startTherapy(fiveDaysAgo);
        patient.endCurrentPhase(fiveDaysAgo.plusDays(1));
        patient.nextPhaseName(PhaseName.CP);
        patient.startNextPhase();

        assertTrue(new TreatmentCard(patient).isCPAdherenceSectionValid());
    }

    @Test
    public void shouldHaveCPAdherenceSectionIfCPStartDateIsInTheFuture() {
        Patient patient = patient();
        LocalDate fiveDaysFromNow = today().plusDays(5);

        patient.startTherapy(fiveDaysFromNow);
        patient.endCurrentPhase(fiveDaysFromNow.plusDays(1));
        patient.nextPhaseName(PhaseName.CP);
        patient.startNextPhase();

        assertTrue(new TreatmentCard(patient).isCPAdherenceSectionValid());
    }

    @Test
    public void shouldHaveCPAdherenceSectionIfCPStartDateIsExactlyToday() {
        Patient patient = patient();
        LocalDate yesterday = today().minusDays(1);

        patient.startTherapy(yesterday);
        patient.endCurrentPhase(yesterday);
        patient.nextPhaseName(PhaseName.CP);
        patient.startNextPhase();

        assertTrue(new TreatmentCard(patient).isCPAdherenceSectionValid());
    }
}
