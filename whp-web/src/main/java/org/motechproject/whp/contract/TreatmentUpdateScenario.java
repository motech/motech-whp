package org.motechproject.whp.contract;

import org.motechproject.whp.patient.command.UpdateScope;

public enum TreatmentUpdateScenario {

    New(UpdateScope.openTreatment), Close(UpdateScope.closeTreatment), Pause(UpdateScope.pauseTreatment), Restart(UpdateScope.restartTreatment);

    private UpdateScope scope;

    TreatmentUpdateScenario(UpdateScope scope) {
        this.scope = scope;
    }

    public UpdateScope getScope() {
        return scope;
    }
}
