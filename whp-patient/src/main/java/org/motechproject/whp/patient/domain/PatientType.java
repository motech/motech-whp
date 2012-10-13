package org.motechproject.whp.patient.domain;

public enum PatientType {
    New("New"), Relapse("Relapse"), TransferredIn("Transferred In"), TreatmentAfterDefault("Treatment After Default"), TreatmentFailure("Treatment Failure"), Chronic("Chronic"), Others("Others");
    private String value;

    PatientType (String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
