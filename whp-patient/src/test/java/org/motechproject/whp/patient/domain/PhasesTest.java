package org.motechproject.whp.patient.domain;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.refdata.domain.Phase;

import java.util.Arrays;

import static junit.framework.Assert.*;
import static junit.framework.Assert.assertEquals;
import static org.motechproject.util.DateUtil.today;

public class PhasesTest {

    private Phases phases;

    @Before
    public void setUp() {
        phases = new Phases(Arrays.asList(new PhaseRecord(Phase.IP), new PhaseRecord(Phase.CP), new PhaseRecord(Phase.EIP)));
    }

    @Test
    public void shouldReturnPhaseByName() {
        assertEquals(Phase.IP, phases.getByPhaseName(Phase.IP).getName());
    }

    @Test
    public void settingEIPStartDateShouldSetEndDateOnIP() {
        phases.setEIPStartDate(today());

        assertEquals(today().minusDays(1), phases.getByPhaseName(Phase.IP).getEndDate());
    }

    @Test
    public void settingEIPStartDateToNullShouldSetEndDateOnIPToNull_WhenCPHasNotStarted() {
        phases.setIPStartDate(today());
        phases.setEIPStartDate(today().plusDays(2));

        assertEquals(today().plusDays(1), phases.getByPhaseName(Phase.IP).getEndDate());

        phases.setEIPStartDate(null);

        assertEquals(null, phases.getByPhaseName(Phase.IP).getEndDate());
    }

    @Test
    public void settingEIPStartDateToNullShouldSetEndDateOnIPToOneDayBeforeCPStartDateTo_WhenCPHasStarted() {
        phases.setIPStartDate(today());
        phases.setEIPStartDate(today().plusDays(2));
        phases.setCPStartDate(today().plusDays(4));

        assertEquals(today().plusDays(1), phases.getByPhaseName(Phase.IP).getEndDate());

        phases.setEIPStartDate(null);

        assertNotNull(null, phases.getByPhaseName(Phase.IP).getEndDate());
        assertEquals(today().plusDays(3), phases.getByPhaseName(Phase.IP).getEndDate());
    }

    @Test
    public void settingCPStartDateShouldSetEndDateOnEIPOnlyIfEIPWasStarted() {
        phases.setEIPStartDate(today().minusDays(2));
        phases.setCPStartDate(today());

        assertEquals(today().minusDays(1), phases.getByPhaseName(Phase.EIP).getEndDate());
        assertNotSame(today().minusDays(1), phases.getByPhaseName(Phase.IP).getEndDate());
    }

    @Test
    public void settingCPStartDateShouldSetEndDateOnIPIfEIPWasNeverStarted() {
        phases.setCPStartDate(today());

        assertEquals(today().minusDays(1), phases.getByPhaseName(Phase.IP).getEndDate());
    }

    @Test
    public void shouldSetPreviousPhaseEndDateToNullIfCurrentPhaseStartDateIsBeingSetToNull() {
        phases.setEIPStartDate(today().minusDays(2));
        phases.setEIPEndDate(today().minusDays(1));
        phases.setCPStartDate(null);

        assertNull(phases.getByPhaseName(Phase.EIP).getEndDate());
    }

    @Test
    public void shouldGetCurrentPhase() {
        phases.setIPStartDate(today());
        phases.setEIPStartDate(today().plusDays(10));

        assertEquals(phases.getByPhaseName(Phase.EIP), phases.getCurrentPhase());
    }

    @Test
    public void shouldGetLastCompletedPhase() {
        phases.setIPStartDate(today());
        phases.setIPEndDate(today().plusDays(2));
        phases.setEIPStartDate(today().plusDays(3));
        phases.setEIPEndDate(today().plusDays(5));
        phases.setCPStartDate(today().plusDays(6));

        assertEquals(phases.getByPhaseName(Phase.EIP), phases.getLastCompletedPhase());
    }

    @Test
    public void shouldReturnNullIfNoPhaseHasStarted() {
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
        phases.setEIPEndDate(today().plusDays(10));
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
        phases.setIPEndDate(today().plusDays(1));
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
}
