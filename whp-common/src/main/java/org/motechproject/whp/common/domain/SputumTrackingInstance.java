package org.motechproject.whp.common.domain;

public enum SputumTrackingInstance {
    PreTreatment("Pre-treatment"), EndIP("End of Intensive Phase"), ExtendedIP("Extended IP"), TwoMonthsIntoCP("Two Months Into CP"),
    EndTreatment("End Treatment"), TreatmentInterruption1("Treatment Interruption 1"), TreatmentInterruption2("Treatment Interruption 2"), TreatmentInterruption3("Treatment Interruption 3");

    public static SputumTrackingInstance[] IN_TREATMENT_INSTANCES = {EndIP, ExtendedIP, TwoMonthsIntoCP, EndTreatment, TreatmentInterruption1, TreatmentInterruption2, TreatmentInterruption3};

    private String displayText;

    SputumTrackingInstance(String displayText) {
        this.displayText = displayText;
    }

    public String getDisplayText() {
        return displayText;
    }

    public static SputumTrackingInstance getInstanceForValue(String value) {
        for (SputumTrackingInstance instance : SputumTrackingInstance.values()) {
            if (instance.getDisplayText().equalsIgnoreCase(value) || instance.name().equalsIgnoreCase(value))
                return instance;
        }
        return null;
    }

    public static boolean isValidMappingInstance(String text) {
        return isValid(text);
    }

    public static SputumTrackingInstance getInstanceByName(String name) {
        for (SputumTrackingInstance instance : SputumTrackingInstance.values()) {
            if (instance.name().equalsIgnoreCase(name))
                return instance;
        }
        return null;
    }

    private static boolean isValid(String text) {
        for (SputumTrackingInstance sputumTrackingInstance : values())
            if (sputumTrackingInstance.getDisplayText().equalsIgnoreCase(text) || sputumTrackingInstance.name().equalsIgnoreCase(text))
                return true;
        return false;
    }
}