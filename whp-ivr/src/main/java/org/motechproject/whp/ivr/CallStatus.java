package org.motechproject.whp.ivr;


public enum CallStatus {
    ADHERENCE_ALREADY_PROVIDED("adherenceAlreadyProvided"), VALID_ADHERENCE_CAPTURE("validAdherenceCapture"), OUTSIDE_ADHERENCE_CAPTURE_WINDOW("outsideAdherenceCaptureWindow");

    private String value;
    CallStatus(String value) {
          this.value = value;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return value();
    }
}
