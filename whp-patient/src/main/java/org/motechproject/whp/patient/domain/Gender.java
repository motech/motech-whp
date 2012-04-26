package org.motechproject.whp.patient.domain;

public enum Gender {

    M("M"), F("F"), O("O");

    private String value;

    Gender(String value) {
        this.value = value;
    }

    public static Gender get(String gender) {
        if (M.getValue().equals(gender.toUpperCase())) return M;
        if (F.getValue().equals(gender.toUpperCase())) return F;
        if (O.getValue().equals(gender.toUpperCase())) return O;
        return null;
    }

    public String getValue() {
        return value;
    }

}
