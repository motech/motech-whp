package org.motechproject.whp.domain.v1;

public enum GenderV1 {

    M("Male"), F("Female"), O("Other");

    private String value;

    GenderV1(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
