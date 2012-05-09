package org.motechproject.whp.patient.service.treatmentupdate;

public enum TreatmentUpdate {
    NewTreatment("openTreatment"), CloseTreatment("closeTreatment"), TransferIn("transferIn");

    private String scope;

    private TreatmentUpdate(String scope) {
        this.scope = scope;
    }

    public String getScope() {
        return scope;
    }

    public TreatmentUpdateScenario getUpdateScenario(){
        switch (this){
            case NewTreatment: return new OpenNewTreatment();
            case CloseTreatment: return new CloseCurrentTreatment();
            case TransferIn: return new TransferInPatient();
            default: return null;
        }
    }
}
