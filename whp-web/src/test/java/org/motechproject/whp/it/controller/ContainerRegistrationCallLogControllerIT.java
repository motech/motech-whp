package org.motechproject.whp.it.controller;

import org.junit.Before;
import org.junit.Test;
import org.kubek2k.springockito.annotations.ReplaceWithMock;
import org.kubek2k.springockito.annotations.SpringockitoContextLoader;
import org.motechproject.http.client.service.HttpClientService;
import org.motechproject.whp.common.util.SpringIntegrationTest;
import org.motechproject.whp.controller.ContainerRegistrationCallLogController;
import org.motechproject.whp.reporting.ReportingApplicationURLs;
import org.motechproject.whp.reports.contract.ContainerRegistrationCallDetailsLogRequest;
import org.motechproject.whp.user.builder.ProviderBuilder;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.repository.AllProviders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup;

@ContextConfiguration(loader = SpringockitoContextLoader.class, locations = "classpath:META-INF/spring/applicationContext.xml")
public class ContainerRegistrationCallLogControllerIT extends SpringIntegrationTest {

    @Autowired
    private ContainerRegistrationCallLogController containerRegistrationCallLogController;

    @ReplaceWithMock
    @Autowired
    private HttpClientService httpClientService;

    @Autowired
    private ReportingApplicationURLs reportingApplicationURLs;

    @Autowired
    private AllProviders allProviders;
    @Before
    public void setUp() {
        reset(httpClientService);
    }

    @Test
    public void shouldPublishCallLogRequest() throws Exception {
        Provider provider = new ProviderBuilder().withDefaults().withPrimaryMobileNumber("0986754322").build();
        allProviders.add(provider);
        markForDeletion(provider);

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
                                .contentType(MediaType.APPLICATION_XML)

                ).andExpect(status().isOk());

        ContainerRegistrationCallDetailsLogRequest expectedRequest = new ContainerRegistrationCallDetailsLogRequest();
        expectedRequest.setCallId("64756435684375");
        expectedRequest.setDisconnectionType("PROVIDER_HUNGUP");
        expectedRequest.setStartDateTime("10/12/2012 12:32:35");
        expectedRequest.setEndDateTime("10/12/2012 12:33:35");
        expectedRequest.setProviderId(provider.getProviderId());
        expectedRequest.setMobileNumber("0986754322");

        verify(httpClientService).post(reportingApplicationURLs.getContainerRegistrationCallDetailsLogURL(), expectedRequest);
    }
}
