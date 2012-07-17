package org.motechproject.whp.reporting.handlers;

import org.motechproject.scheduler.domain.MotechEvent;
import org.motechproject.server.event.annotations.MotechListener;
import org.motechproject.whp.reporting.ReportingEventKeys;
import org.motechproject.whp.reporting.gateway.ReportingGateway;
import org.motechproject.whp.reporting.request.AdherenceCaptureRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdherenceReportHandler {

    private final static Logger logger = LoggerFactory.getLogger(AdherenceReportHandler.class);
    private ReportingGateway reportingGateway;

    @Autowired
    public AdherenceReportHandler(ReportingGateway reportingGateway) {
        this.reportingGateway = reportingGateway;
    }

    @MotechListener(subjects = {ReportingEventKeys.REPORT_ADHERENCE_CAPTURE})
    public void handleAdherenceCapture(MotechEvent event) {
        AdherenceCaptureRequest adherenceCaptureRequest = (AdherenceCaptureRequest) event.getParameters().get("0");
        logger.info(String.format("Handling adherence capture event for patient: %s, provider: %s", adherenceCaptureRequest.getPatientId(), adherenceCaptureRequest.getProviderId()));
        reportingGateway.captureAdherence(adherenceCaptureRequest);
    }


}