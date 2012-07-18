package org.motechproject.whp.reporting.gateway;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.motechproject.whp.reporting.request.AdherenceCaptureRequest;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ReportingGatewayTest {
    @Mock
    private RestTemplate restTemplate;

    @Mock
    private Properties whpProperties;

    @Captor
    private ArgumentCaptor<Class<String>> responseTypeArgumentCaptor;

    @Captor
    private ArgumentCaptor<HashMap<String,String>> urlVariablesArgumentCaptor;

    @Before
    public void setUp() {
        initMocks(this);
    }


    @Test
    public void shouldSendAdherenceCaptureRequestToTheRightUrl() throws Exception {

        when(whpProperties.getProperty("reporting.service.base.url")).thenReturn("whp-reports");
        new ReportingGateway(restTemplate,whpProperties).captureAdherence(new AdherenceCaptureRequest("123456","abcd1234",4));

        ArgumentCaptor<String> urlArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<AdherenceCaptureRequest> adherenceReportRequestArgumentCaptor = ArgumentCaptor.forClass(AdherenceCaptureRequest.class);

        verify(restTemplate).postForLocation(urlArgumentCaptor.capture(), adherenceReportRequestArgumentCaptor.capture(), responseTypeArgumentCaptor.capture(), urlVariablesArgumentCaptor.capture());
        AdherenceCaptureRequest adherenceCreationReportRequest = adherenceReportRequestArgumentCaptor.getValue();

        assertEquals("whp-reports/adherence/capture", urlArgumentCaptor.getValue());

    }
}
