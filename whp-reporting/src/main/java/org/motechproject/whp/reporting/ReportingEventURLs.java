package org.motechproject.whp.reporting;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ReportingEventURLs {

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

    public String getAdherencePath() {
        return adherencePath;
    }

    public String getCallLogURL() {
        return callLogURL;
    }

    public String getCallStatusURL() {
        return callStatusURL;
    }

    public String getFlashingLogURL() {
        return flashingLogURL;
    }

    public String getContainerRegistrationLogURL() {
        return containerRegistrationURL;
    }

    public String getSputumLabResultsCaptureLogURL() {
        return sputumLabResultsCaptureLogURL;
    }

    public String getContainerStatusUpdateLogURL() {
        return containerStatusUpdateLogURL;
    }

    public String getContainerPatientMappingLogURL() {
        return containerPatientMappingLogURL;
    }

    public String getContainerRegistrationCallDetailsLogURL() {
        return containerRegistrationCallDetailsLogURL;
    }

    public String getProviderVerificationLogURL() {
        return providerVerificationLogURL;
    }

    public String getContainerVerificationLogURL() {
        return containerVerificationLogURL;
    }

    public String getProviderReminderCallLogURL() {
        return providerReminderCallLogURL;
    }

    public String getAdherenceSubmissionURL() {
        return adherenceSubmissionURL;
    }

    public String getPatientUpdateURL() {
        return patientUpdateURL;
    }

    public String getProviderUpdateURL() {
        return providerUpdateURL;
    }

    public String getAdherenceAuditLogURL() {
        return adherenceAuditLogURL;
    }

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

}
