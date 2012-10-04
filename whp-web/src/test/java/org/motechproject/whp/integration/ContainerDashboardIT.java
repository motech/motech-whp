package org.motechproject.whp.integration;


import freemarker.template.TemplateException;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.whp.container.builder.ContainerBuilder;
import org.motechproject.whp.container.dashboard.model.ContainerDashboardRow;
import org.motechproject.whp.container.dashboard.repository.AllContainerDashboardRows;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.container.mapping.service.ProviderContainerMappingService;
import org.motechproject.whp.container.repository.AllContainers;
import org.motechproject.whp.container.service.ContainerService;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.user.builder.ProviderBuilder;
import org.motechproject.whp.user.domain.Provider;
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
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
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

    @Test
    public void shouldCreateDashboardPageWhenContainerIsCreated() throws IOException, TemplateException {
        final Container container = new Container();
        container.setContainerId("containerId");
        allContainers.add(container);
        new TimedRunner() {
            @Override
            protected void run() {
                assertNotNull(allContainerDashboardRows.findByContainerId(container.getContainerId()));
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
                assertEquals("patientid", allContainerDashboardRows.findByContainerId("containerId").getPatient().getPatientId());
            }
        }.executeWithTimeout();

    }

    @Test
    public void shouldUpdateDashboardPageWhenProviderIsUpdated() {
        Container container = new ContainerBuilder().withDefaults().withProviderId("providerId").withContainerId("containerId").build();
        Provider provider = new ProviderBuilder().withDefaults().withProviderId("providerId").build();
        ContainerDashboardRow row = new ContainerDashboardRow();
        row.setContainer(container);
        row.setProvider(provider);

        allProviders.add(provider);
        allContainerDashboardRows.add(row);

        provider.setDistrict("district");
        allProviders.update(provider);

        new TimedRunner() {
            @Override
            protected void run() {
                assertEquals("district", allContainerDashboardRows.findByContainerId("containerId").getProvider().getDistrict());
            }
        }.executeWithTimeout();
    }

    @Test
    public void shouldUpdateDashboardPageWhenPatientIsUpdated() {
        Container container = new ContainerBuilder().withDefaults().withPatientId("patientId").withContainerId("containerId").build();
        Patient patient = new PatientBuilder().withDefaults().withPatientId("patientId").withFirstName("name").build();

        ContainerDashboardRow row = new ContainerDashboardRow();
        row.setContainer(container);
        row.setPatient(patient);

        allPatients.add(patient);
        allContainerDashboardRows.add(row);

        patient.setFirstName("differentName");
        allPatients.update(patient);

        new TimedRunner() {
            @Override
            protected void run() {
                assertEquals("differentName", allContainerDashboardRows.findByContainerId("containerId").getPatient().getFirstName());
            }
        }.executeWithTimeout();
    }

    @After
    public void tearDown() {
        allPatients.removeAll();
        allProviders.removeAll();
        allContainers.removeAll();
        allContainerDashboardRows.removeAll();
    }
}
