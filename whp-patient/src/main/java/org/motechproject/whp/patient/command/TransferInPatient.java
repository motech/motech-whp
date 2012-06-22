package org.motechproject.whp.patient.command;

import org.motechproject.common.exception.WHPErrorCode;
import org.motechproject.common.exception.WHPRuntimeException;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Therapy;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTherapies;
import org.motechproject.whp.patient.service.TreatmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TransferInPatient extends TreatmentUpdate {

    private TreatmentService treatmentService;

    @Autowired
    public TransferInPatient(AllPatients allPatients, AllTherapies allTreatments, TreatmentService treatmentService) {
        super(allPatients, allTreatments, UpdateScope.transferIn);
        this.treatmentService = treatmentService;
    }

    public void apply(PatientRequest patientRequest) {
        Patient patient = allPatients.findByPatientId(patientRequest.getCase_id());
        List<WHPErrorCode> errorCodes = new ArrayList<WHPErrorCode>();
        if (!canTransferInPatient(patient, patientRequest, errorCodes)) {
            throw new WHPRuntimeException(errorCodes);
        }
        treatmentService.transferInPatient(patientRequest);
    }

    private boolean canTransferInPatient(Patient patient, PatientRequest patientRequest, List<WHPErrorCode> errorCodes) {
        if (noCurrentTreatmentExists(patient, errorCodes)) {
            return false;
        } else if (!patient.isCurrentTreatmentClosed()) {
            errorCodes.add(WHPErrorCode.TREATMENT_NOT_CLOSED);
            return false;
        } else if (treatmentDetailsDoNotMatch(patient, patientRequest)) {
            errorCodes.add(WHPErrorCode.TREATMENT_DETAILS_DO_NOT_MATCH);
            return false;
        } else {
            return true;
        }
    }

    private boolean treatmentDetailsDoNotMatch(Patient patient, PatientRequest patientRequest) {
        return notOfSameTreatmentCategory(patient, patientRequest);
    }

    private boolean notOfSameTreatmentCategory(Patient patient, PatientRequest patientRequest) {
        Therapy latestTherapy = patient.currentTherapy();
        return !latestTherapy.getTreatmentCategory().equals(patientRequest.getTreatment_category());
    }
}
