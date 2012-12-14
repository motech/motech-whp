package org.motechproject.whp.adherenceapi.domain;

public enum AdherenceCaptureStatus {
    VALID("Valid"), INVALID("Invalid"), NO_INPUT("NoInput"), SKIPPED("Skipped"), ADHERENCE_PROVIDED("Given");

    private String value;

    AdherenceCaptureStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
