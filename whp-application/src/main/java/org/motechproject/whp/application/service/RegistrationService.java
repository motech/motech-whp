package org.motechproject.whp.application.service;

import org.motechproject.whp.patient.contract.CreatePatientRequest;
import org.motechproject.whp.patient.contract.CreateProviderRequest;
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

    public void registerPatient(CreatePatientRequest createPatientRequest) {
        patientService.add(createPatientRequest);
    }

    public void registerProvider(CreateProviderRequest createProviderRequest) {
        providerService.add(createProviderRequest);
    }

}
