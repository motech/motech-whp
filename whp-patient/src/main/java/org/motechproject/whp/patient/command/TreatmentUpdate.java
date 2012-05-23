package org.motechproject.whp.patient.command;

import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTreatments;

public abstract class TreatmentUpdate extends UpdateCommand {

    protected AllTreatments allTreatments;

    protected TreatmentUpdate(AllPatients allPatients, AllTreatments allTreatments, UpdateScope command) {
        super(allPatients, command);
        this.allTreatments = allTreatments;
    }

}
