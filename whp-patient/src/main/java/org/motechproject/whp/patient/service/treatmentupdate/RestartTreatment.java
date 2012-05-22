package org.motechproject.whp.patient.service.treatmentupdate;

import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.exception.WHPErrorCode;
import org.motechproject.whp.patient.exception.WHPRuntimeException;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTreatments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

import static org.motechproject.whp.patient.domain.criteria.UpdatePatientCriteria.canRestartCurrentTreatment;

@Component
public class RestartTreatment extends TreatmentUpdate {

    @Autowired
    public RestartTreatment(AllPatients allPatients, AllTreatments allTreatments) {
        super(allPatients, allTreatments);
    }

    @Override
    public void apply(PatientRequest patientRequest) {
        Patient patient = allPatients.findByPatientId(patientRequest.getCase_id());
         ArrayList<WHPErrorCode> errorCodes = new ArrayList<WHPErrorCode>();

        if (!canRestartCurrentTreatment(patient, patientRequest, errorCodes)) {
            throw new WHPRuntimeException(errorCodes);
        }
        restartTreatment(patient, patientRequest, allPatients);
    }

    private void restartTreatment(Patient patient, PatientRequest patientRequest, AllPatients allPatients) {
        patient.restartCurrentTreatment(patientRequest.getReason_for_restart(), patientRequest.getDate_modified());
        allPatients.update(patient);
    }
}
