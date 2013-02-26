package org.motechproject.whp.it.reporting;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.whp.reporting.ReportingApplicationURLs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/applicationITContext.xml")
public class ReportingApplicationURLsIT {

    @Autowired
    ReportingApplicationURLs reportingApplicationURLs;

    @Test
    public void shouldGetAdherenceCaptureURL() {
        assertThat(reportingApplicationURLs.getAdherencePath(), is("http://127.0.0.1:9999/whp-reports/adherence/measure"));
    }

    @Test
    public void shouldGetCallLogMeasureURL() {
        assertThat(reportingApplicationURLs.getCallLogURL(), is("http://127.0.0.1:9999/whp-reports/adherenceCallLog/measure"));
    }

    @Test
    public void shouldGetCallStatusMeasureURL() {
        assertThat(reportingApplicationURLs.getCallStatusURL(), is("http://127.0.0.1:9999/whp-reports/adherenceCallLog/status/measure"));
    }

    @Test
    public void shouldGetFlashingLogMeasureURL() {
        assertThat(reportingApplicationURLs.getFlashingLogURL(), is("http://127.0.0.1:9999/whp-reports/flashingLog/measure"));
    }

    @Test
    public void shouldGetContainerRegistrationMeasureURL() {
        assertThat(reportingApplicationURLs.getContainerRegistrationURL(), is("http://127.0.0.1:9999/whp-reports/sputumTracking/containerRegistrationMeasure"));
    }

    @Test
    public void shouldGetSputumLabResultsCaptureMeasureURL() {
        assertThat(reportingApplicationURLs.getSputumLabResultsCaptureLogURL(), is("http://127.0.0.1:9999/whp-reports/sputumTracking/sputumLabResultsMeasure"));
    }

    @Test
    public void shouldGetContainerStatusUpdateMeasureURL() {
        assertThat(reportingApplicationURLs.getContainerStatusUpdateLogURL(), is("http://127.0.0.1:9999/whp-reports/sputumTracking/containerStatusMeasure"));
    }

    @Test
    public void shouldGetContainerPatientMappingMeasureURL() {
        assertThat(reportingApplicationURLs.getContainerPatientMappingLogURL(), is("http://127.0.0.1:9999/whp-reports/sputumTracking/containerPatientMappingMeasure"));
    }

    @Test
    public void shouldGetContainerRegistrationCallLogURL() {
        assertThat(reportingApplicationURLs.getContainerRegistrationCallDetailsLogURL(), is("http://127.0.0.1:9999/whp-reports/containerRegistrationCallLog/updateCallDetails"));
    }

    @Test
    public void shouldGetProviderVerificationLogURL() {
        assertThat(reportingApplicationURLs.getProviderVerificationLogURL(), is("http://127.0.0.1:9999/whp-reports/containerRegistrationCallLog/providerVerification"));
    }

    @Test
    public void shouldGetContainerVerificationLogURL() {
        assertThat(reportingApplicationURLs.getContainerVerificationLogURL(), is("http://127.0.0.1:9999/whp-reports/containerRegistrationCallLog/containerVerification"));
    }

    @Test
    public void shouldGetProviderReminderCallLogURL() {
        assertThat(reportingApplicationURLs.getProviderReminderCallLogURL(), is("http://127.0.0.1:9999/whp-reports/providerReminderCallLog/measure"));
    }

    @Test
    public void shouldGetAdherenceSubmissionURL() {
        assertThat(reportingApplicationURLs.getAdherenceSubmissionURL(), is("http://127.0.0.1:9999/whp-reports/adherencesubmission/request"));
    }

    @Test
    public void shouldGetPatientReportingUpdateURL() {
        assertThat(reportingApplicationURLs.getPatientUpdateURL(), is("http://127.0.0.1:9999/whp-reports/patient/update"));
    }

    @Test
    public void shouldGetProviderReportingUpdateURL() {
        assertThat(reportingApplicationURLs.getProviderUpdateURL(), is("http://127.0.0.1:9999/whp-reports/provider/update"));
    }

    @Test
    public void shouldGetAdherenceAuditLogURL() {
        assertThat(reportingApplicationURLs.getAdherenceAuditLogURL(), is("http://127.0.0.1:9999/whp-reports/adherence/auditlog"));
    }

    @Test
    public void shouldGetAdherenceRecordUpdateURL() {
        assertThat(reportingApplicationURLs.getAdherenceRecordUpdateURL(), is("http://127.0.0.1:9999/whp-reports/adherence/record"));
    }
}
