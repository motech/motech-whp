package org.motechproject.whp.patient.service;

import org.joda.time.LocalDate;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.contract.TreatmentUpdateRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.ProvidedTreatment;
import org.motechproject.whp.patient.domain.Treatment;
import org.motechproject.whp.patient.domain.criteria.CriteriaErrors;
import org.motechproject.whp.patient.exception.WHPDomainException;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTreatments;
import org.motechproject.whp.patient.service.treatmentupdate.TreatmentUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.motechproject.whp.patient.domain.criteria.UpdatePatientCriteria.canPerformSimpleUpdate;
import static org.motechproject.whp.patient.mapper.PatientMapper.*;

@Service
public class PatientService {

    private AllTreatments allTreatments;
    private AllPatients allPatients;

    private final String CANNOT_SIMPLE_UPDATE = "Cannot update details for this case: ";

    @Autowired
    public PatientService(AllPatients allPatients, AllTreatments allTreatments) {
        this.allPatients = allPatients;
        this.allTreatments = allTreatments;
    }

    public void createPatient(PatientRequest patientRequest) {
        Patient patient = mapBasicInfo(patientRequest);
        Treatment treatment = createTreatment(patientRequest);

        ProvidedTreatment providedTreatment = mapProvidedTreatment(patientRequest, treatment);
        patient.addProvidedTreatment(providedTreatment, patientRequest.getDate_modified());
        allPatients.add(patient);
    }

    public void simpleUpdate(PatientRequest patientRequest) {
        Patient patient = allPatients.findByPatientId(patientRequest.getCase_id());
        CriteriaErrors criteriaErrors = new CriteriaErrors();
        if(canPerformSimpleUpdate(patient, patientRequest, criteriaErrors)){
            Patient updatedPatient = mapUpdates(patientRequest, patient);
            allPatients.update(updatedPatient);
        } else {
            throw new WHPDomainException(CANNOT_SIMPLE_UPDATE + criteriaErrors);
        }
    }

    public void startOnTreatment(String patientId, LocalDate firstDoseTakenDate) {
        Patient patient = allPatients.findByPatientId(patientId);
        patient.getCurrentProvidedTreatment().getTreatment().setDoseStartDate(firstDoseTakenDate);
        allPatients.update(patient);
    }

    public void performTreatmentUpdate(TreatmentUpdateRequest treatmentUpdateRequest) {
        TreatmentUpdate treatmentUpdate = treatmentUpdateRequest.getTreatment_update().getUpdateScenario();
        try{
            treatmentUpdate.apply(allPatients, allTreatments, treatmentUpdateRequest);
        } catch (WHPDomainException e) {
            throw new WHPDomainException(e.getMessage());
        }
    }

    private Treatment createTreatment(PatientRequest patientRequest) {
        Treatment treatment = mapTreatmentInfo(patientRequest);
        allTreatments.add(treatment);
        return treatment;
    }
}
