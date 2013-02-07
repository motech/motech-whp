package org.motechproject.whp.patient.model;

import lombok.Getter;

@Getter
public enum FlagFilter {

    True("Flagged Patients", "true"),
    False("UnFlagged Patients", "false");

    private String displayText;
    private String value;

    FlagFilter(String displayText, String value) {
        this.displayText = displayText;
        this.value = value;
    }
}