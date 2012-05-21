package org.motechproject.whp.patient.service.treatmentupdate;

import org.motechproject.whp.patient.contract.TreatmentUpdateRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.exception.WHPDomainException;
import org.motechproject.whp.patient.exception.errorcode.WHPDomainErrorCode;
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
    public void apply(TreatmentUpdateRequest treatmentUpdateRequest) {
        Patient patient = allPatients.findByPatientId(treatmentUpdateRequest.getCase_id());
         ArrayList<WHPDomainErrorCode> errorCodes = new ArrayList<WHPDomainErrorCode>();

        if (!canRestartCurrentTreatment(patient, treatmentUpdateRequest, errorCodes)) {
            throw new WHPDomainException(errorCodes);
        }
        restartTreatment(patient, treatmentUpdateRequest, allPatients);
    }

    private void restartTreatment(Patient patient, TreatmentUpdateRequest treatmentUpdateRequest, AllPatients allPatients) {
        patient.restartCurrentTreatment(treatmentUpdateRequest.getReason_for_restart(), treatmentUpdateRequest.getDate_modified());
        allPatients.update(patient);
    }
}
