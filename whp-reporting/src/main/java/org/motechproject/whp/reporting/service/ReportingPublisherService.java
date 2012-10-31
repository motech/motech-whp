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

    public void reportCallLog(CallLogRequest callLogRequest) {
        httpClientService.post(reportingEventURLs.getCallLogURL(), callLogRequest);
    }

    public void reportFlashingRequest(FlashingLogRequest flashingLogRequest) {
        httpClientService.post(reportingEventURLs.getFlashingLogURL(), flashingLogRequest);
    }

    public void reportContainerRegistration(ContainerRegistrationReportingRequest sputumTrackingRequest) {
        httpClientService.post(reportingEventURLs.getContainerRegistrationLogURL(), sputumTrackingRequest);
    }

    public void reportLabResultsCapture(SputumLabResultsCaptureReportingRequest labResultsCaptureReportingRequest) {
        httpClientService.post(reportingEventURLs.getSputumLabResultsCaptureLogURL(), labResultsCaptureReportingRequest);
    }

    public void reportContainerStatusUpdate(ContainerStatusReportingRequest expectedReportingRequest) {
        httpClientService.post(reportingEventURLs.getContainerStatusUpdateLogURL(), expectedReportingRequest);
    }
}
