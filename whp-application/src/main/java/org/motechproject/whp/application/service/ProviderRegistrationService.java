package org.motechproject.whp.application.service;

import org.motechproject.whp.domain.Patient;
import org.motechproject.whp.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProviderRegistrationService {

    private PatientService patientService;

    @Autowired
    public ProviderRegistrationService(PatientService patientService) {
        this.patientService = patientService;
    }

    public void register(Patient patient) {
        patientService.add(patient);
    }

}
