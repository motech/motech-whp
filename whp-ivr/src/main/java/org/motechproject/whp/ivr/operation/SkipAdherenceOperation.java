package org.motechproject.whp.ivr.operation;

import org.motechproject.decisiontree.core.FlowSession;
import org.motechproject.decisiontree.core.model.INodeOperation;
import org.motechproject.whp.ivr.session.IvrSession;
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.motechproject.whp.reports.contract.AdherenceCaptureRequest;

import static org.motechproject.util.DateUtil.now;
import static org.motechproject.whp.ivr.builder.request.AdherenceCaptureRequestBuilder.adherenceCaptureRequest;

public class SkipAdherenceOperation implements INodeOperation {

    private String currentPatientId;
    private ReportingPublisherService reportingService;

    public SkipAdherenceOperation() {
    }

    public SkipAdherenceOperation(String currentPatientId, ReportingPublisherService reportingService) {
        this.currentPatientId = currentPatientId;
        this.reportingService = reportingService;
    }

    @Override
    public void perform(String input, FlowSession flowSession) {
        IvrSession ivrSession = new IvrSession(flowSession);
        publishAdherenceSkipEvent(ivrSession);
        ivrSession.startOfAdherenceSubmission(now());

    }

    private void publishAdherenceSkipEvent(IvrSession ivrSession) {
        AdherenceCaptureRequest adherenceCaptureRequest = adherenceCaptureRequest().skipAdherence(currentPatientId, ivrSession);
        reportingService.reportAdherenceCapture(adherenceCaptureRequest);
    }

}
