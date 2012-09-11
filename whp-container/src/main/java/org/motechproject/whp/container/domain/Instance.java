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

    public static boolean isValid(String text) {
        for(Instance instance : Instance.values())
            if(instance.getDisplayText().toUpperCase().equals(text.toUpperCase()))
                return true;
        return false;
    }
}