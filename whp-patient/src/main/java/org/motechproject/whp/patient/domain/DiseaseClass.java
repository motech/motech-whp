package org.motechproject.whp.patient.domain;

public enum DiseaseClass {
    P("Pulmonary"), E("Extra Pulmonary");
    private String value;

    DiseaseClass (String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
