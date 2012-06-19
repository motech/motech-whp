package org.motechproject.whp.patient.command;

import org.motechproject.common.exception.WHPErrorCode;
import org.motechproject.common.exception.WHPRuntimeException;
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
public class OpenNewTreatment extends TreatmentUpdate {

    private TreatmentService treatmentService;

    @Autowired
    public OpenNewTreatment(AllPatients allPatients, AllTherapies allTreatments, TreatmentService treatmentService) {
        super(allPatients, allTreatments, UpdateScope.openTreatment);
        this.treatmentService = treatmentService;
    }

    @Override
    public void apply(PatientRequest patientRequest) {
        Patient patient = allPatients.findByPatientId(patientRequest.getCase_id());
        List<WHPErrorCode> errorCodes = new ArrayList<WHPErrorCode>();
        if (!canOpenNewTreatment(patient, errorCodes)) {
            throw new WHPRuntimeException(errorCodes);
        }
        treatmentService.openTreatment(patientRequest);
    }

    public boolean canOpenNewTreatment(Patient patient, List<WHPErrorCode> errorCodes) {
        if (noCurrentTreatmentExists(patient, errorCodes))
            return false;
        if (!patient.isCurrentTreatmentClosed()) {
            errorCodes.add(WHPErrorCode.TREATMENT_NOT_CLOSED);
            return false;
        }
        return true;
    }
}
