package org.motechproject.whp.patient.command;

import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.common.exception.WHPRuntimeException;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.patient.service.TreatmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class CloseCurrentTreatment extends TreatmentUpdate {

    private TreatmentService treatmentService;

    @Autowired
    public CloseCurrentTreatment(PatientService patientService,
                                 TreatmentService treatmentService) {
        super(patientService, UpdateScope.closeTreatment);
        this.treatmentService = treatmentService;
    }

    @Override
    public void apply(PatientRequest patientRequest) {
        Patient patient = patientService.findByPatientId(patientRequest.getCase_id());
        List<WHPErrorCode> errorCodes = new ArrayList<WHPErrorCode>();
        if (!canCloseCurrentTreatment(patient, patientRequest, errorCodes)) {
            throw new WHPRuntimeException(errorCodes);
        }
        treatmentService.closeTreatment(patientRequest);
    }

    private boolean canCloseCurrentTreatment(Patient patient, PatientRequest patientRequest, List<WHPErrorCode> errorCodes) {
        String tbId = patientRequest.getTb_id();
        if (noCurrentTreatmentExists(patient, errorCodes)) {
            return false;
        } else if (!updatingCurrentTreatment(tbId, patient.getCurrentTreatment(), errorCodes)) {
            return false;
        } else if (patient.isCurrentTreatmentClosed()) {
            errorCodes.add(WHPErrorCode.TREATMENT_ALREADY_CLOSED);
            return false;
        } else {
            return true;
        }
    }
}
