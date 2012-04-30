package org.motechproject.whp.patient.domain;

public enum TreatmentCategory {

    Category01("01", "RNTCP Category 1"), Category02("02", "RNTCP Category 2"), Category11("11", "Commercial/Private Category 1"), Category12("12", "Commercial/Private Category 2");

    private String value;

    private String displayName;

    TreatmentCategory(String value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    public String value() {
        return value;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public static TreatmentCategory get(String category) {
        if (Category01.value.equals(category)) return Category01;
        if (Category02.value.equals(category)) return Category02;
        if (Category11.value.equals(category)) return Category11;
        if (Category12.value.equals(category)) return Category12;
        return null;
    }

}
