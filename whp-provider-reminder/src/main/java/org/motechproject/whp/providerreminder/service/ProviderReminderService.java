package org.motechproject.whp.providerreminder.service;

import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.domain.ProviderIds;
import org.motechproject.whp.user.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.on;

@Service
public class ProviderReminderService {

    private final ProviderService providerService;
    private final PatientService patientService;

    @Autowired
    public ProviderReminderService(ProviderService providerService, PatientService patientService) {
        this.providerService = providerService;
        this.patientService = patientService;
    }

    public List<String> getActiveProviderPhoneNumbers() {
        ProviderIds providerIds = patientService.providersWithActivePatients();
        List<Provider> providers = providerService.findByProviderIds(providerIds);

        return extract(providers, on(Provider.class).getPrimaryMobile());
    }
}
