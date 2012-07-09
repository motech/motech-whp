package org.motechproject.whp.v0.domain;

public enum GenderV0 {

    M("Male"), F("Female"), O("Other");

    private String value;

    GenderV0(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}