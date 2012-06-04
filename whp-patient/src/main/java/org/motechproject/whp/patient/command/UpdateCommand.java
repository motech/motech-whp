package org.motechproject.whp.patient.command;

import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.exception.WHPErrorCode;
import org.motechproject.whp.patient.repository.AllPatients;

import java.util.List;

public abstract class UpdateCommand {

    protected AllPatients allPatients;
    protected final UpdateScope command;

    public UpdateCommand(AllPatients allPatients, UpdateScope command) {
        this.allPatients = allPatients;
        this.command = command;
    }

    public abstract void apply(PatientRequest patientRequest);

    protected boolean noCurrentTreatmentExists(Patient patient, List<WHPErrorCode> errorCodes) {
        if (patient == null) {
            errorCodes.add(WHPErrorCode.CASE_ID_DOES_NOT_EXIST);
            return true;
        }
        if (!patient.hasCurrentTreatment()) {
            errorCodes.add(WHPErrorCode.NO_EXISTING_TREATMENT_FOR_CASE);
            return true;
        }
        return false;
    }

    public UpdateScope getCommand() {
        return command;
    }
}
