package org.motechproject.whp.patient.domain;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.refdata.domain.Phase;

import static junit.framework.Assert.*;
import static org.motechproject.util.DateUtil.today;

public class PhasesTest {

    private Phases phases;
    private LocalDate today = today();

    @Before
    public void setUp() {
        phases = new Phases();
        phases.setIPStartDate(today);
    }

    @Test
    public void settingEIPStartDateShouldSetEndDateOnIP() {
        phases.setEIPStartDate(today());
        assertEquals(today().minusDays(1), phases.getEndDate(Phase.IP));
    }

    @Test
    public void settingEIPStartDateToNullShouldSetEndDateOnIPToNull_WhenCPHasNotStarted() {
        phases.setIPStartDate(today());
        phases.setEIPStartDate(today().plusDays(2));

        assertEquals(today().plusDays(1), phases.getEndDate(Phase.IP));

        phases.setEIPStartDate(null);

        assertEquals(null, phases.getEndDate(Phase.IP));
    }

    @Test
    public void settingEIPStartDateToNullShouldSetEndDateOnIPToOneDayBeforeCPStartDate_WhenCPHasStarted() {
        phases.setIPStartDate(today());
        phases.setEIPStartDate(today().plusDays(2));
        phases.setCPStartDate(today().plusDays(4));

        assertEquals(today().plusDays(1), phases.getEndDate(Phase.IP));

        phases.setEIPStartDate(null);

        assertNotNull(null, phases.getEndDate(Phase.IP));
        assertEquals(today().plusDays(3), phases.getEndDate(Phase.IP));
    }

    @Test
    public void settingCPStartDateShouldSetEndDateOnEIPOnlyIfEIPWasStarted() {
        phases.setEIPStartDate(today().minusDays(2));
        phases.setCPStartDate(today());

        assertEquals(today().minusDays(1), phases.getEndDate(Phase.EIP));
        assertNotSame(today().minusDays(1), phases.getEndDate(Phase.IP));
    }

    @Test
    public void settingCPStartDateShouldSetEndDateOnIPIfEIPWasNeverStarted() {
        phases.setCPStartDate(today());
        assertEquals(today().minusDays(1), phases.getEndDate(Phase.IP));
    }

    @Test
    public void shouldSetPreviousPhaseEndDateToNullIfCurrentPhaseStartDateIsBeingSetToNull() {
        phases.setEIPStartDate(today().minusDays(2));
        phases.setCPStartDate(null);

        assertNull(phases.getEndDate(Phase.EIP));
    }

    @Test
    public void shouldGetCurrentPhase() {
        phases.setIPStartDate(today());
        phases.setEIPStartDate(today().plusDays(10));

        assertEquals(Phase.EIP, phases.getCurrentPhase().getName());
    }

    @Test
    public void shouldGetLastCompletedPhase() {
        phases.setIPStartDate(today());
        phases.setEIPStartDate(today().plusDays(3));
        phases.setCPStartDate(today().plusDays(6));

        assertEquals(Phase.EIP, phases.getLastCompletedPhase().getName());
    }

    @Test
    public void shouldReturnNullIfNoPhaseHasStarted() {
        Phases phases = new Phases();
        assertNull(phases.getCurrentPhase());
    }

    @Test
    public void shouldReturnTrueWhenCurrentPhaseIsEIP() {
        phases.setEIPStartDate(today());
        assertTrue(phases.ipPhaseWasExtended());
    }

    @Test
    public void shouldReturnTrueWhenCurrentPhaseIsCPAndEIPWasExtended() {
        phases.setEIPStartDate(today());
        phases.setCPStartDate(today().plusDays(100));
        assertTrue(phases.ipPhaseWasExtended());
    }

    @Test
    public void shouldReturnFalseWhenCurrentPhaseIsIP() {
        phases.setIPStartDate(today());
        assertFalse(phases.ipPhaseWasExtended());
    }

    @Test
    public void shouldReturnFalseWhenCurrentPhaseIsCPAndIPWasNotExtended() {
        phases.setIPStartDate(today());
        phases.setCPStartDate(today().plusDays(2));
        assertFalse(phases.ipPhaseWasExtended());
    }

    @Test
    public void shouldReturnFalseIfPatientIsOnIP() {
        Patient patient = PatientBuilder.patient();
        patient.startTherapy(today().minusMonths(5));
        assertFalse(patient.getCurrentTherapy().getPhases().isOrHasBeenOnCp());
    }

    @Test
    public void shouldReturnFalseIfPatientHasCompletedIP() {
        Patient patient = PatientBuilder.patient();
        patient.startTherapy(today().minusMonths(5));
        patient.endCurrentPhase(today().minusMonths(4));
        assertFalse(patient.getCurrentTherapy().getPhases().isOrHasBeenOnCp());
    }

    @Test
    public void shouldReturnFalseIfPatientIsOnEIP() {
        Patient patient = PatientBuilder.patient();
        patient.startTherapy(today().minusMonths(5));
        patient.endCurrentPhase(today().minusMonths(4));
        patient.nextPhaseName(Phase.EIP);
        patient.startNextPhase();
        assertFalse(patient.getCurrentTherapy().getPhases().isOrHasBeenOnCp());
    }

    @Test
    public void shouldReturnFalseIfPatientHasCompletedEIP() {
        Patient patient = PatientBuilder.patient();
        patient.startTherapy(today().minusMonths(5));
        patient.endCurrentPhase(today().minusMonths(4));
        patient.nextPhaseName(Phase.EIP);
        patient.startNextPhase();
        patient.endCurrentPhase(today().minusMonths(2));
        assertFalse(patient.getCurrentTherapy().getPhases().isOrHasBeenOnCp());
    }

    @Test
    public void shouldReturnStartDateOfPhaseNextToGivenPhase() {
        Patient patient = PatientBuilder.patient();
        patient.startTherapy(today.minusMonths(5));
        patient.endCurrentPhase(today.minusMonths(4));
        patient.nextPhaseName(Phase.EIP);
        patient.startNextPhase();

        assertEquals(today.minusMonths(4).plusDays(1), patient.getCurrentTherapy().getPhases().getNextPhaseStartDate(Phase.IP));
    }

    @Test
    public void shouldReturnNullAsStartDateOfPhaseNextToLatestPhase() {
        Patient patient = PatientBuilder.patient();
        patient.startTherapy(today.minusMonths(5));
        patient.endCurrentPhase(today.minusMonths(4));
        patient.nextPhaseName(Phase.EIP);
        patient.startNextPhase();

        assertEquals(null, patient.getCurrentTherapy().getPhases().getNextPhaseStartDate(Phase.EIP));
    }

    @Test
    public void shouldReturnNullAsStartDateOfNextPhaseWhenTherapyWasNeverOnGivenPhase() {
        Patient patient = PatientBuilder.patient();
        assertEquals(null, patient.getCurrentTherapy().getPhases().getNextPhaseStartDate(Phase.IP));
    }

    @Test
    public void shouldReturnTrueIfPatientHasStartedCP() {
        Patient patient = PatientBuilder.patient();
        patient.startTherapy(today().minusMonths(5));
        patient.endCurrentPhase(today().minusMonths(4));
        patient.nextPhaseName(Phase.EIP);
        patient.startNextPhase();
        patient.endCurrentPhase(today().minusMonths(2));
        patient.nextPhaseName(Phase.CP);
        patient.startNextPhase();
        assertTrue(patient.getCurrentTherapy().getPhases().isOrHasBeenOnCp());
    }

    @Test
    public void shouldReturnTrueIfPatientHasCompletedCP() {
        Patient patient = PatientBuilder.patient();
        patient.startTherapy(today().minusMonths(5));
        patient.endCurrentPhase(today().minusMonths(4));
        patient.nextPhaseName(Phase.EIP);
        patient.startNextPhase();
        patient.endCurrentPhase(today().minusMonths(2));
        patient.nextPhaseName(Phase.CP);
        patient.startNextPhase();
        patient.endCurrentPhase(today().minusMonths(1));
        assertTrue(patient.getCurrentTherapy().getPhases().isOrHasBeenOnCp());
    }

    @Test
    public void shouldReturnTotalDosesTakenTillLastSunday() {
        Patient patient = PatientBuilder.patient();
        patient.startTherapy(today().minusMonths(5));

        patient.setNumberOfDosesTaken(Phase.IP, 24);
        patient.setNumberOfDosesTakenAsOfLastSunday(Phase.IP, 22);

        patient.endCurrentPhase(today().minusMonths(4));

        patient.nextPhaseName(Phase.EIP);
        patient.startNextPhase();

        patient.setNumberOfDosesTaken(Phase.EIP, 9);
        patient.setNumberOfDosesTakenAsOfLastSunday(Phase.EIP, 8);

        assertEquals(24 + 8, patient.getTotalNumberOfDosesTakenTillLastSunday());
    }
}
