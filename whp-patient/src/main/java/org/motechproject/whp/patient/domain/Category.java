package org.motechproject.whp.patient.domain;

public enum Category {

    Category1("01"), Category2("02");

    private String value;

    Category(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static Category get(String category) {
        if (Category1.value.equals(category)) return Category1;
        if (Category2.value.equals(category)) return Category2;
        return null;
    }

}
