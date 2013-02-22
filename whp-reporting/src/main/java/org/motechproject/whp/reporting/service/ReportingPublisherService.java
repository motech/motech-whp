package org.motechproject.whp.reporting.service;

import org.apache.log4j.Logger;
import org.motechproject.http.client.service.HttpClientService;
import org.motechproject.whp.reporting.ReportingEventURLs;
import org.motechproject.whp.reports.contract.*;
import org.motechproject.whp.reports.contract.adherence.AdherenceAuditLogDTO;
import org.motechproject.whp.reports.contract.adherence.AdherenceRecordDTO;
import org.motechproject.whp.reports.contract.patient.PatientDTO;
import org.motechproject.whp.reports.contract.provider.ProviderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportingPublisherService {

    private HttpClientService httpClientService;
    private ReportingEventURLs reportingEventURLs;
    private Logger logger = Logger.getLogger(ReportingPublisherService.class);

    @Autowired
    public ReportingPublisherService(HttpClientService httpClientService, ReportingEventURLs reportingEventURLs) {
        this.httpClientService = httpClientService;
        this.reportingEventURLs = reportingEventURLs;
    }

    public void reportAdherenceCapture(AdherenceCaptureRequest adherenceCaptureRequest) {
        logger.info("Posting adherence capture request to the queue");
        httpClientService.post(reportingEventURLs.getAdherencePath(), adherenceCaptureRequest);
    }

    public void reportCallLog(AdherenceCallLogRequest callLogRequest) {
        httpClientService.post(reportingEventURLs.getCallLogURL(), callLogRequest);
    }

    public void reportFlashingRequest(FlashingLogRequest flashingLogRequest) {
        httpClientService.post(reportingEventURLs.getFlashingLogURL(), flashingLogRequest);
    }

    public void reportContainerRegistration(ContainerRegistrationReportingRequest request) {
        httpClientService.post(reportingEventURLs.getContainerRegistrationURL(), request);
    }

    public void reportLabResultsCapture(SputumLabResultsCaptureReportingRequest request) {
        httpClientService.post(reportingEventURLs.getSputumLabResultsCaptureLogURL(), request);
    }

    public void reportContainerStatusUpdate(ContainerStatusReportingRequest request) {
        httpClientService.post(reportingEventURLs.getContainerStatusUpdateLogURL(), request);
    }

    public void reportContainerPatientMapping(ContainerPatientMappingReportingRequest request) {
        httpClientService.post(reportingEventURLs.getContainerPatientMappingLogURL(), request);
    }

    public void reportContainerRegistrationCallDetailsLog(ContainerRegistrationCallDetailsLogRequest request) {
        httpClientService.post(reportingEventURLs.getContainerRegistrationCallDetailsLogURL(), request);
    }

    public void reportProviderVerificationDetailsLog(ProviderVerificationLogRequest request) {
        httpClientService.post(reportingEventURLs.getProviderVerificationLogURL(), request);
    }

    public void reportContainerVerificationDetailsLog(ContainerVerificationLogRequest request) {
        httpClientService.post(reportingEventURLs.getContainerVerificationLogURL(), request);
    }

    public void reportProviderReminderCallLog(ProviderReminderCallLogRequest request) {
        httpClientService.post(reportingEventURLs.getProviderReminderCallLogURL(), request);
    }

    public void reportAdherenceSubmission(AdherenceSubmissionRequest request) {
        httpClientService.post(reportingEventURLs.getAdherenceSubmissionURL(), request);
    }

    public void reportCallStatus(AdherenceCallStatusRequest request) {
        httpClientService.post(reportingEventURLs.getCallStatusURL(), request);
    }

    public void reportPatient(PatientDTO patientDTO) {
        httpClientService.post(reportingEventURLs.getPatientUpdateURL(), patientDTO);
    }

    public void reportProvider(ProviderDTO providerDTO) {
        httpClientService.post(reportingEventURLs.getProviderUpdateURL(), providerDTO);
    }

    public void reportAdherenceAuditLog(AdherenceAuditLogDTO adherenceAuditLogDTO) {
        httpClientService.post(reportingEventURLs.getAdherenceAuditLogURL(), adherenceAuditLogDTO);
    }

    public void reportAdherenceRecord(AdherenceRecordDTO adherenceRecordDTO) {
        httpClientService.post(reportingEventURLs.getAdherenceRecordUpdateURL(), adherenceRecordDTO);
    }
}
