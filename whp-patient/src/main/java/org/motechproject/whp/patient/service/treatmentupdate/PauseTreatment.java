package org.motechproject.whp.patient.service.treatmentupdate;

import org.motechproject.whp.patient.contract.TreatmentUpdateRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.criteria.CriteriaErrors;
import org.motechproject.whp.patient.exception.WHPDomainException;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTreatments;

import static org.motechproject.whp.patient.domain.criteria.UpdatePatientCriteria.canPauseCurrentTreatment;

public class PauseTreatment implements TreatmentUpdate {

    private final String CANNOT_PAUSE_TREATMENT = "Cannot pause treatment for this case: ";

    @Override
    public void apply(AllPatients allPatients, AllTreatments allTreatments, TreatmentUpdateRequest treatmentUpdateRequest) {
        Patient patient = allPatients.findByPatientId(treatmentUpdateRequest.getCase_id());
        CriteriaErrors criteriaErrors = new CriteriaErrors();

        if (!canPauseCurrentTreatment(patient, treatmentUpdateRequest, criteriaErrors)) {
            throw new WHPDomainException(CANNOT_PAUSE_TREATMENT + criteriaErrors);
        }
        pauseTreatment(patient, treatmentUpdateRequest, allPatients);
    }

    private void pauseTreatment(Patient patient, TreatmentUpdateRequest treatmentUpdateRequest, AllPatients allPatients) {
        patient.pauseCurrentTreatment(treatmentUpdateRequest.getReason_for_pause(), treatmentUpdateRequest.getDate_modified());
        allPatients.update(patient);
    }
}
