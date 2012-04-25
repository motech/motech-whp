package org.motechproject.whp.service;

import org.motechproject.whp.domain.Patient;
import org.motechproject.whp.exception.WHPDomainException;
import org.motechproject.whp.repository.AllPatients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatientService {

    AllPatients allPatients;

    @Autowired
    public PatientService(AllPatients allPatients) {
        this.allPatients = allPatients;
    }

    public void add(Patient patient) {
        Patient savedPatient = allPatients.findByPatientId(patient.getPatientId());
        if (savedPatient == null)
            allPatients.add(patient);
        else throw new WHPDomainException("Patient already present");
    }

}
