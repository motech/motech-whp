package org.motechproject.whp.adherence.service;

import org.motechproject.whp.adherence.domain.AdherenceSummaryByProvider;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.repository.AllPatients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class AdherenceDataService {

    private WHPAdherenceService whpAdherenceService;
    private AllPatients allPatients;

    @Autowired
    public AdherenceDataService(WHPAdherenceService whpAdherenceService, AllPatients allPatients) {
        this.whpAdherenceService = whpAdherenceService;
        this.allPatients = allPatients;
    }

    public AdherenceSummaryByProvider getAdherenceSummary(String providerId) {
        List<Patient> patients = allPatients.getAllWithActiveTreatmentFor(providerId);
        return new AdherenceSummaryByProvider(providerId, patients);
    }
}
