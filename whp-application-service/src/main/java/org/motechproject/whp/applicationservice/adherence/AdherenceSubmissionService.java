package org.motechproject.whp.applicationservice.adherence;

import org.joda.time.LocalDate;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.domain.ProviderIds;
import org.motechproject.whp.user.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdherenceSubmissionService {

    private ProviderService providerService;
    private PatientService patientService;

    @Autowired
    public AdherenceSubmissionService(ProviderService providerService, PatientService patientService) {
        this.providerService = providerService;
        this.patientService = patientService;
    }

    public List<Provider> providersToSubmitAdherence() {
        ProviderIds providerIds = patientService.providersWithActivePatients();
        return providerService.findByProviderIds(providerIds);
    }

    public List<Provider> providersPendingAdherence(LocalDate asOf) {
        return providerService.findByProviderIds(patientService.getAllProvidersWithPendingAdherence(asOf));
    }
}
