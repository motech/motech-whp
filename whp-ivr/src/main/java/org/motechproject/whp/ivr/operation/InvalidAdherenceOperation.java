package org.motechproject.whp.ivr.operation;

import org.apache.log4j.Logger;
import org.motechproject.decisiontree.FlowSession;
import org.motechproject.decisiontree.model.INodeOperation;
import org.motechproject.whp.ivr.session.IvrSession;
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.motechproject.whp.reports.contract.AdherenceCaptureRequest;

import static org.motechproject.whp.ivr.builder.request.AdherenceCaptureRequestBuilder.adherenceCaptureRequest;

public class InvalidAdherenceOperation implements INodeOperation {

    private String currentPatientId;
    private ReportingPublisherService reportingService;
    private static Logger logger = Logger.getLogger(InvalidAdherenceOperation.class);

    public InvalidAdherenceOperation() {
    }

    public InvalidAdherenceOperation(String currentPatientId, ReportingPublisherService reportingService) {
        this.currentPatientId = currentPatientId;
        this.reportingService = reportingService;
    }

    @Override
    public void perform(String input, FlowSession flowSession) {
        IvrSession ivrSession = new IvrSession(flowSession);
        publishAdherenceInputEvent(input, ivrSession);
    }

    private void publishAdherenceInputEvent(String input, IvrSession ivrSession) {
        logger.info("Building invalid adherence request");
        AdherenceCaptureRequest adherenceCaptureRequest = adherenceCaptureRequest().invalidAdherence(currentPatientId, ivrSession, input);
        logger.info("Publishing invalid adherence request");
        reportingService.reportAdherenceCapture(adherenceCaptureRequest);
    }
}
