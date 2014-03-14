package org.motechproject.whp.reporting;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ReportingApplicationURLs {

    private String whpReportsURL;
    private String adherencePath;
    private String callLogURL;
    private String flashingLogURL;
    private String containerRegistrationURL;
    private String sputumLabResultsCaptureLogURL;
    private String containerStatusUpdateLogURL;
    private String containerPatientMappingLogURL;
    private String containerRegistrationCallDetailsLogURL;
    private String providerVerificationLogURL;
    private String containerVerificationLogURL;
    private String providerReminderCallLogURL;
    private String adherenceSubmissionURL;
    private String callStatusURL;
    private String patientUpdateURL;
    private String providerUpdateURL;
    private String adherenceAuditLogURL;
    private String adherenceRecordUpdateURL;
    private String providerAdherenceStatusByDistrictURL;
    private String patientAdherenceMissingDataURL;
    private String updateUserGivenPatientDetailsURL;
    private String adherenceRecordDeleteURL;
    private String auditLogsDeleteURL;
    private String dailyAdherenceAuditLogsURL;
    private String deleteUserGivenPatientDetailsURL;
    private String deletePatientURL;
    private String adherencePathDelete;

    @Value("${whp.reports.adherence}")
    public void setAdherencePath(String adherencePath) {
        this.adherencePath = adherencePath;
    }

    @Value("${whp.reports.url}")
    public void setWhpReportsURL(String whpReportsURL) {
        this.whpReportsURL = whpReportsURL;
    }

    @Value("${whp.reports.callLog}")
    public void setCallLogURL(String callLogURL) {
        this.callLogURL = callLogURL;
    }

    @Value("${whp.reports.adherenceCallStatus}")
    public void setCallStatusURL(String callStatusURL) {
        this.callStatusURL = callStatusURL;
    }

    @Value("${whp.reports.flashingLog}")
    public void setFlashingLogURL(String flashingLogURL) {
        this.flashingLogURL = flashingLogURL;
    }

    @Value("${whp.reports.containerRegistration}")
    public void setContainerRegistrationURL(String containerRegistrationURL) {
        this.containerRegistrationURL = containerRegistrationURL;
    }

    @Value("${whp.reports.labResultsCapture}")
    public void setSputumLabResultsCaptureLogURL(String sputumLabResultsCaptureLogURL) {
        this.sputumLabResultsCaptureLogURL = sputumLabResultsCaptureLogURL;
    }

    @Value("${whp.reports.containerStatusUpdate}")
    public void setContainerStatusUpdateLogURL(String containerStatusUpdateLogURL) {
        this.containerStatusUpdateLogURL = containerStatusUpdateLogURL;
    }

    @Value("${whp.reports.containerPatientMapping}")
    public void setContainerPatientMappingLogURL(String containerPatientMappingLogURL) {
        this.containerPatientMappingLogURL = containerPatientMappingLogURL;
    }

    @Value("${whp.reports.updateUserGivenPatientDetails}")
    public void setUpdateContainerUserGivenPatientDetailsURL(String userGivenPatientDetailsURL) {
        this.updateUserGivenPatientDetailsURL = userGivenPatientDetailsURL;
    }

    @Value("${whp.reports.containerRegistrationCallDetailsLog}")
    public void setContainerRegistrationCallDetailsLogURL(String containerRegistrationCallDetailsLogURL) {
        this.containerRegistrationCallDetailsLogURL = containerRegistrationCallDetailsLogURL;
    }

    @Value("${whp.reports.providerVerificationLog}")
    public void setProviderVerificationLogURL(String providerVerificationCallLogURL) {
        this.providerVerificationLogURL = providerVerificationCallLogURL;
    }

    @Value("${whp.reports.containerVerificationLog}")
    public void setContainerVerificationLogURL(String containerVerificationCallURL) {
        this.containerVerificationLogURL = containerVerificationCallURL;
    }

    @Value("${whp.reports.providerReminderCallLog}")
    public void setProviderReminderCallLogURL(String providerReminderCallLogURL) {
        this.providerReminderCallLogURL = providerReminderCallLogURL;
    }

    @Value("${whp.reports.adherenceSubmission}")
    public void setAdherenceSubmissionURL(String adherenceSubmissionURL) {
        this.adherenceSubmissionURL = adherenceSubmissionURL;
    }

    @Value("${whp.reports.patient.update}")
    public void setPatientUpdateURL(String patientUpdateURL) {
        this.patientUpdateURL = patientUpdateURL;
    }

    @Value("${whp.reports.provider.update}")
    public void setProviderUpdateURL(String providerUpdateURL) {
        this.providerUpdateURL = providerUpdateURL;
    }

    @Value("${whp.reports.adherence.auditLog}")
    public void setAdherenceAuditLogURL(String adherenceAuditLogURL) {
        this.adherenceAuditLogURL = adherenceAuditLogURL;
    }

    @Value("${whp.reports.adherence.record}")
    public void setAdherenceRecordUpdateURL(String adherenceRecordUpdateURL) {
        this.adherenceRecordUpdateURL = adherenceRecordUpdateURL;
    }

    @Value("${whp.reports.provider.adherence.status.by.district}")
    public void setProviderAdherenceStatusByDistrictURL(String providerAdherenceStatusURL) {
        this.providerAdherenceStatusByDistrictURL = providerAdherenceStatusURL;
    }

    @Value("${whp.reports.patient.missing.adherence}")
    public void setPatientAdherenceMissingDataURL(String patientAdherenceMissingDataURL) {
        this.patientAdherenceMissingDataURL = patientAdherenceMissingDataURL;
    }

    @Value("${whp.reports.adherence.delete}")
    public void setAdherenceRecordDeleteURL(String adherenceRecordDeleteURL) {
        this.adherenceRecordDeleteURL = adherenceRecordDeleteURL;
    }

    @Value("${whp.reports.auditLog.delete}")
    public void setAuditLogsDeleteURLURL(String auditLogsDeleteURL) {
        this.auditLogsDeleteURL = auditLogsDeleteURL;
    }

    @Value("${whp.reports.dailyAdherenceAuditLogs.delete}")
    public void setDailyAdherenceAuditLogsURL(String dailyAdherenceAuditLogsURL) {
        this.dailyAdherenceAuditLogsURL = dailyAdherenceAuditLogsURL;
    }

    @Value("${whp.reports.deleteUserGivenPatientDetails}")
    public void setDeleteUserGivenPatientDetailsURL(String deleteUserGivenPatientDetailsURL) {
        this.deleteUserGivenPatientDetailsURL = deleteUserGivenPatientDetailsURL;
    }


    @Value("${whp.reports.patient.delete}")
    public void setDeletePatientURL(String deletePatientURL) {
        this.deletePatientURL = deletePatientURL;
    }

    @Value("${whp.reports.adherencePath.delete}")
    public void setAdherencePathDelete(String adherencePathDelete) {
        this.adherencePathDelete = adherencePathDelete;
    }

}
