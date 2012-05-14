package org.motechproject.whp.patient.service.treatmentupdate;

public enum TreatmentUpdateScenario {

    New("openTreatment"), Close("closeTreatment"), TransferIn("transferIn"), Pause("pauseTreatment"), Restart("restartTreatment");

    private String scope;

    private TreatmentUpdateScenario(String scope) {
        this.scope = scope;
    }

    public String getScope() {
        return scope;
    }

    public TreatmentUpdate getUpdateScenario() {
        switch (this) {
            case New:
                return new OpenNewTreatment();
            case Close:
                return new CloseCurrentTreatment();
            case TransferIn:
                return new TransferInPatient();
            case Pause:
                return new PauseTreatment();
            case Restart:
                return new RestartTreatment();
            default:
                return null;
        }
    }
}
