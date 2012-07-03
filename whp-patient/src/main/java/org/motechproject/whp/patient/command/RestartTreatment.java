package org.motechproject.whp.patient.command;

import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.common.exception.WHPRuntimeException;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.service.TreatmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RestartTreatment extends TreatmentUpdate {

    private TreatmentService service;

    @Autowired
    public RestartTreatment(AllPatients allPatients, TreatmentService service) {
        super(allPatients, UpdateScope.restartTreatment);
        this.service = service;
    }

    @Override
    public void apply(PatientRequest patientRequest) {
        Patient patient = allPatients.findByPatientId(patientRequest.getCase_id());
        List<WHPErrorCode> errorCodes = new ArrayList<WHPErrorCode>();
        if (!canRestartCurrentTreatment(patient, patientRequest, errorCodes)) {
            throw new WHPRuntimeException(errorCodes);
        }
        service.restartTreatment(patientRequest);
    }

    public boolean canRestartCurrentTreatment(Patient patient, PatientRequest patientRequest, List<WHPErrorCode> errorCodes) {
        if (noCurrentTreatmentExists(patient, errorCodes)) {
            return false;
        } else if (!updatingCurrentTreatment(patientRequest.getTb_id(), patient.getCurrentTreatment(), errorCodes)) {
            return false;
        } else if (!patient.getCurrentTreatment().isPaused()) {
            errorCodes.add(WHPErrorCode.TREATMENT_ALREADY_IN_PROGRESS);
            return false;
        } else {
            return true;
        }
    }
}
