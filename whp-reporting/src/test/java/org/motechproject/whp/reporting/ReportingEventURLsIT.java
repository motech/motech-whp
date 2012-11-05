package org.motechproject.whp.reporting;

import org.junit.Test;
import org.junit.runner.RunWith;
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
        assertThat(reportingEventURLs.getAdherenceCallLogURL(), is("http://localhost:8080/whp-reports/adherence/callLogs"));
    }

    @Test
    public void shouldGetCallLogMeasureURL() {
        assertThat(reportingEventURLs.getCallLogURL(), is("http://localhost:8080/whp-reports/adherenceCallLog/measure"));
    }

    @Test
    public void shouldGetFlashingLogMeasureURL() {
        assertThat(reportingEventURLs.getFlashingLogURL(), is("http://localhost:8080/whp-reports/flashingLog/measure"));
    }

    @Test
    public void shouldGetContainerRegistrationLogMeasureURL() {
        assertThat(reportingEventURLs.getContainerRegistrationLogURL(), is("http://localhost:8080/whp-reports/sputumTracking/containerRegistrationMeasure"));
    }

    @Test
    public void shouldGetSputumLabResultsCaptureMeasureURL() {
        assertThat(reportingEventURLs.getSputumLabResultsCaptureLogURL(), is("http://localhost:8080/whp-reports/sputumTracking/sputumLabResultsMeasure"));
    }

    @Test
    public void shouldGetContainerStatusUpdateMeasureURL() {
        assertThat(reportingEventURLs.getContainerStatusUpdateLogURL(), is("http://localhost:8080/whp-reports/sputumTracking/containerStatusMeasure"));
    }

    @Test
    public void shouldGetContainerPatientMappingMeasureURL() {
        assertThat(reportingEventURLs.getContainerPatientMappingLogURL(), is("http://localhost:8080/whp-reports/sputumTracking/containerPatientMappingMeasure"));
    }

    @Test
    public void shouldGetContainerRegistrationCallLogURL() {
        assertThat(reportingEventURLs.getContainerRegistrationCallLogURL(), is("http://localhost:8080/whp-reports/containerRegistrationCallLog/create"));
    }

}
