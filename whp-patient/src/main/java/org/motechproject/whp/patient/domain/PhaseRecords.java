package org.motechproject.whp.patient.domain;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.joda.time.LocalDate;
import org.motechproject.whp.refdata.domain.Phase;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class PhaseRecords implements Serializable {

    @JsonProperty
    private Map<String, PhaseRecord> phaseRecords = new HashMap<>();

    public PhaseRecords() {
        phaseRecords.put(Phase.IP.toString(), new PhaseRecord(Phase.IP));
        phaseRecords.put(Phase.EIP.toString(), new PhaseRecord(Phase.EIP));
        phaseRecords.put(Phase.CP.toString(), new PhaseRecord(Phase.CP));
    }

    @JsonIgnore
    public PhaseRecord get(Phase phase) {
        return phaseRecords.get(phase.toString());
    }

    public void recordTransition(PhaseTransition transition, LocalDate transitionDate) {
        Phase phase = transition.getPhase();
        phaseRecords.get(phase.toString()).setStartDate(transitionDate);
        splitPhases(transition);
    }

    public void removeTransition(PhaseTransition transition) {
        Phase phase = transition.getPhase();
        joinPhases(transition);
        phaseRecords.put(phase.toString(), new PhaseRecord(phase));
    }

    private void joinPhases(PhaseTransition transition) {
        if (transition.isProperTransition()) {
            PhaseRecord record = phaseRecords.get(transition.getTransitionedFrom().toString());
            PhaseRecord nextRecord = phaseRecords.get(transition.getTransitionedTo().toString());
            record.setEndDate(nextRecord.getStartDate().minusDays(1));
        } else if (transition.isTransitionToNull()) {
            PhaseRecord record = phaseRecords.get(transition.getTransitionedFrom().toString());
            record.setEndDate(null);
        } else if (transition.isTransitionFromNull()) {
            PhaseRecord currentRecord = phaseRecords.get(transition.getPhase().toString());
            phaseRecords.get(transition.getTransitionedTo().toString()).setStartDate(currentRecord.getStartDate());
        }
    }

    private void splitPhases(PhaseTransition transition) {
        if (transition.getTransitionedFrom() != null) {
            PhaseRecord phaseRecord = phaseRecords.get(transition.getPhase().toString());
            phaseRecords.get(transition.getTransitionedFrom().toString()).setEndDate(phaseRecord.getStartDate().minusDays(1));
        }
        if (transition.getTransitionedTo() != null) {
            PhaseRecord nextPhaseRecord = phaseRecords.get(transition.getTransitionedTo().toString());
            phaseRecords.get(transition.getPhase().toString()).setEndDate(nextPhaseRecord.getStartDate().minusDays(1));
        }
    }
}
