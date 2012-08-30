package org.motechproject.whp.ivr.operation;


import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.testing.utils.BaseUnitTest;
import org.motechproject.whp.ivr.session.IvrSession;
import org.motechproject.whp.ivr.util.FlowSessionStub;
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.motechproject.whp.reports.contract.AdherenceCaptureRequest;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.ivr.builder.request.AdherenceCaptureRequestBuilder.adherenceCaptureRequest;

public class NoInputAdherenceOperationTest extends BaseUnitTest {

    @Mock
    ReportingPublisherService reportingPublisherService;

    FlowSessionStub flowSessionStub;
    IvrSession ivrSession;
    DateTime adherenceStartTime;

    @Before
    public void setUp() {
        initMocks(this);
        adherenceStartTime = new DateTime(2012, 7, 8, 0, 0, 0);
        mockCurrentDate(adherenceStartTime);
        flowSessionStub = new FlowSessionStub();
        ivrSession = new IvrSession(flowSessionStub);
        ivrSession.callId("callId");
        ivrSession.startOfAdherenceSubmission(adherenceStartTime);
        ivrSession.adherenceInputForCurrentPatient("9");
    }

    @Test
    public void shouldReportNoAdherenceInput() {
        String patientID = "patientID";

        new NoInputAdherenceOperation(patientID, reportingPublisherService).perform("", flowSessionStub);

        AdherenceCaptureRequest adherenceCaptureRequest = adherenceCaptureRequest().noAdherenceInput(patientID, ivrSession);
        verify(reportingPublisherService).reportAdherenceCapture(adherenceCaptureRequest);
    }

}
