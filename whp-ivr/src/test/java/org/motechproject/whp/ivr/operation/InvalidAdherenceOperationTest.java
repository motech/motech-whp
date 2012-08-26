package org.motechproject.whp.ivr.operation;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.testing.utils.BaseUnitTest;
import org.motechproject.whp.ivr.builder.request.AdherenceCaptureBuilder;
import org.motechproject.whp.ivr.session.IvrSession;
import org.motechproject.whp.ivr.util.FlowSessionStub;
import org.motechproject.whp.reporting.service.ReportingPublisherService;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.ivr.builder.request.AdherenceCaptureBuilder.adherenceCapture;

public class InvalidAdherenceOperationTest extends BaseUnitTest {
    @Mock
    ReportingPublisherService reportingPublisherService;
    FlowSessionStub flowSessionStub;

    IvrSession ivrSession;
    DateTime adherenceStartTime;
    @Before
    public void setUp(){
        initMocks(this);
        mockCurrentDate(new DateTime(2012,7,8,0,0,0));
        flowSessionStub = new FlowSessionStub();
        ivrSession = new IvrSession(flowSessionStub);
        ivrSession.callId("callId");
        adherenceStartTime = new DateTime(2012,7,8,0,10,10);
        ivrSession.startOfAdherenceSubmission(adherenceStartTime);
        ivrSession.adherenceInputForCurrentPatient("9");


    }
    @Test
    public void shouldReportInvalidAdherence() {
        String patientId = "patient";
        String input = "7";
        new InvalidAdherenceOperation(patientId, reportingPublisherService).perform(input, flowSessionStub);

        verify(reportingPublisherService).reportAdherenceCapture(adherenceCapture().invalidAdherence(patientId,ivrSession, input));
    }
}
