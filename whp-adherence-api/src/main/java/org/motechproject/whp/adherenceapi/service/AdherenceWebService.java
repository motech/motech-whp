package org.motechproject.whp.adherenceapi.service;

import ch.lambdaj.Lambda;
import org.joda.time.LocalDate;
import org.motechproject.whp.adherenceapi.domain.AdherenceSummary;
import org.motechproject.whp.adherenceapi.reporting.AdherenceFlashingReportRequest;
import org.motechproject.whp.adherenceapi.request.AdherenceFlashingRequest;
import org.motechproject.whp.adherenceapi.request.AdherenceValidationRequest;
import org.motechproject.whp.adherenceapi.response.flashing.AdherenceFlashingResponse;
import org.motechproject.whp.adherenceapi.response.validation.AdherenceValidationResponse;
import org.motechproject.whp.adherenceapi.response.validation.AdherenceValidationResponseBuilder;
import org.motechproject.whp.adherenceapi.validator.AdherenceRequestsValidator;
import org.motechproject.whp.common.error.ErrorWithParameters;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.motechproject.whp.user.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static ch.lambdaj.Lambda.on;

@Service
public class AdherenceWebService {

    private AdherenceService adherenceService;
    private ReportingPublisherService reportingPublishingService;
    private final AdherenceRequestsValidator adherenceRequestsValidator;
    private final ProviderService providerService;
    private AdherenceValidationResponseBuilder adherenceValidationResponseBuilder;

    @Autowired
    public AdherenceWebService(AdherenceService adherenceService, ReportingPublisherService reportingPublishingService,
                               AdherenceRequestsValidator adherenceRequestsValidator, ProviderService providerService, AdherenceValidationResponseBuilder adherenceValidationResponseBuilder) {
        this.adherenceService = adherenceService;
        this.reportingPublishingService = reportingPublishingService;
        this.adherenceRequestsValidator = adherenceRequestsValidator;
        this.providerService = providerService;
        this.adherenceValidationResponseBuilder = adherenceValidationResponseBuilder;
    }

    public AdherenceFlashingResponse processFlashingRequest(AdherenceFlashingRequest adherenceFlashingRequest, LocalDate requestedDate) {
        ErrorWithParameters error = adherenceRequestsValidator.validateFlashingRequest(adherenceFlashingRequest, requestedDate);
        if(error == null) {
            String providerId = providerService.findByMobileNumber(adherenceFlashingRequest.getMsisdn()).getProviderId();
            AdherenceSummary adherenceSummary = adherenceService.adherenceSummary(providerId, requestedDate);

            List<String> patientsForProvider = Lambda.extract(adherenceSummary.getPatientsUnderProvider(), on(Patient.class).getPatientId());
            reportingPublishingService.reportFlashingRequest(new AdherenceFlashingReportRequest(adherenceFlashingRequest, providerId).flashingLogRequest());
            return new AdherenceFlashingResponse(adherenceSummary.getPatientsWithAdherence(), patientsForProvider);
        }
        return AdherenceFlashingResponse.failureResponse(error.getCode());
    }

    public AdherenceValidationResponse processValidationRequest(AdherenceValidationRequest adherenceValidationRequest) {
        ErrorWithParameters errors = adherenceRequestsValidator.validateValidationRequest(adherenceValidationRequest);
        if(errors == null) {
            if(adherenceService.validateDosage(adherenceValidationRequest.getPatientId(), adherenceValidationRequest.getDoseTakenCount()))
                return adherenceValidationResponseBuilder.successfulResponse();
            return adherenceValidationResponseBuilder.invalidDosageFailureResponse(adherenceService.getTreatmentCategoryInformation(adherenceValidationRequest.getPatientId()));
        }
        return adherenceValidationResponseBuilder.validationFailureResponse();
    }
}
