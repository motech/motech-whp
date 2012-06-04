package org.motechproject.whp.patient.command;

import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.exception.WHPErrorCode;
import org.motechproject.whp.patient.exception.WHPRuntimeException;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTherapies;
import org.motechproject.whp.patient.service.TreatmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PauseTreatment extends TreatmentUpdate {

    private TreatmentService treatmentService;

    @Autowired
    public PauseTreatment(AllPatients allPatients, AllTherapies allTreatments, TreatmentService treatmentService) {
        super(allPatients, allTreatments, UpdateScope.pauseTreatment);
        this.treatmentService = treatmentService;
    }

    @Override
    public void apply(PatientRequest patientRequest) {
        Patient patient = allPatients.findByPatientId(patientRequest.getCase_id());
        List<WHPErrorCode> errorCodes = new ArrayList<WHPErrorCode>();
        if (!canPauseCurrentTreatment(patient, patientRequest, errorCodes)) {
            throw new WHPRuntimeException(errorCodes);
        }
        treatmentService.pauseTreatment(patientRequest);
    }

    public boolean canPauseCurrentTreatment(Patient patient, PatientRequest patientRequest, List<WHPErrorCode> errorCodes) {
        if (noCurrentTreatmentExists(patient, errorCodes)) {
            return false;
        } else if (patient.isCurrentTreatmentClosed()) {
            errorCodes.add(WHPErrorCode.TREATMENT_ALREADY_CLOSED);
            return false;
        } else if (patient.getCurrentTreatment().isPaused()) {
            errorCodes.add(WHPErrorCode.TREATMENT_ALREADY_PAUSED);
            return false;
        } else {
            return updatingCurrentTreatment(patientRequest.getTb_id(), patient.getCurrentTreatment(), errorCodes);
        }
    }
}
