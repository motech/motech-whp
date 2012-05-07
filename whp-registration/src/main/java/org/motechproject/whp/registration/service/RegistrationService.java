package org.motechproject.whp.registration.service;

import org.motechproject.security.service.MotechAuthenticationService;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.contract.ProviderRequest;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.patient.service.ProviderService;
import org.motechproject.whp.refdata.domain.WHPRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class RegistrationService {

    MotechAuthenticationService motechAuthenticationService;

    PatientService patientService;

    ProviderService providerService;

    @Autowired
    public RegistrationService(ProviderService providerService, PatientService patientService, MotechAuthenticationService motechAuthenticationService) {
        this.providerService = providerService;
        this.patientService = patientService;
        this.motechAuthenticationService = motechAuthenticationService;
    }

    public void registerPatient(PatientRequest patientRequest) {
        patientService.createPatient(patientRequest);
    }

    public void registerProvider(ProviderRequest providerRequest) {
        String providerDocId = providerService.add(providerRequest.getProviderId(),
                providerRequest.getPrimaryMobile(),
                providerRequest.getSecondaryMobile(),
                providerRequest.getTertiaryMobile(),
                providerRequest.getDistrict(),
                providerRequest.getLastModifiedDate());
        motechAuthenticationService.register(providerRequest.getProviderId(), "password", providerDocId, Arrays.asList(WHPRole.PROVIDER.name()));
    }
}
