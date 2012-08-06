package org.motechproject.whp.ivr.operation;

import org.joda.time.DateTime;
import org.motechproject.decisiontree.FlowSession;
import org.motechproject.decisiontree.model.INodeOperation;
import org.motechproject.whp.ivr.session.IvrSession;
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.motechproject.whp.reports.contract.CallLogRequest;

public class PublishCallLogOperation implements INodeOperation{

    private ReportingPublisherService reportingPublisherService;
    private DateTime callEndTime;

    public PublishCallLogOperation(ReportingPublisherService reportingPublisherService, DateTime callEndTime) {
        this.reportingPublisherService = reportingPublisherService;
        this.callEndTime = callEndTime;
    }

    @Override
    public void perform(String userInput, FlowSession session) {
        IvrSession ivrSession = new IvrSession(session);
        CallLogRequest callLogRequest = buildCallLog(ivrSession);

        reportingPublisherService.reportCallLog(callLogRequest);
    }

    private CallLogRequest buildCallLog(IvrSession ivrSession) {
        CallLogRequest callLogRequest = new CallLogRequest();
        callLogRequest.setCalledBy(ivrSession.providerId());
        callLogRequest.setProviderId(ivrSession.providerId());
        callLogRequest.setStartTime(ivrSession.callStartTime().toDate());
        callLogRequest.setEndTime(callEndTime.toDate());
        return callLogRequest;
    }
}
