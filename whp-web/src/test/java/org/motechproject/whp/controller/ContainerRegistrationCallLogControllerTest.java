package org.motechproject.whp.controller;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.motechproject.whp.reports.contract.ContainerRegistrationCallLogRequest;
import org.motechproject.whp.user.builder.ProviderBuilder;
import org.motechproject.whp.user.service.ProviderService;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.common.util.WHPDate.DATE_TIME_FORMAT;
import static org.springframework.http.MediaType.APPLICATION_XML;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup;

public class ContainerRegistrationCallLogControllerTest {

    private ContainerRegistrationCallLogController containerRegistrationCallLogController;

    @Mock
    private ReportingPublisherService reportingPublisherService;

    @Mock
    private ProviderService providerService;

    @Before
    public void setUp() {
        initMocks(this);
        containerRegistrationCallLogController = new ContainerRegistrationCallLogController(reportingPublisherService, providerService);
    }

    @Test
    public void shouldHandleContainerRegistrationCallLogRequests() throws Exception {
        String providerId = "providerid";
        String mobileNumber = "0986754322";

        when(providerService.findByMobileNumber(mobileNumber)).thenReturn(new ProviderBuilder().withDefaults().withId(providerId).build());

        String callLogRequestXML =
                "<call_log>\n" +
                        "    <msisdn>0986754322</msisdn>\n" +
                        "    <start_time>10/12/2012 12:32:35</start_time >\n" +
                        "    <end_time>10/12/2012 12:33:35</end_time >\n" +
                        "   <call_id>64756435684375</call_id>\n" +
                        "   <disconnect_type>PROVIDER_HUNGUP</disconnect_type>\n" +
                        "</call_log >";

        standaloneSetup(containerRegistrationCallLogController)
                .build()
                .perform(
                        post("/ivr/containerRegistration/callLog")
                                .body(callLogRequestXML.getBytes())
                                .contentType(APPLICATION_XML)
                ).andExpect(content().string(""))
                .andExpect(status().isOk());


        ArgumentCaptor<ContainerRegistrationCallLogRequest> captor = forClass(ContainerRegistrationCallLogRequest.class);

        verify(reportingPublisherService).reportContainerRegistrationCallLog(captor.capture());
        verify(providerService).findByMobileNumber(mobileNumber);

        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(DATE_TIME_FORMAT);

        ContainerRegistrationCallLogRequest callLogRequest = captor.getValue();
        assertEquals(mobileNumber, callLogRequest.getMobileNumber());
        assertEquals("64756435684375", callLogRequest.getCallId());
        assertEquals("PROVIDER_HUNGUP", callLogRequest.getDisconnectionType());
        assertEquals(dateTimeFormatter.parseDateTime("10/12/2012 12:32:35").toDate(), callLogRequest.getStartDateTime());
        assertEquals(dateTimeFormatter.parseDateTime("10/12/2012 12:33:35").toDate(), callLogRequest.getEndDateTime());
        assertEquals(providerId, callLogRequest.getProviderId());
    }

}
