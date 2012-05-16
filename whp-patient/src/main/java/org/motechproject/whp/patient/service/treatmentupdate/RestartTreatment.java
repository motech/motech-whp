package org.motechproject.whp.patient.service.treatmentupdate;

import org.motechproject.whp.patient.contract.TreatmentUpdateRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.criteria.CriteriaErrors;
import org.motechproject.whp.patient.exception.WHPDomainException;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllProviders;
import org.motechproject.whp.patient.repository.AllTreatments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.motechproject.whp.patient.domain.criteria.UpdatePatientCriteria.canRestartCurrentTreatment;

@Component
public class RestartTreatment extends TreatmentUpdate {

    private final String CANNOT_RESTART_TREATMENT = "Cannot restart treatment for this case: ";

    @Autowired
    public RestartTreatment(AllPatients allPatients, AllTreatments allTreatments) {
        super(allPatients, allTreatments);
    }

    @Override
    public void apply(TreatmentUpdateRequest treatmentUpdateRequest) {
        Patient patient = allPatients.findByPatientId(treatmentUpdateRequest.getCase_id());
        CriteriaErrors criteriaErrors = new CriteriaErrors();

        if (!canRestartCurrentTreatment(patient, treatmentUpdateRequest, criteriaErrors)) {
            throw new WHPDomainException(CANNOT_RESTART_TREATMENT + criteriaErrors);
        }
        restartTreatment(patient, treatmentUpdateRequest, allPatients);
    }

    private void restartTreatment(Patient patient, TreatmentUpdateRequest treatmentUpdateRequest, AllPatients allPatients) {
        patient.restartCurrentTreatment(treatmentUpdateRequest.getReason_for_restart(), treatmentUpdateRequest.getDate_modified());
        allPatients.update(patient);
    }
}
