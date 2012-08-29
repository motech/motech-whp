package org.motechproject.whp.ivr.operation;


import org.apache.log4j.Logger;
import org.motechproject.decisiontree.core.FlowSession;
import org.motechproject.decisiontree.core.model.INodeOperation;
import org.motechproject.whp.ivr.session.IvrSession;
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.motechproject.whp.reports.contract.AdherenceCaptureRequest;

import static org.motechproject.whp.ivr.builder.request.AdherenceCaptureRequestBuilder.adherenceCaptureRequest;

public class NoInputAdherenceOperation implements INodeOperation {

    private String currentPatientId;
    private ReportingPublisherService reportingService;
    private static Logger logger = Logger.getLogger(InvalidAdherenceOperation.class);

    public NoInputAdherenceOperation() {
    }

    public NoInputAdherenceOperation(String currentPatientId, ReportingPublisherService reportingService) {
        this.currentPatientId = currentPatientId;
        this.reportingService = reportingService;
    }

    @Override
    public void perform(String input, FlowSession flowSession) {
        IvrSession ivrSession = new IvrSession(flowSession);
        publishAdherenceInputEvent(ivrSession);
    }

    private void publishAdherenceInputEvent(IvrSession ivrSession) {
        AdherenceCaptureRequest adherenceCaptureRequest = adherenceCaptureRequest().noAdherenceInput(currentPatientId, ivrSession);
        reportingService.reportAdherenceCapture(adherenceCaptureRequest);
    }
}
