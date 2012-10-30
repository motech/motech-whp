package org.motechproject.whp.it.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kubek2k.springockito.annotations.ReplaceWithMock;
import org.kubek2k.springockito.annotations.SpringockitoContextLoader;
import org.motechproject.http.client.service.HttpClientService;
import org.motechproject.security.authentication.LoginSuccessHandler;
import org.motechproject.security.domain.MotechWebUser;
import org.motechproject.security.service.MotechUser;
import org.motechproject.whp.common.domain.ChannelId;
import org.motechproject.whp.common.domain.SputumTrackingInstance;
import org.motechproject.whp.common.service.RemediProperties;
import org.motechproject.whp.common.util.SpringIntegrationTest;
import org.motechproject.whp.container.builder.request.ContainerRegistrationReportingRequestBuilder;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.container.repository.AllContainers;
import org.motechproject.whp.container.service.ContainerService;
import org.motechproject.whp.containermapping.domain.ContainerRange;
import org.motechproject.whp.containermapping.domain.ProviderContainerMapping;
import org.motechproject.whp.containermapping.repository.AllProviderContainerMappings;
import org.motechproject.whp.controller.ProviderContainerRegistrationController;
import org.motechproject.whp.reporting.ReportingEventURLs;
import org.motechproject.whp.reports.contract.ContainerRegistrationReportingRequest;
import org.motechproject.whp.user.domain.WHPRole;
import org.motechproject.whp.webservice.builder.ProviderRequestBuilder;
import org.motechproject.whp.webservice.request.ProviderWebRequest;
import org.motechproject.whp.webservice.service.ProviderWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.motechproject.whp.common.util.WHPDate.DATE_TIME_FORMAT;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup;

@ContextConfiguration(loader = SpringockitoContextLoader.class, locations = "classpath:META-INF/spring/applicationContext.xml")
public class ProviderContainerRegistrationControllerIT extends SpringIntegrationTest {

    @Autowired
    AllProviderContainerMappings allProviderContainerMappings;

    @Autowired
    ProviderWebService providerWebService;

    @Autowired
    private ProviderContainerRegistrationController providerContainerRegistrationController;

    @Autowired
    private ContainerService containerService;

    @ReplaceWithMock
    @Autowired
    private HttpClientService httpClientService;

    @Autowired
    private RemediProperties remediProperties;

    @Autowired
    private AllContainers allContainers;

    @Autowired
    private ReportingEventURLs reportingEventURLs;

    private final String providerId = "provider";

    private String remediUrl;
    private String apiKey;
    private ProviderContainerMapping providerContainerMapping;

    @Before
    public void setUp() {
        allContainers.removeAll();
        remediUrl = remediProperties.getUrl();
        apiKey = remediProperties.getApiKey();
        providerContainerMapping = new ProviderContainerMapping();
        providerContainerMapping.add(new ContainerRange(10000000000L, 20000000000L));
        providerContainerMapping.setProviderId(providerId);

        allProviderContainerMappings.add(providerContainerMapping);

        ProviderWebRequest whpProviderWeb = new ProviderRequestBuilder().withDefaults().withProviderId(providerId).build();
        providerWebService.createOrUpdate(whpProviderWeb);
        reset(httpClientService);
    }

    @Test
    public void shouldRegisterContainer() throws Exception {
        String containerId = "10000000000";
        SputumTrackingInstance inTreatmentInstance = SputumTrackingInstance.InTreatment;

        List<String> roles = new ArrayList<>();
        roles.add(WHPRole.PROVIDER.name());

        MotechUser testuser = new MotechUser(new MotechWebUser(providerId, null, null, roles));
        standaloneSetup(providerContainerRegistrationController).build()
                .perform(post("/containerRegistration/by_provider/register").param("containerId", containerId).param("instance", inTreatmentInstance.getDisplayText())
                        .sessionAttr(LoginSuccessHandler.LOGGED_IN_USER, testuser))
                .andExpect(status().isOk());

        Container container = containerService.getContainer(containerId);

        ContainerRegistrationReportingRequest expectedContainerRegistrationRequest = new ContainerRegistrationReportingRequestBuilder().forContainer(container).registeredThrough(ChannelId.WEB.name()).withSubmitterId(testuser.getUserName()).withSubmitterRole(WHPRole.PROVIDER.name()).build();

        assertNotNull(container);
        assertThat(container.getProviderId(), is(providerId));
        assertThat(container.getContainerId(), is(containerId));

        String expectedContainerRegistrationXML = String.format("<?xml version=\"1.0\"?>\n" +
                "<case xmlns=\"http://openrosa.org/javarosa\" case_id=\"%s\" date_modified=\"%s\" user_id=\"system\"\n" +
                "      api_key=\"%s\">\n" +
                "    <create>\n" +
                "        <case_type>%s</case_type>\n" +
                "    </create>\n" +
                "    <update>\n" +
                "        <provider_id>%s</provider_id>\n" +
                "    </update>\n" +
                "</case>\n", containerId, container.getCreationTime().toString(DATE_TIME_FORMAT), apiKey, inTreatmentInstance.name(), providerId);

        verify(httpClientService).post(remediUrl, expectedContainerRegistrationXML);
        verify(httpClientService).post(reportingEventURLs.getContainerRegistrationLogURL(), expectedContainerRegistrationRequest);

        markForDeletion(container);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(httpClientService);
        allProviderContainerMappings.remove(providerContainerMapping);
        allContainers.removeAll();
    }

}
