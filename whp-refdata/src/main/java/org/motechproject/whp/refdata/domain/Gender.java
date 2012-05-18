package org.motechproject.whp.refdata.domain;

public enum Gender {

    M("Male"), F("Female"), O("Other");

    private String value;

    Gender(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
