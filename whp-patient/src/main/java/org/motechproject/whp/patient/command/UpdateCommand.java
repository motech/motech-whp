package org.motechproject.whp.patient.command;

import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.service.PatientService;

import java.util.List;

public abstract class UpdateCommand {

    protected PatientService patientService;
    protected final UpdateScope command;

    public UpdateCommand(PatientService patientService, UpdateScope command) {
        this.patientService = patientService;
        this.command = command;
    }

    public abstract void apply(PatientRequest patientRequest);

    protected boolean noCurrentTreatmentExists(Patient patient, List<WHPErrorCode> errorCodes) {
        if (patient == null) {
            errorCodes.add(WHPErrorCode.INVALID_PATIENT_CASE_ID);
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
