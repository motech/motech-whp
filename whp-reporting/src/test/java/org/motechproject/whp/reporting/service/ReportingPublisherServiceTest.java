package org.motechproject.whp.reporting.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.http.client.service.HttpClientService;
import org.motechproject.whp.reporting.ReportingEventURLs;
import org.motechproject.whp.reports.webservice.request.AdherenceCaptureRequest;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;


public class ReportingPublisherServiceTest {

    ReportingPublisherService reportingPublisher;

    @Mock
    HttpClientService httpClientService;

    @Mock
    ReportingEventURLs reportingEventURLs;

    @Before
    public void setUp() {
        initMocks(this);
        reportingPublisher = new ReportingPublisherService(httpClientService, reportingEventURLs);
    }

    @Test
    public void shouldPublishAdherenceCaptureWithTheRightSubject() throws Exception {
        String providerId = "123456";
        String patientId = "abc12345";
        AdherenceCaptureRequest adherenceCaptureRequest = new AdherenceCaptureRequest();
        adherenceCaptureRequest.setPatientId(patientId);
        adherenceCaptureRequest.setProviderId(providerId);

        reportingPublisher.reportAdherenceCapture(adherenceCaptureRequest);
        verify(httpClientService).post(reportingEventURLs.getAdherenceCallLogURL(), adherenceCaptureRequest);
    }
}
