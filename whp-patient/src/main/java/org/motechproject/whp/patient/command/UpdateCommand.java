package org.motechproject.whp.patient.command;

import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.repository.AllPatients;

public abstract class UpdateCommand {

    protected AllPatients allPatients;
    protected final UpdateScope command;

    public UpdateCommand(AllPatients allPatients, UpdateScope command) {
        this.allPatients = allPatients;
        this.command = command;
    }

    public abstract void apply(PatientRequest patientRequest);

    public UpdateScope getCommand() {
        return command;
    }
}
