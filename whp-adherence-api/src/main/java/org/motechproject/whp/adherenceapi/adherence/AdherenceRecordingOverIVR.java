package org.motechproject.whp.adherenceapi.adherence;

import org.motechproject.whp.adherenceapi.domain.Dosage;
import org.motechproject.whp.adherenceapi.domain.ProviderId;
import org.motechproject.whp.adherenceapi.reporting.AdherenceCaptureReportRequest;
import org.motechproject.whp.adherenceapi.request.AdherenceValidationRequest;
import org.motechproject.whp.adherenceapi.response.validation.AdherenceValidationResponse;
import org.motechproject.whp.adherenceapi.service.AdherenceService;
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.motechproject.whp.reports.contract.AdherenceCaptureRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.motechproject.whp.adherenceapi.response.validation.AdherenceValidationResponse.failure;
import static org.motechproject.whp.adherenceapi.response.validation.AdherenceValidationResponse.success;

@Service
public class AdherenceRecordingOverIVR {

    private AdherenceService adherenceService;
    private ReportingPublisherService reportingService;

    @Autowired
    public AdherenceRecordingOverIVR(AdherenceService adherenceService, ReportingPublisherService reportingService) {
        this.adherenceService = adherenceService;
        this.reportingService = reportingService;
    }

    public AdherenceValidationResponse validateInput(AdherenceValidationRequest request, ProviderId providerId) {
        AdherenceValidationResponse response = validateAdherenceInput(request);
        AdherenceCaptureRequest reportingRequest = new AdherenceCaptureReportRequest(request, providerId).captureRequest();

        reportingService.reportAdherenceCapture(reportingRequest);
        return response;
    }

    private AdherenceValidationResponse validateAdherenceInput(AdherenceValidationRequest adherenceValidationRequest) {
        Dosage dosage = adherenceService.dosageForPatient(adherenceValidationRequest.getPatientId());
        if (null == dosage) {
            return failure();
        } else if (dosage.isValidInput(adherenceValidationRequest.doseTakenCount())) {
            return success();
        } else {
            return failure(dosage);
        }
    }
}
