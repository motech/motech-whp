package org.motechproject.whp.reporting;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ReportingEventURLs {

    private String whpReportsURL;
    private String adherenceCallLogPath;

    public String getAdherenceCallLogURL() {
        return whpReportsURL + adherenceCallLogPath;
    }

    @Value("${whp.reports.adherenceCallLog}")
    public void setAdherenceCallLogPath(String adherenceCallLogPath) {
        this.adherenceCallLogPath = adherenceCallLogPath;
    }

    @Value("${whp.reports.url}")
    public void setWhpReportsURL(String whpReportsURL) {
        this.whpReportsURL = whpReportsURL;
    }
}
