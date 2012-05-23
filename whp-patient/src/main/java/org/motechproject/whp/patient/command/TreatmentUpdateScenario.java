package org.motechproject.whp.patient.command;

public enum TreatmentUpdateScenario {

    New(UpdateScope.openTreatment), Close(UpdateScope.closeTreatment), TransferIn(UpdateScope.transferIn), Pause(UpdateScope.pauseTreatment), Restart(UpdateScope.restartTreatment);

    private UpdateScope scope;

    TreatmentUpdateScenario(UpdateScope scope) {
        this.scope = scope;
    }

    public UpdateScope getScope() {
        return scope;
    }
}
