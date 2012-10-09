package org.motechproject.whp.integration;


import freemarker.template.TemplateException;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.whp.container.builder.ContainerBuilder;
import org.motechproject.whp.container.tracking.model.ContainerTrackingRecord;
import org.motechproject.whp.container.tracking.repository.AllContainerTrackingRecords;
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
    AllContainerTrackingRecords allContainerTrackingRecords;

    @Test
    public void shouldCreateDashboardPageWhenContainerIsCreated() throws IOException, TemplateException {
        final Container container = new Container();
        container.setContainerId("containerId");
        allContainers.add(container);
        new TimedRunner() {
            @Override
            protected void run() {
                assertNotNull(allContainerTrackingRecords.findByContainerId(container.getContainerId()));
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
                assertNotNull(allContainerTrackingRecords.findByContainerId("containerId").getPatient());
                assertEquals("patientid", allContainerTrackingRecords.findByContainerId("containerId").getPatient().getPatientId());
            }
        }.executeWithTimeout();

    }

    @Test
    public void shouldUpdateDashboardPageWhenProviderIsUpdated() {
        Container container = new ContainerBuilder().withDefaults().withProviderId("providerId").withContainerId("containerId").build();
        Provider provider = new ProviderBuilder().withDefaults().withProviderId("providerId").build();
        ContainerTrackingRecord row = new ContainerTrackingRecord();
        row.setContainer(container);
        row.setProvider(provider);

        allProviders.add(provider);
        allContainerTrackingRecords.add(row);

        provider.setDistrict("district");
        allProviders.update(provider);

        new TimedRunner() {
            @Override
            protected void run() {
                assertEquals("district", allContainerTrackingRecords.findByContainerId("containerId").getProvider().getDistrict());
            }
        }.executeWithTimeout();
    }

    @Test
    public void shouldUpdateDashboardPageWhenPatientIsUpdated() {
        Container container = new ContainerBuilder().withDefaults().withPatientId("patientId").withContainerId("containerId").build();
        Patient patient = new PatientBuilder().withDefaults().withPatientId("patientId").withFirstName("name").build();

        ContainerTrackingRecord row = new ContainerTrackingRecord();
        row.setContainer(container);
        row.setPatient(patient);

        allPatients.add(patient);
        allContainerTrackingRecords.add(row);

        patient.setFirstName("differentName");
        allPatients.update(patient);

        new TimedRunner() {
            @Override
            protected void run() {
                assertEquals("differentName", allContainerTrackingRecords.findByContainerId("containerId").getPatient().getFirstName());
            }
        }.executeWithTimeout();
    }

    @After
    public void tearDown() {
        allPatients.removeAll();
        allProviders.removeAll();
        allContainers.removeAll();
        allContainerTrackingRecords.removeAll();
    }
}
