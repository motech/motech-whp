package org.motechproject.whp.patient.service.treatmentupdate;

public enum TreatmentUpdate {
    New("openTreatment"), Close("closeTreatment"), TransferIn("transferIn");

    private String scope;

    private TreatmentUpdate(String scope) {
        this.scope = scope;
    }

    public String getScope() {
        return scope;
    }

    public TreatmentUpdateScenario getUpdateScenario(){
        switch (this){
            case New: return new OpenNewTreatment();
            case Close: return new CloseCurrentTreatment();
            case TransferIn: return new TransferInPatient();
            default: return null;
        }
    }
}
