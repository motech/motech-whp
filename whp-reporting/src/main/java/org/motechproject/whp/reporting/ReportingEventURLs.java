package org.motechproject.whp.reporting;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ReportingEventURLs {

    private String whpReportsURL;
    private String adherenceCallLogPath;
    private String callLogURL;
    private String flashingLogURL;
    private String containerRegistrationURL;
    private String sputumLabResultsCaptureLogURL;
    private String containerStatusUpdateLogURL;
    private String containerPatientMappingLogURL;
    private String containerRegistrationCallDetailsLogURL;
    private String providerVerificationLogURL;
    private String containerVerificationLogURL;

    public String getAdherenceCallLogURL() {
        return whpReportsURL + adherenceCallLogPath;
    }

    public String getCallLogURL() {
        return whpReportsURL + callLogURL;
    }

    public String getFlashingLogURL() {
        return whpReportsURL + flashingLogURL;
    }

    public String getContainerRegistrationLogURL() {
        return whpReportsURL + containerRegistrationURL;
    }

    public String getSputumLabResultsCaptureLogURL() {
        return whpReportsURL + sputumLabResultsCaptureLogURL;
    }

    public String getContainerStatusUpdateLogURL() {
        return whpReportsURL + containerStatusUpdateLogURL;
    }

    public String getContainerPatientMappingLogURL() {
        return whpReportsURL + containerPatientMappingLogURL;
    }

    public String getContainerRegistrationCallDetailsLogURL() {
        return whpReportsURL + containerRegistrationCallDetailsLogURL;
    }

    public String getProviderVerificationLogURL() {
        return whpReportsURL + providerVerificationLogURL;
    }

    public String getContainerVerificationLogURL() {
        return whpReportsURL + containerVerificationLogURL;
    }

    @Value("${whp.reports.adherenceCallLog}")
    public void setAdherenceCallLogPath(String adherenceCallLogPath) {
        this.adherenceCallLogPath = adherenceCallLogPath;
    }

    @Value("${whp.reports.url}")
    public void setWhpReportsURL(String whpReportsURL) {
        this.whpReportsURL = whpReportsURL;
    }

    @Value("${whp.reports.callLog}")
    public void setCallLogURL(String callLogURL) {
        this.callLogURL = callLogURL;
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
}
