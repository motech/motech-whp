package org.motechproject.whp.uimodel;

public enum InTreatmentSputumTrackingInstance {

    EndIP("End of Intensive Phase"),
    ExtendedIP("Extended IP"),
    TwoMonthsIntoCP("Two Months Into CP"),
    EndTreatment("End Treatment"),
    TreatmentInterruption("Treatment Interruption", "TreatmentInterruption");

    private String displayText;
    private String searchString;

    private InTreatmentSputumTrackingInstance() {
        this.searchString = this.name();
    }

    private InTreatmentSputumTrackingInstance(String displayText) {
        this();
        this.displayText = displayText;
    }

    private InTreatmentSputumTrackingInstance(String displayText, String searchString) {
        this.displayText = displayText;
        this.searchString = searchString;
    }

    public String getDisplayText() {
        return displayText;
    }

    public String getSearchString() {
        return searchString;
    }

    public static InTreatmentSputumTrackingInstance getInstanceForValue(String value) {
        for (InTreatmentSputumTrackingInstance instance : InTreatmentSputumTrackingInstance.values()) {
            if (instance.getDisplayText().equalsIgnoreCase(value) || instance.name().equalsIgnoreCase(value))
                return instance;
        }
        return null;
    }

    public static boolean isValidMappingInstance(String text) {
        return isValid(text);
    }

    public static InTreatmentSputumTrackingInstance getInstanceByName(String name) {
        for (InTreatmentSputumTrackingInstance instance : InTreatmentSputumTrackingInstance.values()) {
            if (instance.name().equalsIgnoreCase(name))
                return instance;
        }
        return null;
    }

    private static boolean isValid(String text) {
        for (InTreatmentSputumTrackingInstance sputumTrackingInstance : values())
            if (sputumTrackingInstance.getDisplayText().equalsIgnoreCase(text) || sputumTrackingInstance.name().equalsIgnoreCase(text))
                return true;
        return false;
    }
}