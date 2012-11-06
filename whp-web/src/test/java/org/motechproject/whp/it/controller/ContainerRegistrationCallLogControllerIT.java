package org.motechproject.whp.it.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kubek2k.springockito.annotations.ReplaceWithMock;
import org.kubek2k.springockito.annotations.SpringockitoContextLoader;
import org.mockito.Mock;
import org.motechproject.http.client.service.HttpClientService;
import org.motechproject.whp.common.domain.SputumTrackingInstance;
import org.motechproject.whp.common.util.SpringIntegrationTest;
import org.motechproject.whp.common.util.WHPDateUtil;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.controller.ContainerRegistrationCallLogController;
import org.motechproject.whp.reporting.ReportingEventURLs;
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.motechproject.whp.reports.contract.ContainerRegistrationCallLogRequest;
import org.motechproject.whp.request.IvrContainerRegistrationCallLogRequest;
import org.motechproject.whp.user.builder.ProviderBuilder;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.repository.AllProviders;
import org.motechproject.whp.user.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import java.io.Serializable;

import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.motechproject.whp.common.util.WHPDate.DATE_TIME_FORMAT;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.content;
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
    private ReportingEventURLs reportingEventURLs;

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

        ContainerRegistrationCallLogRequest expectedRequest = new ContainerRegistrationCallLogRequest();
        expectedRequest.setCallId("64756435684375");
        expectedRequest.setDisconnectionType("PROVIDER_HUNGUP");
        expectedRequest.setStartDateTime(WHPDateUtil.toDate("10/12/2012 12:32:35"));
        expectedRequest.setEndDateTime(WHPDateUtil.toDate("10/12/2012 12:33:35"));
        expectedRequest.setProviderId(provider.getProviderId());
        expectedRequest.setMobileNumber("0986754322");

        verify(httpClientService).post(reportingEventURLs.getContainerRegistrationCallLogURL(), expectedRequest);
    }
}
