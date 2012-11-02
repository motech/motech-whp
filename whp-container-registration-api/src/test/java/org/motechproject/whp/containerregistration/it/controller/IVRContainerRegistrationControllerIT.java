package org.motechproject.whp.containerregistration.it.controller;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kubek2k.springockito.annotations.ReplaceWithMock;
import org.kubek2k.springockito.annotations.SpringockitoContextLoader;
import org.motechproject.http.client.service.HttpClientService;
import org.motechproject.security.exceptions.WebSecurityException;
import org.motechproject.whp.common.domain.ChannelId;
import org.motechproject.whp.common.domain.SputumTrackingInstance;
import org.motechproject.whp.common.service.RemediProperties;
import org.motechproject.whp.common.util.SpringIntegrationTest;
import org.motechproject.whp.container.builder.request.ContainerRegistrationReportingRequestBuilder;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.container.service.ContainerService;
import org.motechproject.whp.containermapping.domain.ContainerRange;
import org.motechproject.whp.containermapping.domain.ProviderContainerMapping;
import org.motechproject.whp.containermapping.repository.AllProviderContainerMappings;
import org.motechproject.whp.containerregistration.api.webservice.IVRContainerRegistrationController;
import org.motechproject.whp.reporting.ReportingEventURLs;
import org.motechproject.whp.reports.contract.ContainerRegistrationReportingRequest;
import org.motechproject.whp.user.domain.WHPRole;
import org.motechproject.whp.user.service.ProviderService;
import org.motechproject.whp.webservice.builder.ProviderRequestBuilder;
import org.motechproject.whp.webservice.request.ProviderWebRequest;
import org.motechproject.whp.webservice.service.ProviderWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.motechproject.whp.common.domain.ChannelId.IVR;
import static org.motechproject.whp.common.util.WHPDate.DATE_TIME_FORMAT;
import static org.motechproject.whp.user.domain.WHPRole.PROVIDER;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup;

@ContextConfiguration(loader = SpringockitoContextLoader.class, locations = "classpath:applicationContainerRegistrationApiContext.xml")
public class IVRContainerRegistrationControllerIT extends SpringIntegrationTest {

    @Autowired
    private IVRContainerRegistrationController IVRContainerRegistrationController;
    @Autowired
    AllProviderContainerMappings allProviderContainerMappings;
    @Autowired
    private ProviderWebService providerWebService;
    @Autowired
    private ProviderService providerService;
    @Autowired
    private ContainerService containerService;
    @Autowired
    private RemediProperties remediProperties;
    @Autowired
    private ReportingEventURLs reportingEventURLs;

    @ReplaceWithMock
    @Autowired
    private HttpClientService httpClientService;
    private final String providerId = "provider";
    private String remediUrl;
    private String apiKey;

    @Before
    public void setUp() throws WebSecurityException {
        remediUrl = remediProperties.getUrl();
        apiKey = remediProperties.getApiKey();
        ProviderContainerMapping providerContainerMapping = new ProviderContainerMapping();
        providerContainerMapping.add(new ContainerRange(76862367681L, 76862367691L));
        providerContainerMapping.setProviderId(providerId);
        allProviderContainerMappings.add(providerContainerMapping);

        String primaryMobile = "0986754322";
        ProviderWebRequest whpProviderWeb = new ProviderRequestBuilder().withDefaults().withProviderId(providerId).withPrimaryMobile(primaryMobile).build();
        providerWebService.createOrUpdate(whpProviderWeb);

        markForDeletion(providerContainerMapping);
        markForDeletion(providerService.findByProviderId(providerId));
        reset(httpClientService);
    }

    @Test
    public void shouldRegisterTheContainer() throws Exception {
        String containerId = "76862367681";
        SputumTrackingInstance inTreatment = SputumTrackingInstance.PreTreatment;

        standaloneSetup(IVRContainerRegistrationController)
                .build()
                .perform(
                        post("/ivr/containerRegistration/register")
                                .body(readXML("/validIVRContainerRegistrationRequest.xml"))
                                .contentType(MediaType.APPLICATION_XML)

                ).andExpect(status().isOk())
                .andExpect(
                        content().string(containsString("success"))
                );

        Container container = containerService.getContainer(containerId);

        assertNotNull(container);
        assertThat(container.getProviderId(), is(providerId));
        assertThat(container.getContainerId(), is(containerId));

        String expectedContainerRegistrationXML = String.format("<?xml version=\"1.0\"?>\n" +
                "<case xmlns=\"http://openrosa.org/javarosa\" case_id=\"%s\" date_modified=\"%s\" user_id=\"motech\"\n" +
                "      api_key=\"%s\">\n" +
                "    <create>\n" +
                "        <case_type>%s</case_type>\n" +
                "    </create>\n" +
                "    <update>\n" +
                "        <provider_id>%s</provider_id>\n" +
                "    </update>\n" +
                "</case>\n", containerId, container.getCreationTime().toString(DATE_TIME_FORMAT), apiKey, inTreatment.name(), providerId);

        verify(httpClientService).post(remediUrl, expectedContainerRegistrationXML);
        markForDeletion(container);
        verifyReportingEventPublication(container);
    }

    private void verifyReportingEventPublication(Container container) {
        ContainerRegistrationReportingRequest expectedContainerRegistrationRequest = new ContainerRegistrationReportingRequestBuilder().forContainer(container).registeredThrough(ChannelId.IVR.name()).withSubmitterId(providerId).withSubmitterRole(WHPRole.PROVIDER.name()).build();
        assertEquals(container.getContainerId(), expectedContainerRegistrationRequest.getContainerId());
        assertEquals(IVR.name(), expectedContainerRegistrationRequest.getChannelId());
        assertEquals(container.getStatus().name(), expectedContainerRegistrationRequest.getStatus());
        assertEquals(container.getContainerIssuedDate().toDate(), expectedContainerRegistrationRequest.getIssuedOn());
        assertEquals(container.getDiagnosis().name(), expectedContainerRegistrationRequest.getDiagnosis());
        assertEquals(container.getInstance().name(), expectedContainerRegistrationRequest.getInstance());
        assertEquals(container.getDistrict(), expectedContainerRegistrationRequest.getLocationId());
        assertEquals(container.getProviderId(), expectedContainerRegistrationRequest.getProviderId());
        assertEquals(providerId, expectedContainerRegistrationRequest.getSubmitterId());
        assertEquals(PROVIDER.name(), expectedContainerRegistrationRequest.getSubmitterRole());
        verify(httpClientService).post(reportingEventURLs.getContainerRegistrationLogURL(), expectedContainerRegistrationRequest);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(httpClientService);
    }

    private byte[] readXML(String xmlPath) throws IOException {
        return IOUtils.toByteArray(this.getClass().getResourceAsStream(xmlPath));
    }
}