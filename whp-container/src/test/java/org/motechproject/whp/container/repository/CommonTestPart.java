package org.motechproject.whp.container.repository;

import junit.framework.Assert;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.paginator.contract.FilterParams;
import org.motechproject.paginator.contract.SortParams;
import org.motechproject.whp.common.domain.RegistrationInstance;
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

    @Test
    public void shouldSortContainerDashboardRowsByContainerIdAsDefaultOne() {
        allContainerTrackingRecords.add(createContainerTrackingRecord("10000000001", LocalDate.now(), RegistrationInstance.PreTreatment));
        allContainerTrackingRecords.add(createContainerTrackingRecord("10000000003", LocalDate.now(), RegistrationInstance.PreTreatment));
        allContainerTrackingRecords.add(createContainerTrackingRecord("10000000002", LocalDate.now(), RegistrationInstance.PreTreatment));
        allContainerTrackingRecords.add(createContainerTrackingRecord("10000000004", LocalDate.now().plusDays(2), RegistrationInstance.PreTreatment));

        SortParams sortParams = new SortParams();
        sortParams.put("containerIssuedDate", "desc");
        List<Container> all = allContainerTrackingRecords.filter(RegistrationInstance.PreTreatment, new FilterParams(), sortParams, 0, 10);

        assertEquals("10000000004", all.get(0).getContainerId());
        assertEquals("10000000001", all.get(1).getContainerId());
        assertEquals("10000000002", all.get(2).getContainerId());
        assertEquals("10000000003", all.get(3).getContainerId());
    }

    private Container createContainerTrackingRecord(String containerId, LocalDate containerIssuedDate, RegistrationInstance instance) {
        Container containerTrackingRecord = new Container();
        containerTrackingRecord.setContainerId(containerId);
        containerTrackingRecord.setContainerIssuedDate(containerIssuedDate);
        containerTrackingRecord.setCurrentTrackingInstance(instance);
        return containerTrackingRecord;
    }
}
