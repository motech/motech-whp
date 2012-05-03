package org.motechproject.whp.patient.domain;

public enum TreatmentUpdate {
    NewTreatment("openTreatment"), CloseTreatment("closeTreatment");

    private String scope;

    private TreatmentUpdate(String scope) {
        this.scope = scope;
    }

    public String getScope() {
        return scope;
    }
}
