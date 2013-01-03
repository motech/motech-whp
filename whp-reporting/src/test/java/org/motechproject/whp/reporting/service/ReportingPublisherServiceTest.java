package org.motechproject.whp.reporting.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.http.client.service.HttpClientService;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.reporting.ReportingEventURLs;
import org.motechproject.whp.reports.contract.*;

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

        AdherenceCallLogRequest callLog = new AdherenceCallLogRequest();
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

    @Test
    public void shouldPublishContainerPatientMappingLog() {
        ContainerPatientMappingReportingRequest containerPatientMappingReportingRequest = new ContainerPatientMappingReportingRequest();

        reportingPublisher.reportContainerPatientMapping(containerPatientMappingReportingRequest);
        verify(httpClientService).post(reportingEventURLs.getContainerPatientMappingLogURL(), containerPatientMappingReportingRequest);
    }

    @Test
    public void shouldReportContainerRegistrationCallDetailsLogRequest() {
        ContainerRegistrationCallDetailsLogRequest request = new ContainerRegistrationCallDetailsLogRequest();

        reportingPublisher.reportContainerRegistrationCallDetailsLog(request);
        verify(httpClientService).post(reportingEventURLs.getContainerRegistrationCallDetailsLogURL(), request);
    }

    @Test
    public void shouldReportProviderVerificationLogRequest() {
        ProviderVerificationLogRequest request = new ProviderVerificationLogRequest();

        reportingPublisher.reportProviderVerificationDetailsLog(request);
        verify(httpClientService).post(reportingEventURLs.getProviderVerificationLogURL(), request);
    }

    @Test
    public void shouldReportContainerVerificationLogRequest() {
        ContainerVerificationLogRequest request = new ContainerVerificationLogRequest();

        reportingPublisher.reportContainerVerificationDetailsLog(request);
        verify(httpClientService).post(reportingEventURLs.getContainerVerificationLogURL(), request);
    }

    @Test
    public void shouldReportAdherenceSubmissionRequest() {
        AdherenceSubmissionRequest request = new AdherenceSubmissionRequest();

        reportingPublisher.reportAdherenceSubmission(request);
        verify(httpClientService).post(reportingEventURLs.getAdherenceSubmissionURL(), request);
    }
}
