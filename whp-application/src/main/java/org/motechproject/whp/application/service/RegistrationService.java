package org.motechproject.whp.application.service;

import org.motechproject.whp.domain.Patient;
import org.motechproject.whp.domain.Provider;
import org.motechproject.whp.service.PatientService;
import org.motechproject.whp.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    PatientService patientService;

    ProviderService providerService;

    @Autowired
    public RegistrationService(ProviderService providerService, PatientService patientService) {
        this.providerService = providerService;
        this.patientService = patientService;
    }

    public void registerPatient(Patient patient) {
        patientService.add(patient);
    }

    public void registerProvider(Provider provider) {
        providerService.add(provider);
    }

}
