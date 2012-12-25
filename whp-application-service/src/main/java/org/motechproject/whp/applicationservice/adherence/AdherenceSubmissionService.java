package org.motechproject.whp.applicationservice.adherence;

import org.joda.time.LocalDate;
import org.motechproject.whp.adherence.service.AdherenceLogService;
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
    private AdherenceLogService adherenceLogService;

    @Autowired
    public AdherenceSubmissionService(ProviderService providerService, PatientService patientService, AdherenceLogService adherenceLogService) {
        this.providerService = providerService;
        this.patientService = patientService;
        this.adherenceLogService = adherenceLogService;
    }

    public List<Provider> providersToSubmitAdherence() {
        ProviderIds providerIds = patientService.providersWithActivePatients();
        return providerService.findByProviderIds(providerIds);
    }

    public List<Provider> providersPendingAdherence(String district, LocalDate from, LocalDate to) {
        ProviderIds providersWithAdherence = providersIdsWithAdherence(district, from, to);
        ProviderIds providersWithActivePatients = providerIdsWithActivePatients(district);
        return providerService.findByProviderIds(providersWithActivePatients.subtract(providersWithAdherence));
    }


    public List<Provider> providersWithAdherence(String district, LocalDate from, LocalDate to) {
        return providerService.findByProviderIds(providersIdsWithAdherence(district, from, to));
    }

    private ProviderIds providersIdsWithAdherence(String district, LocalDate from, LocalDate to) {
        ProviderIds providersInDistrict = providerIdsWithActivePatients(district);
        return adherenceLogService.providersWithAdherence(providersInDistrict, from, to);
    }

    private ProviderIds providerIdsWithActivePatients(String district) {
        ProviderIds providersInDistrict = providerService.findByDistrict(district);
        return patientService.providersWithActivePatients(providersInDistrict);
    }

    public List<Provider> providersPendingAdherence(LocalDate from, LocalDate to) {
        ProviderIds providersWithActivePatients = patientService.providersWithActivePatients();
        ProviderIds providerIdsWithAdherence = adherenceLogService.providersWithAdherence(from, to);
        return providerService.findByProviderIds(providersWithActivePatients.subtract(providerIdsWithAdherence));
    }
}
