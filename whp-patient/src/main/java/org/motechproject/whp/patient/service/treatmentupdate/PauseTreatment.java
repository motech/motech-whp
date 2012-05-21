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
import java.util.List;

import static org.motechproject.whp.patient.domain.criteria.UpdatePatientCriteria.canPauseCurrentTreatment;

@Component
public class PauseTreatment extends TreatmentUpdate {

    @Autowired
    public PauseTreatment(AllPatients allPatients, AllTreatments allTreatments) {
        super(allPatients, allTreatments);
    }

    @Override
    public void apply(TreatmentUpdateRequest treatmentUpdateRequest) {
        Patient patient = allPatients.findByPatientId(treatmentUpdateRequest.getCase_id());
        List<WHPDomainErrorCode> errorCodes = new ArrayList<WHPDomainErrorCode>();

        if (!canPauseCurrentTreatment(patient, treatmentUpdateRequest, errorCodes)) {
            throw new WHPDomainException(errorCodes);
        }
        pauseTreatment(patient, treatmentUpdateRequest, allPatients);
    }

    private void pauseTreatment(Patient patient, TreatmentUpdateRequest treatmentUpdateRequest, AllPatients allPatients) {
        patient.pauseCurrentTreatment(treatmentUpdateRequest.getReason_for_pause(), treatmentUpdateRequest.getDate_modified());
        allPatients.update(patient);
    }
}
