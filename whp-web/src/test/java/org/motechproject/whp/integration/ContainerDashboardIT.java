package org.motechproject.whp.integration;


import freemarker.template.TemplateException;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kubek2k.springockito.annotations.ReplaceWithMock;
import org.kubek2k.springockito.annotations.SpringockitoContextLoader;
import org.motechproject.http.client.service.HttpClientService;
import org.motechproject.whp.container.builder.ContainerRegistrationRequestBuilder;
import org.motechproject.whp.container.contract.ContainerRegistrationRequest;
import org.motechproject.whp.container.dashboard.repository.AllContainerDashboardRows;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.container.mapping.service.ProviderContainerMappingService;
import org.motechproject.whp.container.repository.AllContainers;
import org.motechproject.whp.container.service.ContainerService;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.user.repository.AllProviders;
import org.motechproject.whp.user.service.ProviderService;
import org.motechproject.whp.webservice.service.ContainerPatientMappingWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = SpringockitoContextLoader.class, locations = "classpath:/META-INF/spring/applicationContext.xml")
public class ContainerDashboardIT {

    @Autowired
    ContainerService containerService;

    @Autowired
    PatientService patientService;

    @Autowired
    ProviderService providerService;

    @Autowired
    ContainerPatientMappingWebService containerPatientMappingService;

    @Autowired
    ProviderContainerMappingService providerContainerMappingService;

    @Autowired
    AllPatients allPatients;

    @Autowired
    AllContainers allContainers;

    @Autowired
    AllProviders allProviders;

    @Autowired
    AllContainerDashboardRows allContainerDashboardRows;

    @Autowired
    @ReplaceWithMock
    HttpClientService httpClientService;

    @Test
    public void shouldCreateDashboardPageWhenContainerIsCreated() throws IOException, TemplateException {
        final ContainerRegistrationRequest request = ContainerRegistrationRequestBuilder
                .newRegistrationRequest()
                .withDefaults()
                .build();
        containerService.registerContainer(request);
        new TimedRunner() {
            @Override
            protected void run() {
                assertNotNull(allContainerDashboardRows.findByContainerId(request.getContainerId()));
            }
        }.executeWithTimeout();
    }

    @Test
    public void shouldUpdateDashboardPageWhenPatientIsMappedToContainer() throws IOException, TemplateException {
        Container container = new Container();
        container.setContainerId("containerId");
        allContainers.add(container);

        allPatients.add(new PatientBuilder().withDefaults().withPatientId("patientId").build());

        container.setPatientId("patientId");
        allContainers.update(container);

        new TimedRunner() {
            @Override
            protected void run() {
                assertNotNull(allContainerDashboardRows.findByContainerId("containerId").getPatient());
                assertEquals("patientId", allContainerDashboardRows.findByContainerId("containerId").getPatient().getPatientId());
            }
        }.executeWithTimeout();

    }

    @Test
    public void shouldUpdateDashboardPageWhenProviderIsUpdated() {

    }

    @Test
    public void shouldUpdateDashboardPageWhenPatientIsUpdated() {

    }

    @After
    public void tearDown() {
        allPatients.removeAll();
        allProviders.removeAll();
        allContainers.removeAll();
        allContainerDashboardRows.removeAll();
    }
}
