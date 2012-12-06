package org.motechproject.whp.adherenceapi.service;

import ch.lambdaj.Lambda;
import org.joda.time.LocalDate;
import org.motechproject.whp.adherenceapi.domain.AdherenceSummary;
import org.motechproject.whp.adherenceapi.reporting.AdherenceFlashingRequest;
import org.motechproject.whp.adherenceapi.request.AdherenceCaptureFlashingRequest;
import org.motechproject.whp.adherenceapi.response.AdherenceCaptureFlashingResponse;
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

    public static final String ERROR_MESSAGE_SEPARATOR = ",";
    private AdherenceService adherenceService;
    private ReportingPublisherService reportingPublishingService;
    private final AdherenceRequestsValidator adherenceRequestsValidator;
    private final ProviderService providerService;

    @Autowired
    public AdherenceWebService(AdherenceService adherenceService, ReportingPublisherService reportingPublishingService, AdherenceRequestsValidator adherenceRequestsValidator, ProviderService providerService) {
        this.adherenceService = adherenceService;
        this.reportingPublishingService = reportingPublishingService;
        this.adherenceRequestsValidator = adherenceRequestsValidator;
        this.providerService = providerService;
    }

    public AdherenceCaptureFlashingResponse processFlashingRequest(AdherenceCaptureFlashingRequest adherenceCaptureFlashingRequest, LocalDate requestedDate) {
        ErrorWithParameters error = adherenceRequestsValidator.validateFlashingRequest(adherenceCaptureFlashingRequest, requestedDate);
        if(error == null) {
            String providerId = providerService.findByMobileNumber(adherenceCaptureFlashingRequest.getMsisdn()).getProviderId();
            AdherenceSummary adherenceSummary = adherenceService.adherenceSummary(providerId, requestedDate);

            List<String> patientsForProvider = Lambda.extract(adherenceSummary.getPatientsForProvider(), on(Patient.class).getPatientId());
            reportingPublishingService.reportFlashingRequest(new AdherenceFlashingRequest(adherenceCaptureFlashingRequest, providerId).flashingLogRequest());
            return new AdherenceCaptureFlashingResponse(adherenceSummary.getPatientsWithAdherence(), patientsForProvider);
        }
        return AdherenceCaptureFlashingResponse.failureResponse(error.getCode());
    }
}
