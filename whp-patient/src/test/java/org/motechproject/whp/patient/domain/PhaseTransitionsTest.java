package org.motechproject.whp.patient.domain;

import org.junit.Test;
import org.motechproject.whp.common.domain.Phase;

import static junit.framework.Assert.*;

public class PhaseTransitionsTest {

    @Test
    public void shouldRecordTransitionToPhase() {
        PhaseTransitions transitions = new PhaseTransitions();
        assertTrue(transitions.isEmpty());
        transitions.add(Phase.IP);
        assertFalse(transitions.isEmpty());
    }

    @Test
    public void shouldRecordTransitionFromOnePhaseToNext() {
        PhaseTransitions transitions = new PhaseTransitions();
        transitions.add(Phase.IP);
        PhaseTransition transition = transitions.add(Phase.EIP);
        assertEquals(new PhaseTransition(Phase.IP, Phase.EIP, null), transition);
    }

    @Test
    public void shouldInsertPhaseTransitionBetweenTwoPhases() {
        PhaseTransitions transitions = new PhaseTransitions();
        transitions.add(Phase.IP);
        transitions.add(Phase.CP);
        PhaseTransition transition = transitions.add(Phase.EIP);
        assertEquals(new PhaseTransition(Phase.IP, Phase.EIP, Phase.CP), transition);
    }

    @Test
    public void shouldInsertPhaseTransitionBeforeTheFirstRecordedPhase() {
        PhaseTransitions transitions = new PhaseTransitions();
        transitions.add(Phase.EIP);
        PhaseTransition transition = transitions.add(Phase.IP);
        assertEquals(new PhaseTransition(null, Phase.IP, Phase.EIP), transition);
    }

    @Test
    public void shouldNotRecordDuplicateTransitionToPhase() {
        PhaseTransitions transitions = new PhaseTransitions();
        transitions.add(Phase.IP);
        assertEquals(new PhaseTransition(null, Phase.IP, null), transitions.add(Phase.IP));
    }

    @Test
    public void shouldReturnTheLatestPhaseRecorded() {
        PhaseTransitions transitions = new PhaseTransitions();
        transitions.add(Phase.IP);
        assertEquals(Phase.IP, transitions.latestPhase());
    }

    @Test
    public void shouldReturnLatestPhaseWhenTransitionsWereRecordedOutOfPhaseOrder() {
        PhaseTransitions transitions = new PhaseTransitions();
        transitions.add(Phase.EIP);
        transitions.add(Phase.IP);
        assertEquals(Phase.EIP, transitions.latestPhase());
    }

    @Test
    public void shouldRemoveTransitionToPhase() {
        PhaseTransitions transitions = new PhaseTransitions();
        transitions.add(Phase.IP);
        assertEquals(new PhaseTransition(null, Phase.IP, null), transitions.remove(Phase.IP));
    }

    @Test
    public void shouldRemoveTransitionToTheLatestPhase() {
        PhaseTransitions transitions = new PhaseTransitions();
        transitions.add(Phase.IP);
        transitions.add(Phase.CP);
        assertEquals(new PhaseTransition(Phase.IP, Phase.CP, null), transitions.remove(Phase.CP));
    }

    @Test
    public void shouldRemoveTransitionToPhaseBetweenTwoOtherPhases() {
        PhaseTransitions transitions = new PhaseTransitions();
        transitions.add(Phase.IP);
        transitions.add(Phase.EIP);
        transitions.add(Phase.CP);
        assertEquals(new PhaseTransition(Phase.IP, Phase.EIP, Phase.CP), transitions.remove(Phase.EIP));
    }

    @Test
    public void shouldRemoveTransitionToTheStartingPhase() {
        PhaseTransitions transitions = new PhaseTransitions();
        transitions.add(Phase.IP);
        transitions.add(Phase.EIP);
        assertEquals(new PhaseTransition(null, Phase.IP, Phase.EIP), transitions.remove(Phase.IP));
    }

    @Test
    public void shouldNotBeAffectedByChangesToAllPhasesExposed() {
        PhaseTransitions transitions = new PhaseTransitions();
        transitions.allPhases().add(Phase.IP);
        assertTrue(transitions.isEmpty());
    }

    @Test
    public void shouldBeEmptyWhenThereWereNoTransitions() {
        PhaseTransitions transitions = new PhaseTransitions();
        assertTrue(transitions.isEmpty());
    }

    @Test
    public void shouldContainPhaseThatHasBeenTransitionedTo() {
        PhaseTransitions transitions = new PhaseTransitions();
        assertFalse(transitions.contains(Phase.IP));
        transitions.add(Phase.IP);
        assertTrue(transitions.contains(Phase.IP));
    }

}
