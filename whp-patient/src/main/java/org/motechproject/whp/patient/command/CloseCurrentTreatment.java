package org.motechproject.whp.patient.command;

import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.common.exception.WHPRuntimeException;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTherapies;
import org.motechproject.whp.patient.service.TreatmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class CloseCurrentTreatment extends TreatmentUpdate {

    private TreatmentService treatmentService;

    @Autowired
    public CloseCurrentTreatment(AllPatients allPatients,
                                 AllTherapies allTreatments,
                                 TreatmentService treatmentService) {
        super(allPatients, allTreatments, UpdateScope.closeTreatment);
        this.treatmentService = treatmentService;
    }

    @Override
    public void apply(PatientRequest patientRequest) {
        Patient patient = allPatients.findByPatientId(patientRequest.getCase_id());
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
