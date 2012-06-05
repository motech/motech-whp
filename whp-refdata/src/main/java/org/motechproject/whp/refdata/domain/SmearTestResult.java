package org.motechproject.whp.refdata.domain;

public enum SmearTestResult {

    Positive("Positive"), Negative("Negative"), Indeterminate("Indeterminate/Spoiled/Poor");

    String value;

    private SmearTestResult(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}

