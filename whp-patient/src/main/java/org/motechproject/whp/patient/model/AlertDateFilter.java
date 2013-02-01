package org.motechproject.whp.patient.model;

public enum AlertDateFilter {

    TillDate("Alerts raised till date", null, null),
    Today("Alerts raised today", null, null),
    ThisWeek("Alerts raised this week", null, null),
    ThisMonth("Alerts raised this month", null, null);

    private String displayText;
    private String from;
    private String to;

    AlertDateFilter(String displayText, String from, String to) {
        this.displayText = displayText;
        this.from = from;
        this.to = to;
    }

    public String getDisplayText() {
        return displayText;
    }
}