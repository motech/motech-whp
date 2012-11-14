package org.motechproject.whp.ivr.transition;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.decisiontree.core.FlowSession;
import org.motechproject.decisiontree.core.model.Node;
import org.motechproject.testing.utils.BaseUnitTest;
import org.motechproject.whp.ivr.CallStatus;
import org.motechproject.whp.ivr.operation.PublishCallLogOperation;
import org.motechproject.whp.ivr.session.IvrSession;
import org.motechproject.whp.ivr.util.FlowSessionStub;
import org.motechproject.whp.reporting.service.ReportingPublisherService;

import static junit.framework.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.util.DateUtil.now;

public class HangupTransitionTest extends BaseUnitTest {


    @Mock
    ReportingPublisherService reportingPublisherService;

    @Before
    public void setUp() {
        initMocks(this);
        mockCurrentDate(now());
    }

    @Test
    public void shouldAddPublishCallLogOperationToNode() {

        FlowSession flowSession = new FlowSessionStub();
        flowSession.set(IvrSession.CALL_STATUS, CallStatus.PROVIDER_DISCONNECT);

        Node expectedNode = new Node()
                .addOperations(new PublishCallLogOperation(reportingPublisherService, CallStatus.PROVIDER_DISCONNECT, now()));

        HangupTransition hangupTransition = new HangupTransition(reportingPublisherService);

        assertEquals(expectedNode.getOperations(), hangupTransition.getDestinationNode("hangup", flowSession).getOperations());
    }

}
