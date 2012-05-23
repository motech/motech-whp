package org.motechproject.whp.patient.command;

import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTreatments;

public abstract class TreatmentUpdate {

    protected AllPatients allPatients;
    protected AllTreatments allTreatments;
    protected final String command;

    protected TreatmentUpdate(AllPatients allPatients, AllTreatments allTreatments, String command) {
        this.allPatients = allPatients;
        this.allTreatments = allTreatments;
        this.command = command;
    }

    public abstract void apply(PatientRequest patientRequest);

    public String getCommand() {
        return command;
    }
}
