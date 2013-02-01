package org.motechproject.whp.patient.model;

public enum AlertTypeFilter {

    AllAlerts("All alerts (Patients with one or more alerts set)"),
    AdherenceMissingWithSeverityOne("Adherence missing for 1 week alert (Yellow alert)"),
    AdherenceMissingWithSeverityTwo("Adherence missing for 2 weeks alert (Red alert)"),
    AdherenceMissingWithSeverityThree("Adherence missing for 6 weeks alert (Maroon alert)"),
    CumulativeMissedDoses("Cumulative missed doses alert"),
    TreatmentNotStarted("Missing Date of Start of Treatment Alert"),
    NoAlerts("No Alerts");

    private String displayText;

    AlertTypeFilter(String displayText) {
        this.displayText = displayText;
    }

    public String getDisplayText() {
        return displayText;
    }
}