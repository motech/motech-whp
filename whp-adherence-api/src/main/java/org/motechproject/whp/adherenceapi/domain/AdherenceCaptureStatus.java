package org.motechproject.whp.adherenceapi.domain;

public enum AdherenceCaptureStatus {
    VALID("Valid"), INVALID("Invalid"), NO_INPUT("NO INPUT"), SKIP_INPUT("SKIP INPUT"), ADHERENCE_PROVIDED("Given"), NO_INPUT_AT_CONFIRMATION("NO INPUT AT CONFIRMATION"), INVALID_INPUT_AT_CONFIRMATION("INVALID INPUT AT CONFIRMATION");

    private String value;

    AdherenceCaptureStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
