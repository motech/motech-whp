package org.motechproject.whp.adherenceapi.service;

import org.joda.time.LocalDate;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.adherenceapi.domain.AdherenceSummary;
import org.motechproject.whp.adherenceapi.domain.Dosage;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.motechproject.whp.common.domain.TreatmentWeekInstance.week;

@Service
public class AdherenceService {

    private WHPAdherenceService whpAdherenceService;
    private PatientService patientService;

    @Autowired
    public AdherenceService(WHPAdherenceService whpAdherenceService, PatientService patientService) {
        this.whpAdherenceService = whpAdherenceService;
        this.patientService = patientService;
    }

    public AdherenceSummary adherenceSummary(String providerId, LocalDate today) {
        return new AdherenceSummary(
                whpAdherenceService.patientsWithAdherence(week(today)),
                patientService.getAllWithActiveTreatmentForProvider(providerId)
        );
    }

    public Dosage dosageForPatient(String patientId) {
        Patient patient = patientService.findByPatientId(patientId);
        return (patient == null) ? null : new Dosage(patient);
    }
}
