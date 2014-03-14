package org.motechproject.whp.reporting.service;

import org.apache.log4j.Logger;
import org.motechproject.http.client.service.HttpClientService;
import org.motechproject.whp.reporting.ReportingApplicationURLs;
import org.motechproject.whp.reports.contract.*;
import org.motechproject.whp.reports.contract.adherence.AdherenceAuditLogDTO;
import org.motechproject.whp.reports.contract.adherence.AdherenceRecordDTO;
import org.motechproject.whp.reports.contract.patient.PatientDTO;
import org.motechproject.whp.reports.contract.provider.ProviderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
public class ReportingPublisherService {

    private HttpClientService httpClientService;
    private ReportingApplicationURLs reportingApplicationURLs;
    private Logger logger = Logger.getLogger(ReportingPublisherService.class);

    @Autowired
    public ReportingPublisherService(HttpClientService httpClientService, ReportingApplicationURLs reportingApplicationURLs) {
        this.httpClientService = httpClientService;
        this.reportingApplicationURLs = reportingApplicationURLs;
    }

    public void reportAdherenceCapture(AdherenceCaptureRequest adherenceCaptureRequest) {
        logger.info("Posting adherence capture request to the queue");
        httpClientService.post(reportingApplicationURLs.getAdherencePath(), adherenceCaptureRequest);
    }

    public void reportCallLog(AdherenceCallLogRequest callLogRequest) {
        httpClientService.post(reportingApplicationURLs.getCallLogURL(), callLogRequest);
    }

    public void reportFlashingRequest(FlashingLogRequest flashingLogRequest) {
        httpClientService.post(reportingApplicationURLs.getFlashingLogURL(), flashingLogRequest);
    }

    public void reportContainerRegistration(ContainerRegistrationReportingRequest request) {
        httpClientService.post(reportingApplicationURLs.getContainerRegistrationURL(), request);
    }

    public void reportLabResultsCapture(SputumLabResultsCaptureReportingRequest request) {
        httpClientService.post(reportingApplicationURLs.getSputumLabResultsCaptureLogURL(), request);
    }

    public void reportContainerStatusUpdate(ContainerStatusReportingRequest request) {
        httpClientService.post(reportingApplicationURLs.getContainerStatusUpdateLogURL(), request);
    }

    public void reportContainerPatientMapping(ContainerPatientMappingReportingRequest request) {
        httpClientService.post(reportingApplicationURLs.getContainerPatientMappingLogURL(), request);
    }

    public void reportContainerRegistrationCallDetailsLog(ContainerRegistrationCallDetailsLogRequest request) {
        httpClientService.post(reportingApplicationURLs.getContainerRegistrationCallDetailsLogURL(), request);
    }

    public void reportProviderVerificationDetailsLog(ProviderVerificationLogRequest request) {
        httpClientService.post(reportingApplicationURLs.getProviderVerificationLogURL(), request);
    }

    public void reportContainerVerificationDetailsLog(ContainerVerificationLogRequest request) {
        httpClientService.post(reportingApplicationURLs.getContainerVerificationLogURL(), request);
    }

    public void reportProviderReminderCallLog(ProviderReminderCallLogRequest request) {
        httpClientService.post(reportingApplicationURLs.getProviderReminderCallLogURL(), request);
    }

    public void reportAdherenceSubmission(AdherenceSubmissionRequest request) {
        httpClientService.post(reportingApplicationURLs.getAdherenceSubmissionURL(), request);
    }

    public void reportCallStatus(AdherenceCallStatusRequest request) {
        httpClientService.post(reportingApplicationURLs.getCallStatusURL(), request);
    }

    public void reportPatient(PatientDTO patientDTO) {
        httpClientService.post(reportingApplicationURLs.getPatientUpdateURL(), patientDTO);
    }

    public void reportProvider(ProviderDTO providerDTO) {
        httpClientService.post(reportingApplicationURLs.getProviderUpdateURL(), providerDTO);
    }

    public void reportAdherenceAuditLog(AdherenceAuditLogDTO adherenceAuditLogDTO) {
        httpClientService.post(reportingApplicationURLs.getAdherenceAuditLogURL(), adherenceAuditLogDTO);
    }

    public void reportAdherenceRecord(AdherenceRecordDTO adherenceRecordDTO) {
        httpClientService.post(reportingApplicationURLs.getAdherenceRecordUpdateURL(), adherenceRecordDTO);
    }

    public void reportAdherenceRecordDelete(List<AdherenceRecordDTO> adherenceRecordDTOs) {
        httpClientService.post(reportingApplicationURLs.getAdherenceRecordDeleteURL(), (Serializable)adherenceRecordDTOs);
    }

    public void reportAdherenceAuditLogDelete(List<AdherenceAuditLogDTO> adherenceAuditLogDTOs){
        httpClientService.post(reportingApplicationURLs.getAuditLogsDeleteURL(), (Serializable)adherenceAuditLogDTOs);
    }

    public void reportDailyAdherenceAuditLogsDelete(List<AdherenceAuditLogDTO> adherenceAuditLogDTOs){
        httpClientService.post(reportingApplicationURLs.getAdherenceRecordDeleteURL(), (Serializable)adherenceAuditLogDTOs);
    }

    public void reportUserGivenPatientDetailsUpdate(UserGivenPatientDetailsReportingRequest userGivenPatientDetailsReportingRequest) {
        httpClientService.post(reportingApplicationURLs.getUpdateUserGivenPatientDetailsURL(), userGivenPatientDetailsReportingRequest);
    }

    public void removeUserGivenPatientDetailsUpdate(UserGivenPatientDetailsReportingRequest userGivenPatientDetailsReportingRequest) {
        httpClientService.post(reportingApplicationURLs.getDeleteUserGivenPatientDetailsURL(), userGivenPatientDetailsReportingRequest);
    }

    public void removePatient(PatientDTO patientDTO) {
        httpClientService.post(reportingApplicationURLs.getDeletePatientURL(), patientDTO);
    }
    public void removeAdherenceOverIVR(String patientID){
        httpClientService.post(reportingApplicationURLs.getAdherencePathDelete(), patientID);
    }
}
