package org.motechproject.whp.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.motechproject.whp.reports.contract.ProviderReminderCallLogRequest;
import org.motechproject.whp.user.builder.ProviderBuilder;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.service.ProviderService;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.http.MediaType.APPLICATION_XML;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup;

public class ProviderReminderCallLogControllerTest {

    ProviderReminderCallLogController providerReminderCallLogController;

    @Mock
    ReportingPublisherService reportingPublisherService;
    @Mock
    ProviderService providerService;

    @Before
    public void setUp() {
        initMocks(this);
        providerReminderCallLogController = new ProviderReminderCallLogController(reportingPublisherService,providerService);
    }

    @Test
    public void shouldPublishProviderCallStatusToReporting() throws Exception {

        String requestXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<provider_reminder_call_status>\n" +
                "    <request_id>request_id</request_id>\n" +
                "    <call_id>call_id</call_id>\n" +
                "    <reminder_type>ADHERENCE_WINDOW_COMMENCED</reminder_type>\n" +
                "    <msisdn>1234567890</msisdn>\n" +
                "    <call_answered>YES</call_answered>\n" +
                "    <disconnection_type>PROVIDER_HANGUP</disconnection_type>\n" +
                "    <start_time>10/12/2012 12:32:35</start_time>\n" +
                "    <end_time>10/12/2012 12:33:35</end_time> \n" +
                "    <attempt_time>10/12/2012 12:21:45</attempt_time>" +
                "    <attempt>1</attempt>\n" +
                "</provider_reminder_call_status>\n";

        Provider provider = new ProviderBuilder().withProviderId("providerId").build();
        when(providerService.findByMobileNumber("1234567890")).thenReturn(provider);

        standaloneSetup(providerReminderCallLogController)
                .build()
                .perform(
                        post("/providerreminder/calllog")
                                .body(requestXML.getBytes())
                                .contentType(APPLICATION_XML)
                ).andExpect(content().string(""))
                .andExpect(status().isOk());

        ArgumentCaptor<ProviderReminderCallLogRequest> captor = ArgumentCaptor.forClass(ProviderReminderCallLogRequest.class);
        verify(reportingPublisherService).reportProviderReminderCallLog(captor.capture());

        ProviderReminderCallLogRequest request = captor.getValue();
        assertEquals("request_id", request.getRequestId());
        assertEquals("call_id", request.getCallId());
        assertEquals("ADHERENCE_WINDOW_COMMENCED", request.getReminderType());
        assertEquals("1234567890", request.getMsisdn());
        assertEquals("YES", request.getCallAnswered());
        assertEquals("PROVIDER_HANGUP", request.getDisconnectionType());
        assertEquals("1", request.getAttempt());
        assertEquals("10/12/2012 12:32:35", request.getStartTime());
        assertEquals("10/12/2012 12:33:35", request.getEndTime());
        assertEquals("10/12/2012 12:21:45", request.getAttemptTime());

    }
}
