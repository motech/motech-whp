package org.motechproject.whp.adherenceapi.service;

import org.joda.time.LocalDate;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.adherenceapi.domain.AdherenceSummary;
import org.motechproject.whp.adherenceapi.domain.TreatmentCategoryInfo;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.Integer.parseInt;
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
        List<String> patientsWithAdherence = whpAdherenceService.patientsWithAdherence(providerId, week(today));
        List<Patient> patientsForProvider = patientService.getAllWithActiveTreatmentForProvider(providerId);

        return new AdherenceSummary(patientsWithAdherence,patientsForProvider);
    }

    public Boolean validateDosage(String patientId, String doseTakenCount) {
        Patient patient = patientService.findByPatientId(patientId);

        return patient != null && patient.isValidDose(parseInt(doseTakenCount));
    }

    public TreatmentCategoryInfo getTreatmentCategoryInformation(String patientId) {
        Patient patient = patientService.findByPatientId(patientId);

        if(patient == null)
            return null;
        return new TreatmentCategoryInfo(patient.getTreatmentCategory());
    }
}
