package org.motechproject.whp.patient.service;

import org.motechproject.util.DateUtil;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.contract.TreatmentUpdateRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.ProvidedTreatment;
import org.motechproject.whp.patient.domain.Treatment;
import org.motechproject.whp.patient.domain.criteria.UpdateTreatmentCriteria;
import org.motechproject.whp.patient.exception.WHPDomainException;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTreatments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.motechproject.util.DateUtil.today;
import static org.motechproject.whp.patient.mapper.PatientMapper.*;

@Service
public class PatientService {

    private AllTreatments allTreatments;
    private AllPatients allPatients;
    private UpdateTreatmentCriteria updateTreatmentCriteria;

    @Autowired
    public PatientService(AllPatients allPatients, AllTreatments allTreatments, UpdateTreatmentCriteria updateTreatmentCriteria) {
        this.allPatients = allPatients;
        this.allTreatments = allTreatments;
        this.updateTreatmentCriteria = updateTreatmentCriteria;
    }

    public void add(PatientRequest patientRequest) {
        Patient patient = mapBasicInfo(patientRequest);

        Treatment treatment = mapTreatmentInfo(patientRequest);
        allTreatments.add(treatment);

        ProvidedTreatment providedTreatment = mapProvidedTreatment(patientRequest, treatment);

        patient.addProvidedTreatment(providedTreatment);

        allPatients.add(patient);
    }

    public void simpleUpdate(PatientRequest patientRequest) {
        Patient patient = allPatients.findByPatientId(patientRequest.getCase_id());
        if (patient == null) {
            throw new WHPDomainException("Invalid case-id. No such patient.");
        }

        Patient updatedPatient = mapUpdates(patientRequest, patient);
        allPatients.update(updatedPatient);
    }

    public void startOnTreatment(String patientId) {
        Patient patient = allPatients.findByPatientId(patientId);
        patient.getCurrentProvidedTreatment().getTreatment().setDoseStartDate(DateUtil.today());
        allPatients.update(patient);
    }

    public void performTreatmentUpdate(TreatmentUpdateRequest treatmentUpdateRequest) {
        Patient patient = allPatients.findByPatientId(treatmentUpdateRequest.getCase_id());
        if (patient == null) {
            throw new WHPDomainException("Invalid case-id. No such patient.");
        }
        switch (treatmentUpdateRequest.getTreatment_update()) {
            case NewTreatment:
                if (updateTreatmentCriteria.canOpenNewTreatment(treatmentUpdateRequest)) {
                    addNewTreatmentForCategoryChange(treatmentUpdateRequest, patient);
                } else {
                    throw new WHPDomainException("Cannot open new treatment for this case: either case does not have any current treatment or current treatment is not closed.");
                }
                break;
            case CloseTreatment:
                if (updateTreatmentCriteria.canCloseCurrentTreatment(treatmentUpdateRequest)){
                    closeCurrentTreatment(patient, treatmentUpdateRequest);
                } else {
                    throw new WHPDomainException("Cannot close current treatment for case: either wrong tb id, there is no current treatment or it is already closed.");
                }
        }
    }

    private void addNewTreatmentForCategoryChange(TreatmentUpdateRequest treatmentUpdateRequest, Patient patient) {
        Treatment newTreatment = createNewTreatmentFrom(patient, treatmentUpdateRequest);
        allTreatments.add(newTreatment);
        ProvidedTreatment newProvidedTreatment = createNewProvidedTreatmentForTreatmentCategoryChange(patient, treatmentUpdateRequest, newTreatment);
        patient.addProvidedTreatment(newProvidedTreatment);
        patient.setLastModifiedDate(treatmentUpdateRequest.getDate_modified());
        allPatients.update(patient);
    }

    private void closeCurrentTreatment(Patient patient, TreatmentUpdateRequest treatmentUpdateRequest) {
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
