package org.motechproject.whp.domain.v0;

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