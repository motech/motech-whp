package org.motechproject.whp.reporting.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.http.client.service.HttpClientService;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.reporting.ReportingEventURLs;
import org.motechproject.whp.reports.contract.*;
import org.motechproject.whp.reports.contract.patient.PatientDTO;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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
        verify(httpClientService).post(reportingEventURLs.getAdherencePath(), adherenceCaptureRequest);
    }

    @Test
    public void shouldPublishCallLog() throws Exception {
        String providerId = "123456";

        AdherenceCallLogRequest callLog = new AdherenceCallLogRequest();
        callLog.setStartTime(DateUtil.now().toDate());
        callLog.setEndTime(DateUtil.now().toDate());
        callLog.setProviderId(providerId);

        reportingPublisher.reportCallLog(callLog);

        verify(httpClientService).post(reportingEventURLs.getCallLogURL(), callLog);
    }

    @Test
    public void shouldPublishCallStatus() throws Exception {
        String url = "url";
        when(reportingEventURLs.getCallStatusURL()).thenReturn(url);
        AdherenceCallStatusRequest callStatusRequest = mock(AdherenceCallStatusRequest.class);

        reportingPublisher.reportCallStatus(callStatusRequest);

        verify(httpClientService).post(url, callStatusRequest);
    }

    @Test
    public void shouldPublishFlashingLog() throws Exception {
        String url = "url";
        when(reportingEventURLs.getFlashingLogURL()).thenReturn(url);
        String providerId = "123456";
        FlashingLogRequest flashingLog = new FlashingLogRequest();
        flashingLog.setProviderId(providerId);
        reportingPublisher.reportFlashingRequest(flashingLog);

        verify(httpClientService).post(url, flashingLog);
    }

    @Test
    public void shouldPublishContainerRegistrationLog() throws Exception {
        String url = "url";
        when(reportingEventURLs.getContainerRegistrationLogURL()).thenReturn(url);

        ContainerRegistrationReportingRequest sputumTrackingRequest = new ContainerRegistrationReportingRequest();

        reportingPublisher.reportContainerRegistration(sputumTrackingRequest);
        verify(httpClientService).post(url, sputumTrackingRequest);
    }

    @Test
    public void shouldPublishSputumLabResultsCaptureLog() throws Exception {
        String url = "url";
        when(reportingEventURLs.getSputumLabResultsCaptureLogURL()).thenReturn(url);

        SputumLabResultsCaptureReportingRequest labResultsCaptureReportingRequest = new SputumLabResultsCaptureReportingRequest();

        reportingPublisher.reportLabResultsCapture(labResultsCaptureReportingRequest);
        verify(httpClientService).post(url, labResultsCaptureReportingRequest);

    }

    @Test
    public void shouldPublishContainerStatusUpdateLog() {
        String url = "url";
        when(reportingEventURLs.getContainerStatusUpdateLogURL()).thenReturn(url);

        ContainerStatusReportingRequest containerStatusReportingRequest = new ContainerStatusReportingRequest();

        reportingPublisher.reportContainerStatusUpdate(containerStatusReportingRequest);
        verify(httpClientService).post(url, containerStatusReportingRequest);
    }

    @Test
    public void shouldPublishContainerPatientMappingLog() {
        String url = "url";
        when(reportingEventURLs.getContainerPatientMappingLogURL()).thenReturn(url);

        ContainerPatientMappingReportingRequest containerPatientMappingReportingRequest = new ContainerPatientMappingReportingRequest();

        reportingPublisher.reportContainerPatientMapping(containerPatientMappingReportingRequest);
        verify(httpClientService).post(url, containerPatientMappingReportingRequest);
    }

    @Test
    public void shouldReportContainerRegistrationCallDetailsLogRequest() {
        String url = "url";
        when(reportingEventURLs.getContainerRegistrationCallDetailsLogURL()).thenReturn(url);

        ContainerRegistrationCallDetailsLogRequest request = new ContainerRegistrationCallDetailsLogRequest();

        reportingPublisher.reportContainerRegistrationCallDetailsLog(request);
        verify(httpClientService).post(url, request);
    }

    @Test
    public void shouldReportProviderVerificationLogRequest() {
        String url = "url";
        when(reportingEventURLs.getProviderVerificationLogURL()).thenReturn(url);

        ProviderVerificationLogRequest request = new ProviderVerificationLogRequest();

        reportingPublisher.reportProviderVerificationDetailsLog(request);
        verify(httpClientService).post(url, request);
    }

    @Test
    public void shouldReportContainerVerificationLogRequest() {
        String url = "url";
        when(reportingEventURLs.getContainerVerificationLogURL()).thenReturn(url);

        ContainerVerificationLogRequest request = new ContainerVerificationLogRequest();

        reportingPublisher.reportContainerVerificationDetailsLog(request);
        verify(httpClientService).post(url, request);
    }

    @Test
    public void shouldReportAdherenceSubmissionRequest() {
        String url = "url";
        when(reportingEventURLs.getAdherenceSubmissionURL()).thenReturn(url);

        AdherenceSubmissionRequest request = new AdherenceSubmissionRequest();

        reportingPublisher.reportAdherenceSubmission(request);
        verify(httpClientService).post(url, request);
    }

    @Test
    public void shouldReportPatientUpdates() {
        String url = "url";
        when(reportingEventURLs.getPatientUpdateURL()).thenReturn(url);

        PatientDTO request = new PatientDTO();

        reportingPublisher.reportPatient(request);
        verify(httpClientService).post(url, request);
    }
}
