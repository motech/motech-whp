package org.motechproject.whp.reporting.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.http.client.service.HttpClientService;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.common.domain.ChannelId;
import org.motechproject.whp.reporting.ReportingEventURLs;
import org.motechproject.whp.reports.contract.*;

import java.util.Date;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;


public class ReportingPublisherServiceTest {

    ReportingPublisherService reportingPublisher;

    @Mock
    HttpClientService httpClientService;

    @Mock
    ReportingEventURLs reportingEventURLs;

    @Before
    public void setUp() {
        initMocks(this);
        reportingPublisher = new ReportingPublisherService(httpClientService, reportingEventURLs);
    }

    @Test
    public void shouldPublishAdherenceCaptureWithTheRightSubject() throws Exception {
        String providerId = "123456";
        String patientId = "abc12345";
        AdherenceCaptureRequest adherenceCaptureRequest = new AdherenceCaptureRequest();
        adherenceCaptureRequest.setPatientId(patientId);
        adherenceCaptureRequest.setProviderId(providerId);

        reportingPublisher.reportAdherenceCapture(adherenceCaptureRequest);
        verify(httpClientService).post(reportingEventURLs.getAdherenceCallLogURL(), adherenceCaptureRequest);
    }

    @Test
    public void shouldPublishCallLog() throws Exception {
        String providerId = "123456";

        CallLogRequest callLog = new CallLogRequest();
        callLog.setStartTime(DateUtil.now().toDate());
        callLog.setEndTime(DateUtil.now().toDate());
        callLog.setCalledBy(providerId);
        callLog.setProviderId(providerId);

        reportingPublisher.reportCallLog(callLog);

        verify(httpClientService).post(reportingEventURLs.getCallLogURL(), callLog);
    }

    @Test
    public void shouldPublishFlashingLog() throws Exception {
        String providerId = "123456";
        FlashingLogRequest flashingLog = new FlashingLogRequest();
        flashingLog.setProviderId(providerId);
        reportingPublisher.reportFlashingRequest(flashingLog);

        verify(httpClientService).post(reportingEventURLs.getFlashingLogURL(), flashingLog);
    }

    @Test
    public void shouldPublishContainerRegistrationLog() throws Exception {

        ContainerRegistrationReportingRequest sputumTrackingRequest = new ContainerRegistrationReportingRequest();

        reportingPublisher.reportContainerRegistration(sputumTrackingRequest);
        verify(httpClientService).post(reportingEventURLs.getContainerRegistrationLogURL(), sputumTrackingRequest);
    }

    @Test
    public void shouldPublishSputumLabResultsCaptureLog() throws Exception {

        SputumLabResultsCaptureReportingRequest labResultsCaptureReportingRequest = new SputumLabResultsCaptureReportingRequest();

        reportingPublisher.reportLabResultsCapture(labResultsCaptureReportingRequest);
        verify(httpClientService).post(reportingEventURLs.getSputumLabResultsCaptureLogURL(), labResultsCaptureReportingRequest);

    }

    @Test
    public void shouldPublishContainerStatusUpdateLog() {
        ContainerStatusReportingRequest containerStatusReportingRequest = new ContainerStatusReportingRequest();

        reportingPublisher.reportContainerStatusUpdate(containerStatusReportingRequest);
        verify(httpClientService).post(reportingEventURLs.getContainerStatusUpdateLogURL(), containerStatusReportingRequest);
    }
}
