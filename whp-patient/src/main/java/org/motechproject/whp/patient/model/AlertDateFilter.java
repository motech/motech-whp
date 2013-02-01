package org.motechproject.whp.patient.model;

public enum AlertDateFilter {

    TillDate("Alerts raised till date"),
    Today("Alerts raised today"),
    ThisWeek("Alerts raised this week"),
    ThisMonth("Alerts raised this month");

    private String displayText;

    AlertDateFilter(String displayText) {
        this.displayText = displayText;
    }

    public String getDisplayText() {
        return displayText;
    }
}