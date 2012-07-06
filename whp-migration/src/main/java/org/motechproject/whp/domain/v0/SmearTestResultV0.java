package org.motechproject.whp.domain.v0;

public enum SmearTestResultV0 {

    Positive("Positive"), Negative("Negative"), Indeterminate("Indeterminate/Spoiled/Poor");

    String value;

    private SmearTestResultV0(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}