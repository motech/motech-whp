package org.motechproject.whp.patient.domain;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.motechproject.whp.refdata.domain.Phase;
import org.springframework.util.Assert;

import java.io.Serializable;

@Data
public class PhaseTransition implements Serializable {

    private Phase phase;

    private Phase transitionedFrom;

    private Phase transitionedTo;

    public PhaseTransition() {
    }

    public PhaseTransition(Phase from, Phase phase, Phase to) {
        Assert.notNull(phase);
        this.transitionedFrom = from;
        this.phase = phase;
        this.transitionedTo = to;
    }

    @JsonIgnore
    public boolean isNullTransition() {
        return transitionedFrom == null && transitionedTo == null;
    }

    @JsonIgnore
    public boolean isTransitionToNull() {
        return transitionedFrom != null && transitionedTo == null;
    }

    @JsonIgnore
    public boolean isTransitionFromNull() {
        return transitionedFrom == null && transitionedTo != null;
    }

    @JsonIgnore
    public boolean isProperTransition() {
        return transitionedFrom != null && transitionedTo != null;
    }
}
