package org.motechproject.whp.patient.domain;

public enum TreatmentCategory {

    Category01("01"), Category02("02"), Category11("11"), Category12("12");

    private String value;

    TreatmentCategory(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static TreatmentCategory get(String category) {
        if (Category01.value.equals(category)) return Category01;
        if (Category02.value.equals(category)) return Category02;
        if (Category11.value.equals(category)) return Category11;
        if (Category12.value.equals(category)) return Category12;
        return null;
    }

}
