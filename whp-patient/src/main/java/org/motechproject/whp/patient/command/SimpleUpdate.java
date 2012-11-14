package org.motechproject.whp.patient.command;

import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.common.exception.WHPRuntimeException;
import org.motechproject.whp.common.repository.AllDistricts;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.mapper.PatientMapper;
import org.motechproject.whp.patient.repository.AllPatients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SimpleUpdate extends UpdateCommand {

    private PatientMapper patientMapper;
    private AllDistricts allDistricts;

    @Autowired
    public SimpleUpdate(AllPatients allPatients, PatientMapper patientMapper, AllDistricts allDistricts) {
        super(allPatients, UpdateScope.simpleUpdate);
        this.patientMapper = patientMapper;
        this.allDistricts = allDistricts;
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
        String tbId = patientRequest.getTb_id();
        if(patient == null) {
            errorCodes.add(WHPErrorCode.INVALID_PATIENT_CASE_ID);
            return false;
        }
        if(!patient.hasTreatment(tbId)) {
            errorCodes.add(WHPErrorCode.NO_SUCH_TREATMENT_EXISTS);
            return false;
        }
        if(!patientRequest.getAddress().isEmpty() && allDistricts.findByName(patientRequest.getAddress().getAddress_district()) == null) {
            errorCodes.add(WHPErrorCode.INVALID_DISTRICT);
            return false;
        }
        return true;
    }
}
