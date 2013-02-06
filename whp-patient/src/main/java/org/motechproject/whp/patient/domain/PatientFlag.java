package org.motechproject.whp.patient.domain;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class PatientFlag {
    @JsonProperty
    private boolean value;

    @JsonIgnore
    public boolean isFlagSet() {
        return value == true;
    }

    public void setFlagValue(boolean flagValue) {
        value = flagValue;
    }
}
