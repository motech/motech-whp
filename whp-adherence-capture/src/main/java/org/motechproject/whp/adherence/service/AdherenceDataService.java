package org.motechproject.whp.adherence.service;

import org.motechproject.whp.adherence.domain.AdherenceSummaryByProvider;
import org.motechproject.whp.common.domain.TreatmentWeekInstance;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.user.repository.AllProviders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.on;

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
        List<String> patientIds = extract(patients, on(Patient.class).getPatientId());
        List<String> patientsWithAdherence = whpAdherenceService.patientsWithAdherence(providerId, TreatmentWeekInstance.currentWeekInstance());
        return new AdherenceSummaryByProvider(providerId, patientIds, patientsWithAdherence);
    }
}
