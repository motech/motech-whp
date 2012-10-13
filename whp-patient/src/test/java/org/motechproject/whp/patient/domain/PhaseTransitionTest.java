package org.motechproject.whp.patient.domain;

import org.junit.Test;
import org.motechproject.whp.common.domain.Phase;

import static junit.framework.Assert.assertTrue;

public class PhaseTransitionTest {

    @Test
    public void shouldBeAProperTransitionWhenBothPredecessorAndSuccessorExist() {
        PhaseTransition phaseTransition = new PhaseTransition(Phase.IP, Phase.EIP, Phase.CP);
        assertTrue(phaseTransition.isProperTransition());
    }

    @Test
    public void shouldBeATransitionToNullWhenNoSuccessorExists() {
        PhaseTransition phaseTransition = new PhaseTransition(Phase.IP, Phase.EIP, null);
        assertTrue(phaseTransition.isTransitionToNull());
    }

    @Test
    public void shouldBeATransitionFromNullWhenNoPredecessorExists() {
        PhaseTransition phaseTransition = new PhaseTransition(null, Phase.EIP, Phase.CP);
        assertTrue(phaseTransition.isTransitionFromNull());
    }

    @Test
    public void shouldBeANullTransitionWhenNoPredecessorOrSuccessorExist() {
        PhaseTransition phaseTransition = new PhaseTransition(null, Phase.EIP, null);
        assertTrue(phaseTransition.isNullTransition());
    }

    @Test(expected = RuntimeException.class)
    public void shouldNotBeValidIfPhaseIsNull() {
        new PhaseTransition(null, null, null);
    }
}
