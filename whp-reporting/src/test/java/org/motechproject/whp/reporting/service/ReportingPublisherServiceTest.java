package org.motechproject.whp.reporting.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.http.client.service.HttpClientService;
import org.motechproject.scheduler.context.EventContext;
import org.motechproject.whp.reporting.ReportingEventKeys;
import org.motechproject.whp.reporting.request.AdherenceCaptureRequest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;


public class ReportingPublisherServiceTest {

    ReportingPublisherService reportingPublisher;

    @Mock
    HttpClientService httpClientService;

    @Before
    public void setUp() {
        initMocks(this);
        reportingPublisher = new ReportingPublisherService(httpClientService);
    }

    @Test
    public void shouldPublishAdherenceCaptureWithTheRightSubject() throws Exception {
        String providerId = "123456";
        String patientId = "abc12345";
        AdherenceCaptureRequest adherenceCaptureRequest = new AdherenceCaptureRequest(providerId,patientId,2);
        reportingPublisher.reportAdherenceCapture(adherenceCaptureRequest);

        verify(httpClientService).post(ReportingEventKeys.REPORT_ADHERENCE_CAPTURE, adherenceCaptureRequest);
    }
}
