package org.motechproject.whp.it.reporting;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.whp.reporting.ReportingEventURLs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/applicationWHPReportingContext.xml")
public class ReportingEventURLsIT {

    @Autowired
    ReportingEventURLs reportingEventURLs;

    @Test
    public void shouldGetAdherenceCaptureURL() {
        assertThat(reportingEventURLs.getAdherenceCallLogURL(), is("http://127.0.0.1:9999/whp-reports/adherence/callLogs"));
    }

    @Test
    public void shouldGetCallLogMeasureURL() {
        assertThat(reportingEventURLs.getCallLogURL(), is("http://127.0.0.1:9999/whp-reports/adherenceCallLog/measure"));
    }

    @Test
    public void shouldGetFlashingLogMeasureURL() {
        assertThat(reportingEventURLs.getFlashingLogURL(), is("http://127.0.0.1:9999/whp-reports/flashingLog/measure"));
    }

    @Test
    public void shouldGetContainerRegistrationLogMeasureURL() {
        assertThat(reportingEventURLs.getContainerRegistrationLogURL(), is("http://127.0.0.1:9999/whp-reports/sputumTracking/containerRegistrationMeasure"));
    }

    @Test
    public void shouldGetSputumLabResultsCaptureMeasureURL() {
        assertThat(reportingEventURLs.getSputumLabResultsCaptureLogURL(), is("http://127.0.0.1:9999/whp-reports/sputumTracking/sputumLabResultsMeasure"));
    }

    @Test
    public void shouldGetContainerStatusUpdateMeasureURL() {
        assertThat(reportingEventURLs.getContainerStatusUpdateLogURL(), is("http://127.0.0.1:9999/whp-reports/sputumTracking/containerStatusMeasure"));
    }

    @Test
    public void shouldGetContainerPatientMappingMeasureURL() {
        assertThat(reportingEventURLs.getContainerPatientMappingLogURL(), is("http://127.0.0.1:9999/whp-reports/sputumTracking/containerPatientMappingMeasure"));
    }

    @Test
    public void shouldGetContainerRegistrationCallLogURL() {
        assertThat(reportingEventURLs.getContainerRegistrationCallDetailsLogURL(), is("http://127.0.0.1:9999/whp-reports/containerRegistrationCallLog/updateCallDetails"));
    }

    @Test
    public void shouldGetProviderVerificationLogURL() {
        assertThat(reportingEventURLs.getProviderVerificationLogURL(), is("http://127.0.0.1:9999/whp-reports/containerRegistrationCallLog/providerVerification"));
    }

    @Test
    public void shouldGetContainerVerificationLogURL() {
        assertThat(reportingEventURLs.getContainerVerificationLogURL(), is("http://127.0.0.1:9999/whp-reports/containerRegistrationCallLog/containerVerification"));
    }
}
