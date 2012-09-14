package org.motechproject.whp.refdata.domain;

public enum SputumTrackingInstance {
    PRE_TREATMENT("Pre-treatment"), IN_TREATMENT("In-treatment");

    private String displayText;

    SputumTrackingInstance(String displayText) {
        this.displayText = displayText;
    }

    public String getDisplayText() {
        return displayText;
    }

    public static SputumTrackingInstance getInstanceForValue(String value) {
        for(SputumTrackingInstance instance : SputumTrackingInstance.values()) {
            if(instance.getDisplayText().equals(value))
                return instance;
        }
        return null;
    }

    public static boolean isValid(String text) {
        for(SputumTrackingInstance sputumTrackingInstance : SputumTrackingInstance.values())
            if(sputumTrackingInstance.getDisplayText().toUpperCase().equals(text.toUpperCase()))
                return true;
        return false;
    }
}