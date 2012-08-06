package org.motechproject.whp.ivr.operation;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.decisiontree.FlowSession;
import org.motechproject.whp.ivr.session.IvrSession;
import org.motechproject.whp.ivr.util.FlowSessionStub;
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.motechproject.whp.reports.contract.CallLogRequest;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.util.DateUtil.now;

public class PublishCallLogOperationTest {

    @Mock
    ReportingPublisherService reportingPublisherService;

    @Before
    public void setUp()  {
        initMocks(this);
    }

    @Test
    public void shouldPublishCallLog(){
        String providerId = "providerId";
        DateTime startTime = now();
        DateTime endTime = now();
        FlowSession flowSession = new FlowSessionStub();
        flowSession.set(IvrSession.PROVIDER_ID, providerId);
        flowSession.set(IvrSession.CALL_START_TIME, startTime);

        CallLogRequest callLogRequest = createCallLogRequest(providerId, startTime, endTime);
        PublishCallLogOperation publishCallLogOperation = new PublishCallLogOperation(reportingPublisherService, endTime);

        publishCallLogOperation.perform("", flowSession);
        verify(reportingPublisherService).reportCallLog(callLogRequest);
    }

    private CallLogRequest createCallLogRequest(String providerId, DateTime startTime, DateTime endTime) {
        CallLogRequest callLogRequest = new CallLogRequest();
        callLogRequest.setEndTime(endTime.toDate());
        callLogRequest.setStartTime(startTime.toDate());
        callLogRequest.setCalledBy(providerId);
        callLogRequest.setProviderId(providerId);
        return callLogRequest;
    }
}
