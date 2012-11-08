package org.motechproject.whp.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.motechproject.whp.reports.contract.ContainerRegistrationCallDetailsLogRequest;
import org.motechproject.whp.request.IvrContainerRegistrationCallLogRequest;
import org.motechproject.whp.user.builder.ProviderBuilder;
import org.motechproject.whp.user.service.ProviderService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
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


        ArgumentCaptor<ContainerRegistrationCallDetailsLogRequest> captor = forClass(ContainerRegistrationCallDetailsLogRequest.class);

        verify(reportingPublisherService).reportContainerRegistrationCallDetailsLog(captor.capture());
        verify(providerService).findByMobileNumber(mobileNumber);

        ContainerRegistrationCallDetailsLogRequest callLogRequest = captor.getValue();
        assertEquals(mobileNumber, callLogRequest.getMobileNumber());
        assertEquals("64756435684375", callLogRequest.getCallId());
        assertEquals("PROVIDER_HUNGUP", callLogRequest.getDisconnectionType());
        assertEquals("10/12/2012 12:32:35", callLogRequest.getStartDateTime());
        assertEquals("10/12/2012 12:33:35", callLogRequest.getEndDateTime());
        assertEquals(providerId, callLogRequest.getProviderId());
    }

    @Test
    public void shouldHandleCallLogRequestsWithInvalidMobileNumber() {
        String invalidMobileNumber = "98121210";
        IvrContainerRegistrationCallLogRequest request = new IvrContainerRegistrationCallLogRequest();
        request.setMobileNumber(invalidMobileNumber);

        containerRegistrationCallLogController.recordCallLog(request);
        when(providerService.findByMobileNumber(invalidMobileNumber)).thenReturn(null);

        ArgumentCaptor<ContainerRegistrationCallDetailsLogRequest> captor = forClass(ContainerRegistrationCallDetailsLogRequest.class);
        verify(reportingPublisherService).reportContainerRegistrationCallDetailsLog(captor.capture());
        verify(providerService).findByMobileNumber(invalidMobileNumber);

        ContainerRegistrationCallDetailsLogRequest callLogRequest = captor.getValue();

        assertEquals(invalidMobileNumber, callLogRequest.getMobileNumber());
        assertNull(callLogRequest.getProviderId());
    }

}
