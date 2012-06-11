package org.motechproject.whp.patient.command;

import org.motechproject.common.exception.WHPErrorCode;
import org.motechproject.common.exception.WHPRuntimeException;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Treatment;
import org.motechproject.whp.patient.repository.AllPatients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.motechproject.whp.patient.mapper.PatientMapper.mapUpdates;

@Component
public class SimpleUpdate extends UpdateCommand {

    @Autowired
    public SimpleUpdate(AllPatients allPatients) {
        super(allPatients, UpdateScope.simpleUpdate);
    }

    @Override
    public void apply(PatientRequest patientRequest) {
        Patient patient = allPatients.findByPatientId(patientRequest.getCase_id());
        ArrayList<WHPErrorCode> errorCodes = new ArrayList<WHPErrorCode>();
        if (canPerformSimpleUpdate(patient, patientRequest, errorCodes)) {
            Patient updatedPatient = mapUpdates(patientRequest, patient);
            allPatients.update(updatedPatient);
        } else {
            throw new WHPRuntimeException(errorCodes);
        }
    }

    public boolean canPerformSimpleUpdate(Patient patient, PatientRequest patientRequest, List<WHPErrorCode> errorCodes) {
        if (noCurrentTreatmentExists(patient, errorCodes))
            return false;
        Treatment currentTreatment = patient.getCurrentTreatment();
        if (!currentTreatment.getTbId().equalsIgnoreCase(patientRequest.getTb_id())) {
            errorCodes.add(WHPErrorCode.TB_ID_DOES_NOT_MATCH);
            return false;
        }
        return true;
    }
}
