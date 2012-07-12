package org.motechproject.whp.patient.domain;

import lombok.Getter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.motechproject.whp.common.collections.UniqueElementList;
import org.motechproject.whp.refdata.domain.Phase;

import java.util.ArrayList;
import java.util.List;

public class PhaseTransitions {

    @Getter
    private List<Phase> transitions = new UniqueElementList<>();

    public PhaseTransition add(Phase phase) {
        boolean inserted = insert(phase);
        if (!inserted) {
            transitions.add(phase);
        }
        return new PhaseTransition(transitionedFrom(phase), phase, transitionedTo(phase));
    }

    @JsonIgnore
    public Phase get(int index) {
        return transitions.get(index);
    }

    public boolean contains(Phase phase) {
        return transitions.contains(phase);
    }

    @JsonIgnore
    public boolean isEmpty() {
        return transitions.isEmpty();
    }

    public PhaseTransition remove(Phase phase) {
        PhaseTransition transition = new PhaseTransition(transitionedFrom(phase), phase, transitionedTo(phase));
        transitions.remove(phase);
        return transition;
    }

    public Phase latestPhase() {
        if (!transitions.isEmpty()) {
            return transitions.get(transitions.size() - 1);
        }
        return null;
    }

    @JsonIgnore
    public Phase getNextPhase(Phase phase) {
        if (transitions.contains(phase) && phase != latestPhase()) {
            return transitions.get(transitions.indexOf(phase) + 1);
        } else {
            return null;
        }
    }

    public List<Phase> allPhases() {
        return new ArrayList<>(transitions);
    }

    //so that ektorp does not deserialize into ArrayList instead of UniqueElementList.
    public void setTransitions(List<Phase> transitions){
        this.transitions = new UniqueElementList<>(transitions);
    }

    private boolean insert(Phase phase) {
        boolean inserted = false;
        for (int i = transitions.size() - 1; i >= 0; i--) {
            Phase transition = transitions.get(i);
            if (phase.occursBefore(transition)) {
                transitions.add(i, phase);
                inserted = true;
            }
        }
        return inserted;
    }

    private Phase transitionedFrom(Phase phase) {
        if (!transitions.contains(phase) || transitions.indexOf(phase) == 0) {
            return null;
        } else {
            return transitions.get(transitions.indexOf(phase) - 1);
        }
    }

    private Phase transitionedTo(Phase phase) {
        if (!transitions.contains(phase) || transitions.indexOf(phase) == transitions.size() - 1) {
            return null;
        } else {
            return transitions.get(transitions.indexOf(phase) + 1);
        }
    }
}
