package org.motechproject.whp.container.repository;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.whp.container.builder.ContainerBuilder;
import org.motechproject.whp.container.domain.Container;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/applicationContainerContext.xml")
public class CommonTestPart {

    @Autowired
    AllContainerTrackingRecords allContainerTrackingRecords;

    @After
    public void tearDown() {
        allContainerTrackingRecords.removeAll();
    }

    @Test
    public void shouldFetchAllContainerDashboardRows() {
        Container container = new Container();
        allContainerTrackingRecords.add(container);
        assertEquals(container.getId(), allContainerTrackingRecords.get(container.getId()).getId());
    }

    @Test
    public void ShouldFetchContainerTrackingRecordByContainerId() {
        Container container = ContainerBuilder.newContainer().withDefaults().build();
        allContainerTrackingRecords.add(container);

        Container byContainerId = allContainerTrackingRecords.findByContainerId(container.getContainerId());
        assertEquals(container, byContainerId);
    }

    @Test
    public void shouldFetchContainerTrackingRecordByProviderId() {
        Container container = ContainerBuilder.newContainer().withDefaults().build();
        container.setProviderId("providerId");

        allContainerTrackingRecords.add(container);

        List<Container> byProviderId = allContainerTrackingRecords.withProviderId("providerId");
        assertEquals(container, byProviderId.get(0));
    }

    @Test
    public void shouldFetchContainerTrackingRecordByPatientId() {
        Container container = ContainerBuilder.newContainer().withDefaults().build();
        container.setPatientId("patientId");

        allContainerTrackingRecords.add(container);

        List<Container> byPatientId = allContainerTrackingRecords.withPatientId("patientId");
        assertEquals(container, byPatientId.get(0));
    }

    @Test
    public void shouldUpdateAllContainerDashboardRows() {
        Container containerTrackingRecord1 = new Container();
        allContainerTrackingRecords.add(containerTrackingRecord1);

        Container containerTrackingRecord2 = new Container();
        allContainerTrackingRecords.add(containerTrackingRecord2);

        containerTrackingRecord1.setProviderId("providerId1");
        containerTrackingRecord2.setProviderId("providerId2");

        allContainerTrackingRecords.updateAll(asList(containerTrackingRecord1, containerTrackingRecord2));

        Assert.assertEquals("providerid1", allContainerTrackingRecords.get(containerTrackingRecord1.getId()).getProviderId());
        Assert.assertEquals("providerid2", allContainerTrackingRecords.get(containerTrackingRecord2.getId()).getProviderId());
    }
}
