package org.motechproject.whp.patient.service;

import org.motechproject.util.DateUtil;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Treatment;
import org.motechproject.whp.patient.exception.WHPDomainException;
import org.motechproject.whp.patient.mapper.PatientMapper;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTreatments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatientService {

    private AllTreatments allTreatments;
    private AllPatients allPatients;

    @Autowired
    public PatientService(AllPatients allPatients, AllTreatments allTreatments) {
        this.allPatients = allPatients;
        this.allTreatments = allTreatments;
    }

    public void add(PatientRequest patientRequest) {
        Patient patient = new PatientMapper().map(patientRequest);
        Treatment treatment = patient.latestProvidedTreatment().getTreatment();

        allPatients.add(patient);
        allTreatments.add(treatment);

        patient.latestProvidedTreatment().setTreatmentDocId(treatment.getId());

        allPatients.update(patient);
    }

    public void simpleUpdate(PatientRequest patientRequest) {
        Patient patient = allPatients.findByPatientId(patientRequest.getCase_id());
        if (patient == null) {
            throw new WHPDomainException("Invalid case-id. No such patient.");
        }

        Patient updatedPatient = new PatientMapper().mapUpdates(patientRequest, patient);
        allPatients.update(updatedPatient);
    }

    public void startOnTreatment(String patientId) {
        Patient patient = allPatients.findByPatientId(patientId);
        patient.getCurrentProvidedTreatment().getTreatment().setDoseStartDate(DateUtil.today());
        allPatients.update(patient);
    }
}
