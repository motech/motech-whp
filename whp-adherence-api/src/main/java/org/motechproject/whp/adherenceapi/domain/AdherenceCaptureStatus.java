package org.motechproject.whp.adherenceapi.domain;

public enum AdherenceCaptureStatus {
    INVALID("Invalid"), VALID("Valid"), NO_INPUT("NoInput"), SKIPPED("Skipped"), CONFIRMED("Confirmed");

    private String text;

    AdherenceCaptureStatus(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
