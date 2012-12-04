package org.motechproject.whp.adherenceapi.service;

import org.joda.time.LocalDate;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.adherenceapi.request.AdherenceCaptureFlashingResponse;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.on;
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

    public AdherenceCaptureFlashingResponse adherenceSubmissionInformation(String providerId, LocalDate today) {
        List<String> patientsWithAdherence = whpAdherenceService.patientsWithAdherence(providerId, week(today));
        List<Patient> patientsForProvider = patientService.getAllWithActiveTreatmentForProvider(providerId);

        return new AdherenceCaptureFlashingResponse(patientsWithAdherence,
                extract(patientsForProvider, on(Patient.class).getPatientId())
        );
    }
}
