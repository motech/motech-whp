package org.motechproject.whp.adherenceapi.validator;

import org.motechproject.whp.adherenceapi.domain.ProviderId;
import org.motechproject.whp.adherenceapi.errors.AdherenceErrors;
import org.motechproject.whp.adherenceapi.request.AdherenceValidationRequest;
import org.motechproject.whp.adherenceapi.response.validation.AdherenceValidationResponse;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdherenceNotCapturedRequestValidator {
    private AdherenceRequestValidator adherenceRequestValidator;
    private PatientService patientService;

    @Autowired
    public AdherenceNotCapturedRequestValidator(AdherenceRequestValidator adherenceRequestValidator, PatientService patientService) {
        this.adherenceRequestValidator = adherenceRequestValidator;
        this.patientService = patientService;
    }

    public AdherenceValidationResponse validate(AdherenceValidationRequest request, ProviderId providerId) {
        Patient patient = patientService.findByPatientId(request.getPatientId());
        AdherenceErrors errors = adherenceRequestValidator.validatePatientProviderMapping(providerId, patient);
        if (errors.isNotEmpty())
            return new AdherenceValidationResponse().failure(errors.errorMessage());
        return new AdherenceValidationResponse().success();
    }
}
