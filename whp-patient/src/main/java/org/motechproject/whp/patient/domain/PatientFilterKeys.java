package org.motechproject.whp.patient.domain;

public enum PatientFilterKeys {

    SelectedDistrict("selectedDistrict"), SelectedProvider("selectedProvider");
    String value;

    private PatientFilterKeys(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
