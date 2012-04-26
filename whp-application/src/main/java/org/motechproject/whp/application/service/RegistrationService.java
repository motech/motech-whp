package org.motechproject.whp.application.service;

import org.motechproject.whp.patient.contract.CreatePatientRequest;
import org.motechproject.whp.patient.domain.Provider;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.patient.service.ProviderService;
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

    public void registerPatient(CreatePatientRequest patientRequest) {
        patientService.add(patientRequest);
    }

    public void registerProvider(Provider provider) {
        providerService.add(provider);
    }

}
