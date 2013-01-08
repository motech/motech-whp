package org.motechproject.whp.adherenceapi.adherence;

import org.motechproject.whp.adherenceapi.reporting.AdherenceCallStatusReportRequest;
import org.motechproject.whp.adherenceapi.request.AdherenceCallStatusRequest;
import org.motechproject.whp.adherenceapi.response.validation.AdherenceValidationResponse;
import org.motechproject.whp.adherenceapi.validator.AdherenceCallStatusRequestValidator;
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdherenceCallStatusOverIVR {

    private ReportingPublisherService reportingService;
    private AdherenceCallStatusRequestValidator adherenceCallStatusRequestValidator;

    @Autowired
    public AdherenceCallStatusOverIVR(ReportingPublisherService reportingService, AdherenceCallStatusRequestValidator adherenceCallStatusRequestValidator) {
        this.adherenceCallStatusRequestValidator = adherenceCallStatusRequestValidator;
        this.reportingService = reportingService;
    }

    public AdherenceValidationResponse recordCallStatus(AdherenceCallStatusRequest request) {
        AdherenceValidationResponse response = adherenceCallStatusRequestValidator.validate(request);
        if(!response.failed()) {
            reportingService.reportCallStatus(new AdherenceCallStatusReportRequest(request).callStatusRequest());
        }
        return response;
    }
}
