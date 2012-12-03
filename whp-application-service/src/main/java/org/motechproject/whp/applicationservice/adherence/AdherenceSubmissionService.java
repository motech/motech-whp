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

    public List<Provider> providersPendingAdherence(String district, LocalDate asOf) {
        ProviderIds providersWithAdherence = providersIdsWithAdherence(district, asOf);
        ProviderIds providersWithActivePatients = providerIdsWithActivePatients(district);
        return providerService.findByProviderIds(providersWithActivePatients.subtract(providersWithAdherence));
    }

    public List<Provider> providersWithAdherence(String district, LocalDate asOf) {
        return providerService.findByProviderIds(providersIdsWithAdherence(district, asOf));
    }

    private ProviderIds providersIdsWithAdherence(String district, LocalDate asOf) {
        return adherenceLogService.providersWithAdherenceRecords(asOf, providerIdsWithActivePatients(district));
    }

    private ProviderIds providerIdsWithActivePatients(String district) {
        ProviderIds providersInDistrict = providerService.findByDistrict(district);
        return patientService.providersWithActivePatients(providersInDistrict);
    }
}
