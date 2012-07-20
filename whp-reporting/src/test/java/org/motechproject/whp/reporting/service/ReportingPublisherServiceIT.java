package org.motechproject.whp.reporting.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kubek2k.springockito.annotations.ReplaceWithMock;
import org.kubek2k.springockito.annotations.SpringockitoContextLoader;
import org.motechproject.whp.reporting.gateway.ReportingGateway;
import org.motechproject.whp.reporting.request.AdherenceCaptureRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.mockito.Mockito.verify;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = SpringockitoContextLoader.class, locations = "classpath:applicationWHPReportingContext.xml")
//Tests the wiring of JMS queue for sending reporting events
public class ReportingPublisherServiceIT {

    @Autowired
    ReportingPublisherService reportingPublisherService;

    @ReplaceWithMock
    @Autowired
    ReportingGateway reportingGateway;

    @Test
    public void shouldPublishMessageToGateway() throws Exception {

        String providerId = "123456";
        String patientId = "abc12345";
        AdherenceCaptureRequest adherenceCaptureRequest = new AdherenceCaptureRequest(providerId,patientId,2);
        reportingPublisherService.reportAdherenceCapture(adherenceCaptureRequest);
        // Might need a Thread.sleep(1000) on fast environments
        verify(reportingGateway).captureAdherence(adherenceCaptureRequest);
    }
}
