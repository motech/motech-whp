package org.motechproject.whp.patient.service.treatmentupdate;

import org.motechproject.whp.patient.contract.TreatmentUpdateRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.ProvidedTreatment;
import org.motechproject.whp.patient.domain.Treatment;
import org.motechproject.whp.patient.exception.WHPDomainException;
import org.motechproject.whp.patient.exception.errorcode.WHPDomainErrorCode;
import org.motechproject.whp.patient.mapper.TreatmentMapper;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTreatments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

import static org.motechproject.whp.patient.domain.criteria.UpdatePatientCriteria.canOpenNewTreatment;
import static org.motechproject.whp.patient.mapper.PatientMapper.createNewProvidedTreatmentForTreatmentCategoryChange;

@Component
public class OpenNewTreatment extends TreatmentUpdate {

    @Autowired
    public OpenNewTreatment(AllPatients allPatients, AllTreatments allTreatments) {
        super(allPatients, allTreatments);
    }

    @Override
    public void apply(TreatmentUpdateRequest treatmentUpdateRequest) {
        Patient patient = allPatients.findByPatientId(treatmentUpdateRequest.getCase_id());
        ArrayList<WHPDomainErrorCode> errorCodes = new ArrayList<WHPDomainErrorCode>();
        if (!canOpenNewTreatment(patient, errorCodes)) {
            throw new WHPDomainException(errorCodes);
        }
        addNewTreatmentForCategoryChange(patient, treatmentUpdateRequest, allPatients, allTreatments);
    }

    private void addNewTreatmentForCategoryChange(Patient patient, TreatmentUpdateRequest treatmentUpdateRequest, AllPatients allPatients, AllTreatments allTreatments) {
        Treatment newTreatment = TreatmentMapper.createNewTreatment(patient, treatmentUpdateRequest);
        allTreatments.add(newTreatment);
        ProvidedTreatment newProvidedTreatment = createNewProvidedTreatmentForTreatmentCategoryChange(patient, treatmentUpdateRequest, newTreatment);
        patient.addProvidedTreatment(newProvidedTreatment, treatmentUpdateRequest.getDate_modified());
        allPatients.update(patient);
    }
}
