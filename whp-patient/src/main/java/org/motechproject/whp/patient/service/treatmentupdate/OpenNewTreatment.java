package org.motechproject.whp.patient.service.treatmentupdate;

import org.motechproject.whp.patient.contract.TreatmentUpdateRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.ProvidedTreatment;
import org.motechproject.whp.patient.domain.Treatment;
import org.motechproject.whp.patient.domain.criteria.CriteriaErrors;
import org.motechproject.whp.patient.exception.WHPDomainException;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTreatments;

import static org.motechproject.whp.patient.domain.criteria.UpdatePatientCriteria.canOpenNewTreatment;
import static org.motechproject.whp.patient.mapper.PatientMapper.createNewProvidedTreatmentForTreatmentCategoryChange;
import static org.motechproject.whp.patient.mapper.PatientMapper.createNewTreatmentFrom;

public class OpenNewTreatment implements TreatmentUpdateScenario {

    private final String CANNOT_OPEN_NEW_TREATMENT = "Cannot open new treatment for this case: ";

    @Override
    public void performUpdate(AllPatients allPatients, AllTreatments allTreatments, TreatmentUpdateRequest treatmentUpdateRequest) {
        Patient patient = allPatients.findByPatientId(treatmentUpdateRequest.getCase_id());
        CriteriaErrors criteriaErrors = new CriteriaErrors();

        if (canOpenNewTreatment(patient, criteriaErrors)) {
            addNewTreatmentForCategoryChange(patient, treatmentUpdateRequest, allPatients, allTreatments);
        } else {
            throw new WHPDomainException(CANNOT_OPEN_NEW_TREATMENT + criteriaErrors);
        }
    }

    private void addNewTreatmentForCategoryChange(Patient patient, TreatmentUpdateRequest treatmentUpdateRequest, AllPatients allPatients, AllTreatments allTreatments) {
        Treatment newTreatment = createNewTreatmentFrom(patient, treatmentUpdateRequest);
        allTreatments.add(newTreatment);
        ProvidedTreatment newProvidedTreatment = createNewProvidedTreatmentForTreatmentCategoryChange(patient, treatmentUpdateRequest, newTreatment);
        patient.addProvidedTreatment(newProvidedTreatment);
        patient.setLastModifiedDate(treatmentUpdateRequest.getDate_modified());
        allPatients.update(patient);
    }
}
