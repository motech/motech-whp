package org.motechproject.whp.patient.command;

import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.repository.AllPatients;

public abstract class UpdateCommand {

    protected AllPatients allPatients;
    protected final String command;

    public UpdateCommand(AllPatients allPatients, String command) {
        this.allPatients = allPatients;
        this.command = command;
    }

    public abstract void apply(PatientRequest patientRequest);

    public String getCommand() {
        return command;
    }
}
