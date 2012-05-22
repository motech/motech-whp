package org.motechproject.whp.patient.service.treatmentupdate;

import org.motechproject.whp.patient.contract.TreatmentUpdateRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.exception.WHPErrorCode;
import org.motechproject.whp.patient.exception.WHPRuntimeException;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTreatments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

import static org.motechproject.whp.patient.domain.criteria.UpdatePatientCriteria.canCloseCurrentTreatment;

@Component
public class CloseCurrentTreatment extends TreatmentUpdate {

    @Autowired
    public CloseCurrentTreatment(AllPatients allPatients, AllTreatments allTreatments) {
        super(allPatients, allTreatments);
    }

    @Override
    public void apply(TreatmentUpdateRequest treatmentUpdateRequest){
        Patient patient = allPatients.findByPatientId(treatmentUpdateRequest.getCase_id());

         ArrayList<WHPErrorCode> errorCodes = new ArrayList<WHPErrorCode>();

        if (!canCloseCurrentTreatment(patient, treatmentUpdateRequest, errorCodes)){
            throw new WHPRuntimeException(errorCodes);
        }
        closeCurrentTreatment(patient, treatmentUpdateRequest, allPatients, allTreatments);
    }

    private void closeCurrentTreatment(Patient patient, TreatmentUpdateRequest treatmentUpdateRequest, AllPatients allPatients, AllTreatments allTreatments) {
        patient.closeCurrentTreatment(treatmentUpdateRequest.getTreatment_outcome(), treatmentUpdateRequest.getDate_modified());
        allTreatments.update(patient.getCurrentProvidedTreatment().getTreatment());
        allPatients.update(patient);
    }
}
