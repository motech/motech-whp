package org.motechproject.whp.adherenceapi.adherence;

import org.motechproject.whp.adherenceapi.domain.AdherenceCaptureStatus;
import org.motechproject.whp.adherenceapi.domain.ProviderId;
import org.motechproject.whp.adherenceapi.reporting.AdherenceCaptureReportRequest;
import org.motechproject.whp.adherenceapi.request.AdherenceNotCapturedRequest;
import org.motechproject.whp.adherenceapi.response.validation.AdherenceValidationResponse;
import org.motechproject.whp.adherenceapi.validator.AdherenceNotCapturedRequestValidator;
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.motechproject.whp.reports.contract.AdherenceCaptureRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdherenceNotCapturedOverIVR {

    private ReportingPublisherService reportingService;
    private AdherenceNotCapturedRequestValidator adherenceNotCapturedRequestValidator;

    @Autowired
    public AdherenceNotCapturedOverIVR(ReportingPublisherService reportingService, AdherenceNotCapturedRequestValidator adherenceNotCapturedRequestValidator) {
        this.adherenceNotCapturedRequestValidator = adherenceNotCapturedRequestValidator;
        this.reportingService = reportingService;
    }

    public AdherenceValidationResponse recordNotCaptured(AdherenceNotCapturedRequest adherenceNotCapturedRequest, ProviderId providerId) {
        AdherenceValidationResponse response = adherenceNotCapturedRequestValidator.validate(adherenceNotCapturedRequest.validationRequest(), providerId);
        if(!response.failed())
            publishAdherenceSkipEvent(adherenceNotCapturedRequest, providerId, response);
        return response;
    }

    private void publishAdherenceSkipEvent(AdherenceNotCapturedRequest request, ProviderId providerId, AdherenceValidationResponse response) {
        AdherenceCaptureRequest adherenceCaptureRequest = new AdherenceCaptureReportRequest(request.validationRequest(), providerId, !response.failed(), AdherenceCaptureStatus.valueOf(request.getAdherenceCaptureStatus())).request();
        reportingService.reportAdherenceCapture(adherenceCaptureRequest);
    }
}
