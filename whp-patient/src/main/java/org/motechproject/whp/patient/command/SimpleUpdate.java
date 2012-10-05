package org.motechproject.whp.patient.command;

import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.common.exception.WHPRuntimeException;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Treatment;
import org.motechproject.whp.patient.mapper.PatientMapper;
import org.motechproject.whp.patient.repository.AllPatients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SimpleUpdate extends UpdateCommand {

    private PatientMapper patientMapper;

    @Autowired
    public SimpleUpdate(AllPatients allPatients, PatientMapper patientMapper) {
        super(allPatients, UpdateScope.simpleUpdate);
        this.patientMapper = patientMapper;
    }

    @Override
    public void apply(PatientRequest patientRequest) {
        Patient patient = allPatients.findByPatientId(patientRequest.getCase_id());
        ArrayList<WHPErrorCode> errorCodes = new ArrayList<WHPErrorCode>();
        if (canPerformSimpleUpdate(patient, patientRequest, errorCodes)) {
            Patient updatedPatient = patientMapper.mapUpdates(patientRequest, patient);
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
