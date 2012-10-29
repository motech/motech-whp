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
}
