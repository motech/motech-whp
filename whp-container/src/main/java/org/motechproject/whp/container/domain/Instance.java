package org.motechproject.whp.container.domain;

public enum Instance {
    PRE_TREATMENT("Pre-treatment"), IN_TREATMENT("In-treatment");

    private String displayText;

    Instance(String displayText) {
        this.displayText = displayText;
    }

    public String getDisplayText() {
        return displayText;
    }
}