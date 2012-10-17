package org.motechproject.whp.it;


import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.container.repository.AllContainerTrackingRecords;
import org.motechproject.whp.container.service.ContainerService;
import org.motechproject.whp.containermapping.service.ProviderContainerMappingService;
import org.motechproject.whp.user.builder.ProviderBuilder;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.repository.AllProviders;
import org.motechproject.whp.user.service.ProviderService;
import org.motechproject.whp.webservice.service.ContainerPatientMappingWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static junit.framework.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
public class ContainerDashboardIT {

    @Autowired
    ContainerService containerService;

    @Autowired
    ProviderService providerService;

    @Autowired
    ContainerPatientMappingWebService containerPatientMappingService;

    @Autowired
    ProviderContainerMappingService providerContainerMappingService;

    @Autowired
    AllProviders allProviders;

    @Autowired
    AllContainerTrackingRecords allContainerTrackingRecords;

    @Test
    public void shouldUpdateDashboardPageWhenProviderIsUpdated() {
        Provider provider = new ProviderBuilder().withDefaults().withProviderId("providerId").build();
        Container row = new Container();
        row.setProviderId(provider.getProviderId());
        row.setContainerId("containerId");

        allProviders.add(provider);
        allContainerTrackingRecords.add(row);

        provider.setDistrict("district");
        allProviders.update(provider);

        new TimedRunner() {
            @Override
            protected void run() {
                assertEquals("district", allContainerTrackingRecords.findByContainerId("containerId").getDistrict());
            }
        }.executeWithTimeout();
    }

    @After
    public void tearDown() {
        allProviders.removeAll();
        allContainerTrackingRecords.removeAll();
    }
}
