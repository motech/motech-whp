package org.motechproject.whp.reporting.service;

import org.apache.log4j.Logger;
import org.motechproject.http.client.service.HttpClientService;
import org.motechproject.whp.reporting.ReportingEventURLs;
import org.motechproject.whp.reports.contract.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportingPublisherService {

    private HttpClientService httpClientService;
    private ReportingEventURLs reportingEventURLs;
    private Logger logger = Logger.getLogger(ReportingPublisherService.class);

    @Autowired
    public ReportingPublisherService(HttpClientService httpClientService, ReportingEventURLs reportingEventURLs) {
        this.httpClientService = httpClientService;
        this.reportingEventURLs = reportingEventURLs;
    }

    public void reportAdherenceCapture(AdherenceCaptureRequest adherenceCaptureRequest) {
        logger.info("Posting adherence capture request to the queue");
        httpClientService.post(reportingEventURLs.getAdherenceCallLogURL(), adherenceCaptureRequest);
    }

    public void reportCallLog(AdherenceCallLogRequest callLogRequest) {
        httpClientService.post(reportingEventURLs.getCallLogURL(), callLogRequest);
    }

    public void reportFlashingRequest(FlashingLogRequest flashingLogRequest) {
        httpClientService.post(reportingEventURLs.getFlashingLogURL(), flashingLogRequest);
    }

    public void reportContainerRegistration(ContainerRegistrationReportingRequest request) {
        httpClientService.post(reportingEventURLs.getContainerRegistrationLogURL(), request);
    }

    public void reportLabResultsCapture(SputumLabResultsCaptureReportingRequest request) {
        httpClientService.post(reportingEventURLs.getSputumLabResultsCaptureLogURL(), request);
    }

    public void reportContainerStatusUpdate(ContainerStatusReportingRequest request) {
        httpClientService.post(reportingEventURLs.getContainerStatusUpdateLogURL(), request);
    }

    public void reportContainerPatientMapping(ContainerPatientMappingReportingRequest request) {
        httpClientService.post(reportingEventURLs.getContainerPatientMappingLogURL(), request);
    }
}
