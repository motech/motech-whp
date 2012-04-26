package org.motechproject.whp.patient.service;

import org.motechproject.whp.patient.contract.CreatePatientRequest;
import org.motechproject.whp.patient.domain.Patient;
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

}
