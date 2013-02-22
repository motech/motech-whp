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
@ContextConfiguration(locations = "classpath*:/applicationITContext.xml")
public class ReportingEventURLsIT {

    @Autowired
    ReportingEventURLs reportingEventURLs;

    @Test
    public void shouldGetAdherenceCaptureURL() {
        assertThat(reportingEventURLs.getAdherencePath(), is("http://127.0.0.1:9999/whp-reports/adherence/measure"));
    }

    @Test
    public void shouldGetCallLogMeasureURL() {
        assertThat(reportingEventURLs.getCallLogURL(), is("http://127.0.0.1:9999/whp-reports/adherenceCallLog/measure"));
    }

    @Test
    public void shouldGetCallStatusMeasureURL() {
        assertThat(reportingEventURLs.getCallStatusURL(), is("http://127.0.0.1:9999/whp-reports/adherenceCallLog/status/measure"));
    }

    @Test
    public void shouldGetFlashingLogMeasureURL() {
        assertThat(reportingEventURLs.getFlashingLogURL(), is("http://127.0.0.1:9999/whp-reports/flashingLog/measure"));
    }

    @Test
    public void shouldGetContainerRegistrationMeasureURL() {
        assertThat(reportingEventURLs.getContainerRegistrationURL(), is("http://127.0.0.1:9999/whp-reports/sputumTracking/containerRegistrationMeasure"));
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

    @Test
    public void shouldGetProviderReminderCallLogURL() {
        assertThat(reportingEventURLs.getProviderReminderCallLogURL(), is("http://127.0.0.1:9999/whp-reports/providerReminderCallLog/measure"));
    }

    @Test
    public void shouldGetAdherenceSubmissionURL() {
        assertThat(reportingEventURLs.getAdherenceSubmissionURL(), is("http://127.0.0.1:9999/whp-reports/adherencesubmission/request"));
    }

    @Test
    public void shouldGetPatientReportingUpdateURL() {
        assertThat(reportingEventURLs.getPatientUpdateURL(), is("http://127.0.0.1:9999/whp-reports/patient/update"));
    }

    @Test
    public void shouldGetProviderReportingUpdateURL() {
        assertThat(reportingEventURLs.getProviderUpdateURL(), is("http://127.0.0.1:9999/whp-reports/provider/update"));
    }

    @Test
    public void shouldGetAdherenceAuditLogURL() {
        assertThat(reportingEventURLs.getAdherenceAuditLogURL(), is("http://127.0.0.1:9999/whp-reports/adherence/auditlog"));
    }

    @Test
    public void shouldGetAdherenceRecordUpdateURL() {
        assertThat(reportingEventURLs.getAdherenceRecordUpdateURL(), is("http://127.0.0.1:9999/whp-reports/adherence/record"));
    }
}
