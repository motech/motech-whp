package org.motechproject.whp.patient.domain;

public enum Gender {

    Male, Female;

    public static Gender get(String gender) {
        if ("M".equals(gender.toUpperCase())) return Male;
        return Female;
    }

}
