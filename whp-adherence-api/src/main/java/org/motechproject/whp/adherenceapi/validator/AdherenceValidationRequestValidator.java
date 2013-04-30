package org.motechproject.whp.adherenceapi.validator;

import org.apache.commons.lang.StringUtils;
import org.motechproject.whp.adherenceapi.domain.Dosage;
import org.motechproject.whp.adherenceapi.domain.ProviderId;
import org.motechproject.whp.adherenceapi.errors.AdherenceErrors;
import org.motechproject.whp.adherenceapi.request.AdherenceValidationRequest;
import org.motechproject.whp.adherenceapi.response.validation.AdherenceValidationResponse;
import org.motechproject.whp.adherenceapi.service.AdherenceService;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdherenceValidationRequestValidator {
    private AdherenceService adherenceService;
    private AdherenceRequestValidator adherenceRequestValidator;
    private PatientService patientService;

    @Autowired
    public AdherenceValidationRequestValidator(AdherenceService adherenceService, AdherenceRequestValidator adherenceRequestValidator, PatientService patientService) {
        this.adherenceService = adherenceService;
        this.adherenceRequestValidator = adherenceRequestValidator;
        this.patientService = patientService;
    }

    public AdherenceValidationResponse validate(AdherenceValidationRequest request, ProviderId providerId) {
        Patient patient = patientService.findByPatientId(request.getPatientId());
        AdherenceErrors errors = adherenceRequestValidator.validatePatientProviderMapping(providerId, patient);
        Dosage dosage = adherenceService.dosageForPatient(patient);
        if (errors.isNotEmpty()) {
            return new AdherenceValidationResponse(dosage).failure(errors.errorMessage());
        } else if (isValidDose(request, dosage)) {
            return new AdherenceValidationResponse(dosage).success();
        } else {
            return new AdherenceValidationResponse(dosage).invalidAdherenceRange();
        }
    }

    private boolean isValidDose(AdherenceValidationRequest adherenceValidationRequest, Dosage dosage) {
        String doseTakenCount = adherenceValidationRequest.getDoseTakenCount();
        return StringUtils.isNumeric(doseTakenCount) && dosage.isValidInput(Integer.parseInt(doseTakenCount));
    }
}
