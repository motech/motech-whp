package org.motechproject.whp.adherenceapi.validator;

import org.joda.time.LocalDate;
import org.motechproject.whp.adherence.service.AdherenceWindow;
import org.motechproject.whp.adherenceapi.request.AdherenceCaptureFlashingRequest;
import org.motechproject.whp.adherenceapi.request.AdherenceValidationRequest;
import org.motechproject.whp.common.error.ErrorWithParameters;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.user.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdherenceRequestsValidator {

    private ProviderService providerService;
    private AdherenceWindow adherenceWindow;
    private PatientService patientService;

    @Autowired
    public AdherenceRequestsValidator(ProviderService providerService, AdherenceWindow adherenceWindow, PatientService patientService) {
        this.providerService = providerService;
        this.adherenceWindow = adherenceWindow;
        this.patientService = patientService;
    }

    public ErrorWithParameters validateFlashingRequest(AdherenceCaptureFlashingRequest flashingRequest, LocalDate requestDate) {
        if(!validateMobileNumber(flashingRequest.getMsisdn()))
            return new ErrorWithParameters(AdherenceCaptureError.INVALID_MOBILE_NUMBER.name());

        if(!validateAdherenceDay(requestDate))
            return new ErrorWithParameters(AdherenceCaptureError.NON_ADHERENCE_DAY.name());

        return null;
    }

    public ErrorWithParameters validateValidationRequest(AdherenceValidationRequest validationRequest) {
        if(!validateMobileNumber(validationRequest.getMsisdn()))
            return new ErrorWithParameters(AdherenceCaptureError.INVALID_MOBILE_NUMBER.name());

        if(!validatePatient(validationRequest.getPatientId()))
            return new ErrorWithParameters(AdherenceCaptureError.INVALID_PATIENT.name());

        return null;
    }

    private boolean validatePatient(String patientId) {
        return patientService.findByPatientId(patientId) != null;
    }

    private Boolean validateAdherenceDay(LocalDate requestDate) {
        return adherenceWindow.isValidAdherenceDay(requestDate);
    }

    private Boolean validateMobileNumber(String msisdn) {
        return (providerService.findByMobileNumber(msisdn) != null);
    }
}
