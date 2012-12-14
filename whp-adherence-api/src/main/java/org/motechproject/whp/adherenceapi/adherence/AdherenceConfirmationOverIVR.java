package org.motechproject.whp.adherenceapi.adherence;

import org.motechproject.whp.adherence.audit.contract.AuditParams;
import org.motechproject.whp.adherence.domain.AdherenceSource;
import org.motechproject.whp.adherence.domain.WeeklyAdherenceSummary;
import org.motechproject.whp.adherenceapi.domain.ProviderId;
import org.motechproject.whp.adherenceapi.reporting.AdherenceCaptureReportRequest;
import org.motechproject.whp.adherenceapi.request.AdherenceConfirmationRequest;
import org.motechproject.whp.adherenceapi.request.AdherenceValidationRequest;
import org.motechproject.whp.adherenceapi.response.validation.AdherenceValidationResponse;
import org.motechproject.whp.applicationservice.orchestrator.TreatmentUpdateOrchestrator;
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.motechproject.whp.reports.contract.AdherenceCaptureRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.motechproject.whp.adherenceapi.domain.AdherenceCaptureStatus.ADHERENCE_PROVIDED;
import static org.motechproject.whp.common.domain.TreatmentWeekInstance.currentAdherenceCaptureWeek;

@Service
public class AdherenceConfirmationOverIVR {

    private ReportingPublisherService reportingService;
    private TreatmentUpdateOrchestrator treatmentUpdateOrchestrator;
    private AdherenceValidationOverIVR adherenceValidationOverIVR;

    @Autowired
    public AdherenceConfirmationOverIVR(ReportingPublisherService reportingService, TreatmentUpdateOrchestrator treatmentUpdateOrchestrator, AdherenceValidationOverIVR adherenceValidationOverIVR) {
        this.treatmentUpdateOrchestrator = treatmentUpdateOrchestrator;
        this.adherenceValidationOverIVR = adherenceValidationOverIVR;
        this.reportingService = reportingService;
    }

    public AdherenceValidationResponse confirmAdherence(AdherenceConfirmationRequest request, ProviderId providerId) {
        AdherenceValidationRequest validationRequest = request.validationRequest();
        AdherenceValidationResponse response = adherenceValidationOverIVR.validateAdherenceInput(validationRequest, providerId);

        if (!response.failed()) {
            recordAdherence(request, providerId);
            reportAdherenceConfirmation(validationRequest, response, providerId);
        }
        return response;
    }

    private void recordAdherence(AdherenceConfirmationRequest request, ProviderId providerId) {
        WeeklyAdherenceSummary weeklyAdherenceSummary = new WeeklyAdherenceSummary(request.getPatientId(), currentAdherenceCaptureWeek(), request.doseTakenCount());
        treatmentUpdateOrchestrator.recordWeeklyAdherence(weeklyAdherenceSummary, request.getPatientId(), new AuditParams(providerId.value(), AdherenceSource.IVR, ""));
    }

    private void reportAdherenceConfirmation(AdherenceValidationRequest request, AdherenceValidationResponse response, ProviderId providerId) {
        AdherenceCaptureRequest reportingRequest = new AdherenceCaptureReportRequest(
                request,
                providerId,
                !response.failed(),
                ADHERENCE_PROVIDED).request();
        reportingService.reportAdherenceCapture(reportingRequest);
    }
}
