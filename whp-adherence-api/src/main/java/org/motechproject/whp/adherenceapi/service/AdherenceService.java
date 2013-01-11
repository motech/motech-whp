package org.motechproject.whp.adherenceapi.service;

import org.motechproject.whp.adherence.domain.AdherenceSummaryByProvider;
import org.motechproject.whp.adherence.service.AdherenceDataService;
import org.motechproject.whp.adherenceapi.domain.Dosage;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdherenceService {

    private PatientService patientService;
    private AdherenceDataService adherenceDataService;

    @Autowired
    public AdherenceService(PatientService patientService, AdherenceDataService adherenceDataService) {
        this.patientService = patientService;
        this.adherenceDataService = adherenceDataService;
    }

    public AdherenceSummaryByProvider adherenceSummary(String providerId) {
        return adherenceDataService.getAdherenceSummary(providerId);
    }

    public Dosage dosageForPatient(String patientId) {
        Patient patient = patientService.findByPatientId(patientId);
        return (patient == null) ? null : new Dosage(patient);
    }
}
