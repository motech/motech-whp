package org.motechproject.whp.patient.domain;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;

public class PatientFlag implements Serializable {
    private static final long serialVersionUID = 1L;

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
