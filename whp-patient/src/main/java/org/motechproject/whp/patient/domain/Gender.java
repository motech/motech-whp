package org.motechproject.whp.patient.domain;

public enum Gender {

    Male("M"), Female("F"), Other("O");

    private String value;

    Gender(String value) {
        this.value = value;
    }

    public static Gender get(String gender) {
        if (Male.getValue().equals(gender.toUpperCase())) return Male;
        if (Female.getValue().equals(gender.toUpperCase())) return Female;
        if (Other.getValue().equals(gender.toUpperCase())) return Other;
        return null;
    }

    public String getValue() {
        return value;
    }

}
