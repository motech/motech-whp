package org.motechproject.whp.patient.command;

import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTherapies;

public abstract class TreatmentUpdate extends UpdateCommand {

    protected AllTherapies allTherapies;

    protected TreatmentUpdate(AllPatients allPatients, AllTherapies allTherapies, UpdateScope command) {
        super(allPatients, command);
        this.allTherapies = allTherapies;
    }

}
