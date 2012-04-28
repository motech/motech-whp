package org.motechproject.whp.application.service;

import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.contract.ProviderRequest;
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

    public void registerPatient(PatientRequest patientRequest) {
        patientService.add(patientRequest);
    }

    public void registerProvider(ProviderRequest providerRequest) {
        providerService.add(providerRequest.getProviderId(),
                providerRequest.getPrimaryMobile(),
                providerRequest.getSecondaryMobile(),
                providerRequest.getTertiaryMobile(),
                providerRequest.getDistrict(),
                providerRequest.getLastModifiedDate());
    }
}
