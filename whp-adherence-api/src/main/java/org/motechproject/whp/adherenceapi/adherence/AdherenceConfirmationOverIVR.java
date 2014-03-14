package org.motechproject.whp.adherenceapi.adherence;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.annotations.MotechListener;
import org.motechproject.scheduler.context.EventContext;
import org.motechproject.whp.adherence.audit.contract.AuditParams;
import org.motechproject.whp.adherence.domain.AdherenceSource;
import org.motechproject.whp.adherence.domain.WeeklyAdherenceSummary;
import org.motechproject.whp.adherenceapi.domain.ProviderId;
import org.motechproject.whp.adherenceapi.reporting.AdherenceCaptureReportRequest;
import org.motechproject.whp.adherenceapi.request.AdherenceConfirmationRequest;
import org.motechproject.whp.adherenceapi.request.AdherenceValidationRequest;
import org.motechproject.whp.adherenceapi.response.validation.AdherenceValidationResponse;
import org.motechproject.whp.adherenceapi.validator.AdherenceValidationRequestValidator;
import org.motechproject.whp.applicationservice.orchestrator.TreatmentUpdateOrchestrator;
import org.motechproject.whp.common.event.EventKeys;
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
    private AdherenceValidationRequestValidator adherenceValidationOverIVR;
    private EventContext eventContext;

    @Autowired
    public AdherenceConfirmationOverIVR(ReportingPublisherService reportingService, TreatmentUpdateOrchestrator treatmentUpdateOrchestrator, AdherenceValidationRequestValidator adherenceValidationRequestValidator, EventContext eventContext) {
        this.treatmentUpdateOrchestrator = treatmentUpdateOrchestrator;
        this.adherenceValidationOverIVR = adherenceValidationRequestValidator;
        this.reportingService = reportingService;
        this.eventContext = eventContext;
    }

    public AdherenceValidationResponse confirmAdherence(AdherenceConfirmationRequest request, ProviderId providerId) {
        AdherenceValidationRequest validationRequest = request.validationRequest();
        AdherenceValidationResponse response = adherenceValidationOverIVR.validate(validationRequest, providerId);

        if (!response.failed()) {
            eventContext.send(EventKeys.PROVIDER_CONFIRMS_ADHERENCE_OVER_IVR, request);
            reportAdherenceConfirmation(validationRequest, response, providerId);
        }
        return response;
    }

    @MotechListener(subjects = EventKeys.PROVIDER_CONFIRMS_ADHERENCE_OVER_IVR)
    public void recordAdherence(MotechEvent event) {
        AdherenceConfirmationRequest request = (AdherenceConfirmationRequest) event.getParameters().get("0");
        WeeklyAdherenceSummary weeklyAdherenceSummary = new WeeklyAdherenceSummary(request.getPatientId(), currentAdherenceCaptureWeek(), request.doseTakenCount());
        treatmentUpdateOrchestrator.recordWeeklyAdherence(weeklyAdherenceSummary, request.getPatientId(), new AuditParams(request.getProviderId(), AdherenceSource.IVR, ""));
    }

    private void reportAdherenceConfirmation(AdherenceValidationRequest request, AdherenceValidationResponse response, ProviderId providerId) {
        AdherenceCaptureRequest reportingRequest = new AdherenceCaptureReportRequest(
                request,
                providerId,
                !response.failed(),
                ADHERENCE_PROVIDED).request();
        reportingService.reportAdherenceCapture(reportingRequest);
    }

    public void deleteAdherenceOfInvalidPatient(String patientId){
        reportingService.removeAdherenceOverIVR(patientId);
    }
}
