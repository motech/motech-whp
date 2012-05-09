package org.motechproject.whp.patient.service.treatmentupdate;

import org.motechproject.whp.patient.contract.TreatmentUpdateRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.ProvidedTreatment;
import org.motechproject.whp.patient.domain.Treatment;
import org.motechproject.whp.patient.domain.criteria.CriteriaErrors;
import org.motechproject.whp.patient.exception.WHPDomainException;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTreatments;

import static org.motechproject.util.DateUtil.today;
import static org.motechproject.whp.patient.domain.criteria.UpdatePatientCriteria.canCloseCurrentTreatment;

public class CloseCurrentTreatment implements TreatmentUpdateScenario {

    private final String CANNOT_CLOSE_CURRENT_TREATMENT = "Cannot close current treatment for case: ";

    @Override
    public void performUpdate(AllPatients allPatients, AllTreatments allTreatments, TreatmentUpdateRequest treatmentUpdateRequest){
        Patient patient = allPatients.findByPatientId(treatmentUpdateRequest.getCase_id());
        CriteriaErrors criteriaErrors = new CriteriaErrors();

        if (canCloseCurrentTreatment(patient, treatmentUpdateRequest, criteriaErrors)){
            closeCurrentTreatment(patient, treatmentUpdateRequest, allPatients, allTreatments);
        } else {
            throw new WHPDomainException(CANNOT_CLOSE_CURRENT_TREATMENT + criteriaErrors);
        }
    }

    private void closeCurrentTreatment(Patient patient, TreatmentUpdateRequest treatmentUpdateRequest, AllPatients allPatients, AllTreatments allTreatments) {
        ProvidedTreatment currentProvidedTreatment = patient.getCurrentProvidedTreatment();
        currentProvidedTreatment.setEndDate(today());
        Treatment treatment = currentProvidedTreatment.getTreatment();
        treatment.setEndDate(today());
        treatment.setReasonForClosure(treatmentUpdateRequest.getReason_for_closure());
        treatment.setTreatmentComplete(treatmentUpdateRequest.getTreatment_complete());
        patient.setLastModifiedDate(treatmentUpdateRequest.getDate_modified());
        allTreatments.update(treatment);
        allPatients.update(patient);
    }
}
