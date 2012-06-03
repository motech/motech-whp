package org.motechproject.whp.patient.command;

import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.exception.WHPErrorCode;
import org.motechproject.whp.patient.exception.WHPRuntimeException;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTherapies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.motechproject.whp.patient.domain.criteria.UpdatePatientCriteria.canPauseCurrentTreatment;

@Component
public class PauseTreatment extends TreatmentUpdate {

    @Autowired
    public PauseTreatment(AllPatients allPatients, AllTherapies allTreatments) {
        super(allPatients, allTreatments, UpdateScope.pauseTreatment);
    }

    @Override
    public void apply(PatientRequest patientRequest) {
        Patient patient = allPatients.findByPatientId(patientRequest.getCase_id());
        List<WHPErrorCode> errorCodes = new ArrayList<WHPErrorCode>();

        if (!canPauseCurrentTreatment(patient, patientRequest, errorCodes)) {
            throw new WHPRuntimeException(errorCodes);
        }
        pauseTreatment(patient, patientRequest, allPatients);
    }

    private void pauseTreatment(Patient patient, PatientRequest patientRequest, AllPatients allPatients) {
        patient.pauseCurrentTreatment(patientRequest.getReason(), patientRequest.getDate_modified());
        allPatients.update(patient);
    }
}
