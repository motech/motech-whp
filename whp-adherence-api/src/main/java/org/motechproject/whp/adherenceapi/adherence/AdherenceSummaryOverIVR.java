package org.motechproject.whp.adherenceapi.adherence;


import org.joda.time.LocalDate;
import org.motechproject.whp.adherence.service.AdherenceWindow;
import org.motechproject.whp.adherenceapi.domain.AdherenceSummary;
import org.motechproject.whp.adherenceapi.domain.ProviderId;
import org.motechproject.whp.adherenceapi.errors.FlashingRequestErrors;
import org.motechproject.whp.adherenceapi.reporting.AdherenceFlashingReportRequest;
import org.motechproject.whp.adherenceapi.request.AdherenceFlashingRequest;
import org.motechproject.whp.adherenceapi.response.flashing.AdherenceFlashingResponse;
import org.motechproject.whp.adherenceapi.service.AdherenceService;
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.motechproject.whp.reports.contract.FlashingLogRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.motechproject.util.DateUtil.today;
import static org.motechproject.whp.adherenceapi.response.flashing.AdherenceFlashingResponse.failureResponse;
import static org.motechproject.whp.adherenceapi.response.flashing.AdherenceFlashingResponse.successResponse;

@Service
public class AdherenceSummaryOverIVR {

    private AdherenceService adherenceService;
    private AdherenceWindow adherenceWindow;
    private ReportingPublisherService reportingPublishingService;

    @Autowired
    public AdherenceSummaryOverIVR(AdherenceService adherenceService, AdherenceWindow adherenceWindow, ReportingPublisherService reportingPublishingService) {
        this.adherenceService = adherenceService;
        this.adherenceWindow = adherenceWindow;
        this.reportingPublishingService = reportingPublishingService;
    }

    public AdherenceFlashingResponse value(AdherenceFlashingRequest adherenceFlashingRequest, ProviderId providerId) {
        LocalDate requestedDate = today();
        reportingPublishingService.reportFlashingRequest(flashingLogRequest(adherenceFlashingRequest, providerId));
        return flashingResponse(providerId, requestedDate);
    }

    private AdherenceFlashingResponse flashingResponse(ProviderId providerId, LocalDate requestedDate) {
        FlashingRequestErrors errors = new FlashingRequestErrors(!providerId.isEmpty(), isAdherenceDay(requestedDate));
        if (errors.isNotEmpty()) {
            return failureResponse(errors.errorMessage());
        } else {
            return successResponse(adherenceSummary(providerId, requestedDate));
        }
    }

    private AdherenceSummary adherenceSummary(ProviderId providerId, LocalDate requestedDate) {
        return adherenceService.adherenceSummary(providerId.value(), requestedDate);
    }

    private FlashingLogRequest flashingLogRequest(AdherenceFlashingRequest adherenceFlashingRequest, ProviderId providerId) {
        return new AdherenceFlashingReportRequest(adherenceFlashingRequest, providerId.value()).flashingLogRequest();
    }

    private boolean isAdherenceDay(LocalDate requestDate) {
        return adherenceWindow.isValidAdherenceDay(requestDate);
    }
}
