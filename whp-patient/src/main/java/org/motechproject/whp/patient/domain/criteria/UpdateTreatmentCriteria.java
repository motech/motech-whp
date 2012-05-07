package org.motechproject.whp.patient.domain.criteria;

import org.motechproject.whp.patient.contract.TreatmentUpdateRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.ProvidedTreatment;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.ValidationErrors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UpdateTreatmentCriteria {

    private AllPatients allPatients;

    @Autowired
    public UpdateTreatmentCriteria(AllPatients allPatients) {
        this.allPatients = allPatients;
    }

    public boolean canOpenNewTreatment(TreatmentUpdateRequest treatmentUpdateRequest, CriteriaErrors criteriaErrors) {
        Patient patient = allPatients.findByPatientId(treatmentUpdateRequest.getCase_id());
        ProvidedTreatment currentProvidedTreatment = patient.getCurrentProvidedTreatment();
        if (currentProvidedTreatment == null) {
            criteriaErrors.add("Case does not have any current treatment");
            return false;
        }
        if (currentProvidedTreatment.getTreatment().getEndDate() == null) {
            criteriaErrors.add("Current treatment is not closed");
            return false;
        }
        return true;
    }

    public boolean canCloseCurrentTreatment(TreatmentUpdateRequest treatmentUpdateRequest, CriteriaErrors criteriaErrors) {
        Patient patient = allPatients.findByPatientId(treatmentUpdateRequest.getCase_id());
        ProvidedTreatment currentProvidedTreatment = patient.getCurrentProvidedTreatment();
        if (currentProvidedTreatment == null) {
            criteriaErrors.add("Case does not have any current treatment");
            return false;
        }
        boolean tbIdMatches = true;
        if (!treatmentUpdateRequest.getTb_id().equals(currentProvidedTreatment.getTbId())) {
            criteriaErrors.add("No such tb id for current treatment");
            tbIdMatches = false;
        }
        boolean endDateNotSet = true;
        if (currentProvidedTreatment.getTreatment().getEndDate() != null) {
            criteriaErrors.add("Current treatment is already closed");
            endDateNotSet = false;
        }
        return tbIdMatches && endDateNotSet;
    }
}

