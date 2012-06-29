package org.motechproject.whp.patient.domain;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.refdata.domain.PhaseName;

import java.util.Arrays;

import static junit.framework.Assert.*;
import static org.motechproject.util.DateUtil.today;

public class PhasesTest {

    private Phases phases;

    @Before
    public void setUp() {
        phases = new Phases(Arrays.asList(new Phase(PhaseName.IP), new Phase(PhaseName.CP), new Phase(PhaseName.EIP)));
    }

    @Test
    public void shouldReturnPhaseByName() {
        assertEquals(PhaseName.IP, phases.getByPhaseName(PhaseName.IP).getName());
    }

    @Test
    public void settingEIPStartDateShouldSetEndDateOnIP() {
        phases.setEIPStartDate(today());

        assertEquals(today().minusDays(1), phases.getByPhaseName(PhaseName.IP).getEndDate());
    }

    @Test
    public void settingCPStartDateShouldSetEndDateOnEIPOnlyIfEIPWasStarted() {
        phases.setEIPStartDate(today().minusDays(2));
        phases.setCPStartDate(today());

        assertEquals(today().minusDays(1), phases.getByPhaseName(PhaseName.EIP).getEndDate());
        assertNotSame(today().minusDays(1), phases.getByPhaseName(PhaseName.IP).getEndDate());
    }

    @Test
    public void settingCPStartDateShouldSetEndDateOnIPIfEIPWasNeverStarted() {
        phases.setCPStartDate(today());

        assertEquals(today().minusDays(1), phases.getByPhaseName(PhaseName.IP).getEndDate());
    }

    @Test
    public void shouldSetPreviousPhaseEndDateToNullIfCurrentPhaseStartDateIsBeingSetToNull() {
        phases.setEIPStartDate(today().minusDays(2));
        phases.setEIPEndDate(today().minusDays(1));
        phases.setCPStartDate(null);

        assertNull(phases.getByPhaseName(PhaseName.EIP).getEndDate());
    }

    @Test
    public void shouldGetCurrentPhase() {
        phases.setIPStartDate(today());
        phases.setEIPStartDate(today().plusDays(10));

        assertEquals(phases.getByPhaseName(PhaseName.EIP), phases.getCurrentPhase());
    }

    @Test
    public void shouldGetLastCompletedPhase() {
        phases.setIPStartDate(today());
        phases.setIPEndDate(today().plusDays(2));
        phases.setEIPStartDate(today().plusDays(3));
        phases.setEIPEndDate(today().plusDays(5));
        phases.setCPStartDate(today().plusDays(6));

        assertEquals(phases.getByPhaseName(PhaseName.EIP), phases.getLastCompletedPhase());
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

}
