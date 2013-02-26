package org.motechproject.whp.reporting.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.http.client.service.HttpClientService;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.reporting.ReportingApplicationURLs;
import org.motechproject.whp.reports.contract.*;
import org.motechproject.whp.reports.contract.adherence.AdherenceAuditLogDTO;
import org.motechproject.whp.reports.contract.adherence.AdherenceRecordDTO;
import org.motechproject.whp.reports.contract.patient.PatientDTO;
import org.motechproject.whp.reports.contract.provider.ProviderDTO;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;


public class ReportingPublisherServiceTest {

    ReportingPublisherService reportingPublisher;

    @Mock
    HttpClientService httpClientService;

    @Mock
    ReportingApplicationURLs reportingApplicationURLs;

    @Before
    public void setUp() {
        initMocks(this);
        reportingPublisher = new ReportingPublisherService(httpClientService, reportingApplicationURLs);
    }

    @Test
    public void shouldPublishAdherenceCaptureWithTheRightSubject() throws Exception {
        String providerId = "123456";
        String patientId = "abc12345";
        AdherenceCaptureRequest adherenceCaptureRequest = new AdherenceCaptureRequest();
        adherenceCaptureRequest.setPatientId(patientId);
        adherenceCaptureRequest.setProviderId(providerId);

        reportingPublisher.reportAdherenceCapture(adherenceCaptureRequest);
        verify(httpClientService).post(reportingApplicationURLs.getAdherencePath(), adherenceCaptureRequest);
    }

    @Test
    public void shouldPublishCallLog() throws Exception {
        String providerId = "123456";

        AdherenceCallLogRequest callLog = new AdherenceCallLogRequest();
        callLog.setStartTime(DateUtil.now().toDate());
        callLog.setEndTime(DateUtil.now().toDate());
        callLog.setProviderId(providerId);

        reportingPublisher.reportCallLog(callLog);

        verify(httpClientService).post(reportingApplicationURLs.getCallLogURL(), callLog);
    }

    @Test
    public void shouldPublishCallStatus() throws Exception {
        String url = "url";
        when(reportingApplicationURLs.getCallStatusURL()).thenReturn(url);
        AdherenceCallStatusRequest callStatusRequest = mock(AdherenceCallStatusRequest.class);

        reportingPublisher.reportCallStatus(callStatusRequest);

        verify(httpClientService).post(url, callStatusRequest);
    }

    @Test
    public void shouldPublishFlashingLog() throws Exception {
        String url = "url";
        when(reportingApplicationURLs.getFlashingLogURL()).thenReturn(url);
        String providerId = "123456";
        FlashingLogRequest flashingLog = new FlashingLogRequest();
        flashingLog.setProviderId(providerId);
        reportingPublisher.reportFlashingRequest(flashingLog);

        verify(httpClientService).post(url, flashingLog);
    }

    @Test
    public void shouldPublishContainerRegistrationLog() throws Exception {
        String url = "url";
        when(reportingApplicationURLs.getContainerRegistrationURL()).thenReturn(url);

        ContainerRegistrationReportingRequest sputumTrackingRequest = new ContainerRegistrationReportingRequest();

        reportingPublisher.reportContainerRegistration(sputumTrackingRequest);
        verify(httpClientService).post(url, sputumTrackingRequest);
    }

    @Test
    public void shouldPublishSputumLabResultsCaptureLog() throws Exception {
        String url = "url";
        when(reportingApplicationURLs.getSputumLabResultsCaptureLogURL()).thenReturn(url);

        SputumLabResultsCaptureReportingRequest labResultsCaptureReportingRequest = new SputumLabResultsCaptureReportingRequest();

        reportingPublisher.reportLabResultsCapture(labResultsCaptureReportingRequest);
        verify(httpClientService).post(url, labResultsCaptureReportingRequest);

    }

    @Test
    public void shouldPublishContainerStatusUpdateLog() {
        String url = "url";
        when(reportingApplicationURLs.getContainerStatusUpdateLogURL()).thenReturn(url);

        ContainerStatusReportingRequest containerStatusReportingRequest = new ContainerStatusReportingRequest();

        reportingPublisher.reportContainerStatusUpdate(containerStatusReportingRequest);
        verify(httpClientService).post(url, containerStatusReportingRequest);
    }

    @Test
    public void shouldPublishContainerPatientMappingLog() {
        String url = "url";
        when(reportingApplicationURLs.getContainerPatientMappingLogURL()).thenReturn(url);

        ContainerPatientMappingReportingRequest containerPatientMappingReportingRequest = new ContainerPatientMappingReportingRequest();

        reportingPublisher.reportContainerPatientMapping(containerPatientMappingReportingRequest);
        verify(httpClientService).post(url, containerPatientMappingReportingRequest);
    }

    @Test
    public void shouldReportContainerRegistrationCallDetailsLogRequest() {
        String url = "url";
        when(reportingApplicationURLs.getContainerRegistrationCallDetailsLogURL()).thenReturn(url);

        ContainerRegistrationCallDetailsLogRequest request = new ContainerRegistrationCallDetailsLogRequest();

        reportingPublisher.reportContainerRegistrationCallDetailsLog(request);
        verify(httpClientService).post(url, request);
    }

    @Test
    public void shouldReportProviderVerificationLogRequest() {
        String url = "url";
        when(reportingApplicationURLs.getProviderVerificationLogURL()).thenReturn(url);

        ProviderVerificationLogRequest request = new ProviderVerificationLogRequest();

        reportingPublisher.reportProviderVerificationDetailsLog(request);
        verify(httpClientService).post(url, request);
    }

    @Test
    public void shouldReportContainerVerificationLogRequest() {
        String url = "url";
        when(reportingApplicationURLs.getContainerVerificationLogURL()).thenReturn(url);

        ContainerVerificationLogRequest request = new ContainerVerificationLogRequest();

        reportingPublisher.reportContainerVerificationDetailsLog(request);
        verify(httpClientService).post(url, request);
    }

    @Test
    public void shouldReportAdherenceSubmissionRequest() {
        String url = "url";
        when(reportingApplicationURLs.getAdherenceSubmissionURL()).thenReturn(url);

        AdherenceSubmissionRequest request = new AdherenceSubmissionRequest();

        reportingPublisher.reportAdherenceSubmission(request);
        verify(httpClientService).post(url, request);
    }

    @Test
    public void shouldReportPatientUpdates() {
        String url = "url";
        when(reportingApplicationURLs.getPatientUpdateURL()).thenReturn(url);

        PatientDTO request = new PatientDTO();

        reportingPublisher.reportPatient(request);
        verify(httpClientService).post(url, request);
    }

    @Test
    public void shouldReportProviderUpdates() {
        String url = "url";
        when(reportingApplicationURLs.getProviderUpdateURL()).thenReturn(url);

        ProviderDTO providerDTO = new ProviderDTO();

        reportingPublisher.reportProvider(providerDTO);
        verify(httpClientService).post(url, providerDTO);
    }

    @Test
    public void shouldPublishAdherenceAuditLog() {
        String url = "url";
        when(reportingApplicationURLs.getAdherenceAuditLogURL()).thenReturn(url);

        AdherenceAuditLogDTO adherenceAuditLogDTO = new AdherenceAuditLogDTO();

        reportingPublisher.reportAdherenceAuditLog(adherenceAuditLogDTO);
        verify(httpClientService).post(url, adherenceAuditLogDTO);
    }

    @Test
    public void shouldPublishAdherenceRecord() {
        String url = "url";
        when(reportingApplicationURLs.getAdherenceRecordUpdateURL()).thenReturn(url);

        AdherenceRecordDTO adherenceRecordDTO = new AdherenceRecordDTO();

        reportingPublisher.reportAdherenceRecord(adherenceRecordDTO);
        verify(httpClientService).post(url, adherenceRecordDTO);
    }

}
