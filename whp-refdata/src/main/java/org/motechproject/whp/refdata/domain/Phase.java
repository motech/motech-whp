package org.motechproject.whp.refdata.domain;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;

public enum Phase {

    CP("Continuation Phase", Collections.<Phase>emptyList()),
    EIP("Extended Intensive Phase", asList(CP)),
    IP("Intensive Phase", asList(EIP, CP));

    private String name;
    private final List<Phase> possibleTransitions;

    Phase(String name, List<Phase> possibleTransitions) {
        this.name = name;
        this.possibleTransitions = possibleTransitions;
    }

    public boolean occursBefore(Phase phase) {
        return possibleTransitions.contains(phase);
    }

    @Override
    public String toString() {
        return name;
    }
}
