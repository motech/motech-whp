package org.motechproject.whp.adherenceapi.adherence;

import org.motechproject.whp.adherenceapi.domain.AdherenceCaptureStatus;
import org.motechproject.whp.adherenceapi.domain.ProviderId;
import org.motechproject.whp.adherenceapi.reporting.AdherenceCaptureReportRequest;
import org.motechproject.whp.adherenceapi.request.AdherenceValidationRequest;
import org.motechproject.whp.adherenceapi.response.validation.AdherenceValidationResponse;
import org.motechproject.whp.adherenceapi.validator.AdherenceValidationRequestValidator;
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.motechproject.whp.reports.contract.AdherenceCaptureRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.motechproject.whp.adherenceapi.domain.AdherenceCaptureStatus.INVALID;
import static org.motechproject.whp.adherenceapi.domain.AdherenceCaptureStatus.VALID;

@Service
public class AdherenceValidationOverIVR {

    private ReportingPublisherService reportingService;
    private AdherenceValidationRequestValidator adherenceValidationRequestValidator;

    @Autowired
    public AdherenceValidationOverIVR(ReportingPublisherService reportingService, AdherenceValidationRequestValidator adherenceValidationRequestValidator) {
        this.reportingService = reportingService;
        this.adherenceValidationRequestValidator = adherenceValidationRequestValidator;
    }

    public AdherenceValidationResponse handleValidationRequest(AdherenceValidationRequest request, ProviderId providerId) {
        AdherenceValidationResponse response = validateAdherenceInput(request, providerId);
        reportAdherenceValidation(request, response, providerId);
        return response;
    }

    protected AdherenceValidationResponse validateAdherenceInput(AdherenceValidationRequest adherenceValidationRequest, ProviderId providerId) {
        return adherenceValidationRequestValidator.validate(adherenceValidationRequest, providerId);
    }

    private void reportAdherenceValidation(AdherenceValidationRequest request, AdherenceValidationResponse response, ProviderId providerId) {
        AdherenceCaptureStatus status = response.failed() ? INVALID : VALID;
        AdherenceCaptureRequest reportingRequest = new AdherenceCaptureReportRequest(
                request,
                providerId,
                !response.failed(),
                status).request();
        reportingService.reportAdherenceCapture(reportingRequest);
    }
}
