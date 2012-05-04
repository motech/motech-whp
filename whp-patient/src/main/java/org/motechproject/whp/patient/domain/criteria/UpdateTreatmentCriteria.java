package org.motechproject.whp.patient.domain.criteria;

import org.motechproject.whp.patient.contract.TreatmentUpdateRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.ProvidedTreatment;
import org.motechproject.whp.patient.repository.AllPatients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UpdateTreatmentCriteria {

    private AllPatients allPatients;

    @Autowired
    public UpdateTreatmentCriteria(AllPatients allPatients) {
        this.allPatients = allPatients;
    }

    public boolean canOpenNewTreatment(TreatmentUpdateRequest treatmentUpdateRequest) {
        Patient patient = allPatients.findByPatientId(treatmentUpdateRequest.getCase_id());
        ProvidedTreatment currentProvidedTreatment = patient.getCurrentProvidedTreatment();
        return currentProvidedTreatment != null && currentProvidedTreatment.getTreatment().getEndDate() != null;
    }

    public boolean canCloseCurrentTreatment(TreatmentUpdateRequest treatmentUpdateRequest){
        Patient patient = allPatients.findByPatientId(treatmentUpdateRequest.getCase_id());
        ProvidedTreatment currentProvidedTreatment = patient.getCurrentProvidedTreatment();
        return currentProvidedTreatment != null
               && treatmentUpdateRequest.getTb_id().equals(currentProvidedTreatment.getTbId())
               && currentProvidedTreatment.getTreatment().getEndDate() == null;
    }
}

