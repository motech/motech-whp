package org.motechproject.whp.patient.service;

import org.motechproject.whp.patient.contract.CreatePatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.ProvidedTreatment;
import org.motechproject.whp.patient.domain.Treatment;
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

    public void add(CreatePatientRequest patientRequest) {
        Patient patient = new PatientMapper().map(patientRequest);
        Treatment treatment = patient.latestProvidedTreatment().getTreatment();

        allPatients.add(patient);
        allTreatments.add(treatment);
        patient.latestProvidedTreatment().setTreatmentDocId(treatment.getId());
        allPatients.update(patient);
    }

    public void simpleUpdate(CreatePatientRequest createPatientRequest) {
        Patient patient = allPatients.findByPatientId(createPatientRequest.getCaseId());
        ProvidedTreatment currentProvidedTreatment = patient.getCurrentProvidedTreatment();
        Treatment currentTreatment = currentProvidedTreatment.getTreatment();

        patient.setPhoneNumber(createPatientRequest.getMobileNumber());
        currentProvidedTreatment.setPatientAddress(createPatientRequest.getAddress());
        currentTreatment.addSmearTestResult(createPatientRequest.getSmearTestResults());
        currentTreatment.addWeightStatistics(createPatientRequest.getWeightStatistics());
        currentTreatment.setTbRegistrationNumber(createPatientRequest.getTbRegistrationNumber());

        allPatients.update(patient);
    }
}
