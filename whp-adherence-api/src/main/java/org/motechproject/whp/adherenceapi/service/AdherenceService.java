package org.motechproject.whp.adherenceapi.service;

import org.joda.time.LocalDate;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.adherenceapi.response.AdherenceValidationResponse;
import org.motechproject.whp.adherenceapi.response.AdherenceValidationResponseBuilder;
import org.motechproject.whp.adherenceapi.response.AdherenceCaptureFlashingResponse;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.on;
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

    public AdherenceCaptureFlashingResponse adherenceSummary(String providerId, LocalDate today) {
        List<String> patientsWithAdherence = whpAdherenceService.patientsWithAdherence(providerId, week(today));
        List<Patient> patientsForProvider = patientService.getAllWithActiveTreatmentForProvider(providerId);

        return new AdherenceCaptureFlashingResponse(patientsWithAdherence,
                extract(patientsForProvider, on(Patient.class).getPatientId())
        );
    }

    public AdherenceValidationResponse validateDosage(String patientId, String doseTakenCount) {
        Patient patient = patientService.findByPatientId(patientId);

        AdherenceValidationResponseBuilder responseBuilder = new AdherenceValidationResponseBuilder();

        if (patient.isValidDose(parseInt(doseTakenCount)))
            return responseBuilder.successfulResponse();
        return responseBuilder.failureResponse(patient.getTreatmentCategory());
    }
}
