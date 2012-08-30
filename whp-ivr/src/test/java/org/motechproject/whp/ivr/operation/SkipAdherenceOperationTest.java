package org.motechproject.whp.ivr.operation;


import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.testing.utils.BaseUnitTest;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.ivr.session.IvrSession;
import org.motechproject.whp.ivr.util.FlowSessionStub;
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.motechproject.whp.reports.contract.AdherenceCaptureRequest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.util.DateUtil.now;
import static org.motechproject.whp.ivr.builder.request.AdherenceCaptureRequestBuilder.adherenceCaptureRequest;

public class SkipAdherenceOperationTest extends BaseUnitTest {

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
    public void shouldReportSkipAdherence() {
        String patientID = "patientID";

        new SkipAdherenceOperation(patientID, reportingPublisherService).perform("9", flowSessionStub);

        AdherenceCaptureRequest adherenceCaptureRequest = adherenceCaptureRequest().skipAdherence(patientID, ivrSession);
        verify(reportingPublisherService).reportAdherenceCapture(adherenceCaptureRequest);
    }

    @Test
    public void shouldResetCaptureAdherenceTime() {
        DateTime now = now();
        mockCurrentDate(now);
        String patientID = "patientID";

        new SkipAdherenceOperation(patientID, reportingPublisherService).perform("9", flowSessionStub);

        assertThat(ivrSession.startOfAdherenceSubmission(), is(now));
    }

}
