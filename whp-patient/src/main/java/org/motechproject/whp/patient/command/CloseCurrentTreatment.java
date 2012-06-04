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

import static org.motechproject.whp.patient.domain.criteria.UpdatePatientCriteria.canCloseCurrentTreatment;

@Component
public class CloseCurrentTreatment extends TreatmentUpdate {

    @Autowired
    public CloseCurrentTreatment(AllPatients allPatients, AllTherapies allTreatments) {
        super(allPatients, allTreatments, UpdateScope.closeTreatment);
    }

    @Override
    public void apply(PatientRequest patientRequest) {
        Patient patient = allPatients.findByPatientId(patientRequest.getCase_id());
        List<WHPErrorCode> errorCodes = new ArrayList<WHPErrorCode>();

        if (!canCloseCurrentTreatment(patient, patientRequest, errorCodes)) {
            throw new WHPRuntimeException(errorCodes);
        }
        closeCurrentTreatment(patient, patientRequest, allPatients, allTherapies);
    }

    private void closeCurrentTreatment(Patient patient, PatientRequest patientRequest, AllPatients allPatients, AllTherapies allTreatments) {
        patient.closeCurrentTreatment(patientRequest.getTreatment_outcome(), patientRequest.getDate_modified());
        allTreatments.update(patient.latestTherapy());
        allPatients.update(patient);
    }
}
