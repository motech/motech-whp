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
import org.motechproject.whp.common.domain.District;
import org.motechproject.whp.common.domain.RegistrationInstance;
import org.motechproject.whp.common.repository.AllDistricts;
import org.motechproject.whp.common.service.RemediProperties;
import org.motechproject.whp.common.util.SpringIntegrationTest;
import org.motechproject.whp.container.builder.request.ContainerRegistrationReportingRequestBuilder;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.container.domain.ContainerId;
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
import static org.mockito.Mockito.*;
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

    @Autowired
    private AllDistricts allDistricts;

    private final String providerId = "provider";

    private String remediUrl;
    private String apiKey;
    private ProviderContainerMapping providerContainerMapping;
    private District district;

    @Before
    public void setUp() {
        allContainers.removeAll();
        remediUrl = remediProperties.getUrl();
        apiKey = remediProperties.getApiKey();
        providerContainerMapping = new ProviderContainerMapping();
        providerContainerMapping.add(new ContainerRange(10000L, 20000L));
        providerContainerMapping.setProviderId(providerId);

        district = new District("Patna");
        allDistricts.add(district);
        allProviderContainerMappings.add(providerContainerMapping);
        ProviderWebRequest whpProviderWeb = new ProviderRequestBuilder().withDefaults().withProviderId(providerId).build();
        providerWebService.createOrUpdate(whpProviderWeb);
        reset(httpClientService);
    }

    @Test
    public void shouldRegisterContainer() throws Exception {
        String containerId = "10000";
        RegistrationInstance inTreatmentInstance = RegistrationInstance.InTreatment;

        List<String> roles = new ArrayList<>();
        roles.add(WHPRole.PROVIDER.name());

        standaloneSetup(providerContainerRegistrationController).build()
                .perform(post("/containerRegistration/by_provider/register").param("containerId", containerId).param("instance", inTreatmentInstance.getDisplayText())
                        .sessionAttr(LoginSuccessHandler.LOGGED_IN_USER, new MotechUser(new MotechWebUser(providerId, null, null, roles))))
                .andExpect(status().isOk());

        String containerIdValue = new ContainerId(providerId, containerId).value();

        Container container = containerService.getContainer(containerIdValue);


        assertNotNull(container);
        assertThat(container.getProviderId(), is(providerId));
        assertThat(container.getContainerId(), is(containerIdValue));

        String expectedContainerRegistrationXML = String.format("<?xml version=\"1.0\"?>\n" +
                "<case xmlns=\"http://openrosa.org/javarosa\" case_id=\"%s\" date_modified=\"%s\" user_id=\"motech\"\n" +
                "      api_key=\"%s\">\n" +
                "    <create>\n" +
                "        <case_type>%s</case_type>\n" +
                "    </create>\n" +
                "    <update>\n" +
                "        <provider_id>%s</provider_id>\n" +
                "    </update>\n" +
                "</case>\n", containerIdValue, container.getCreationTime().toString(DATE_TIME_FORMAT), apiKey, inTreatmentInstance.name(), providerId);

        verify(httpClientService).post(remediUrl, expectedContainerRegistrationXML);

        ContainerRegistrationReportingRequest expectedContainerRegistrationRequest = new ContainerRegistrationReportingRequestBuilder().forContainer(container).withSubmitterRole(WHPRole.PROVIDER.name()).withSubmitterId(providerId).registeredThrough(ChannelId.WEB.name()).build();
        verify(httpClientService).post(reportingEventURLs.getContainerRegistrationLogURL(), expectedContainerRegistrationRequest);

        markForDeletion(container);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(httpClientService);
        allProviderContainerMappings.remove(providerContainerMapping);
        allDistricts.remove(district);
        allContainers.removeAll();
    }

}
