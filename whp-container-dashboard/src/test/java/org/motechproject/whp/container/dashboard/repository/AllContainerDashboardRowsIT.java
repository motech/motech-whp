package org.motechproject.whp.container.dashboard.repository;

import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.whp.container.builder.ContainerBuilder;
import org.motechproject.whp.container.dashboard.model.ContainerDashboardRow;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.refdata.domain.SputumTrackingInstance;
import org.motechproject.whp.user.builder.ProviderBuilder;
import org.motechproject.whp.user.domain.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/applicationContainerDashboardContext.xml")
@Ignore("ignored due to build failure")
public class AllContainerDashboardRowsIT {

    @Autowired
    AllContainerDashboardRows allContainerDashboardRows;

    @After
    public void tearDown() {
        allContainerDashboardRows.removeAll();
    }

    @Test
    public void shouldFetchAllContainerDashboardRows() {
        ContainerDashboardRow containerDashboardRow = new ContainerDashboardRow();
        allContainerDashboardRows.add(containerDashboardRow);
        assertEquals(containerDashboardRow.getId(), allContainerDashboardRows.get(containerDashboardRow.getId()).getId());
    }

    @Test
    public void shouldFetchContainerDashboardRowByContainerId() {
        ContainerDashboardRow containerDashboardRow = new ContainerDashboardRow();
        Container container = ContainerBuilder.newContainer().withDefaults().build();
        containerDashboardRow.setContainer(container);
        allContainerDashboardRows.add(containerDashboardRow);

        ContainerDashboardRow byContainerId = allContainerDashboardRows.findByContainerId(container.getContainerId());
        assertEquals(container, byContainerId.getContainer());
    }

    @Test
    public void shouldFetchContainerDashboardRowByProviderId() {
        ContainerDashboardRow containerDashboardRow = new ContainerDashboardRow();
        Container container = ContainerBuilder.newContainer().withDefaults().build();
        containerDashboardRow.setContainer(container);
        containerDashboardRow.setProvider(new ProviderBuilder().withDefaults().withProviderId("providerId").build());

        allContainerDashboardRows.add(containerDashboardRow);

        List<ContainerDashboardRow> byProviderId = allContainerDashboardRows.withProviderId("providerId");
        assertEquals(container, byProviderId.get(0).getContainer());
    }

    @Test
    public void shouldFetchContainerDashboardRowByPatientId() {
        ContainerDashboardRow containerDashboardRow = new ContainerDashboardRow();
        Container container = ContainerBuilder.newContainer().withDefaults().build();
        containerDashboardRow.setContainer(container);
        containerDashboardRow.setPatient(new PatientBuilder().withDefaults().withPatientId("patientId").build());

        allContainerDashboardRows.add(containerDashboardRow);

        List<ContainerDashboardRow> byPatientId = allContainerDashboardRows.withPatientId("patientId");
        assertEquals(container, byPatientId.get(0).getContainer());
    }

    @Test
    public void shouldUpdateAllContainerDashboardRows() {
        ContainerDashboardRow containerDashboardRow1 = new ContainerDashboardRow();
        Container container1 = ContainerBuilder.newContainer().withDefaults().build();
        containerDashboardRow1.setContainer(container1);
        allContainerDashboardRows.add(containerDashboardRow1);

        ContainerDashboardRow containerDashboardRow2 = new ContainerDashboardRow();
        Container container2 = ContainerBuilder.newContainer().withDefaults().build();
        containerDashboardRow2.setContainer(container2);
        allContainerDashboardRows.add(containerDashboardRow2);

        containerDashboardRow1.setProvider(new Provider());
        containerDashboardRow2.setProvider(new Provider());

        allContainerDashboardRows.updateAll(asList(containerDashboardRow1, containerDashboardRow2));

        assertNotNull(allContainerDashboardRows.get(containerDashboardRow1.getId()).getProvider());
        assertNotNull(allContainerDashboardRows.get(containerDashboardRow2.getId()).getProvider());
    }

    @Test
    public void shouldReturnEmptyListWhenNoDashboardRowsPresent() {
        assertTrue(allContainerDashboardRows.getAllPretreatmentContainerDashboardRows(1, 1).isEmpty());
    }

    @Test
    public void shouldReturnOnlyPretreatmentContainerDashboardRows() {
        ContainerDashboardRow containerDashboardRow = new ContainerDashboardRow();
        Container container = ContainerBuilder.newContainer().withDefaults().build();
        container.setCurrentTrackingInstance(SputumTrackingInstance.PreTreatment);
        containerDashboardRow.setContainer(container);

        allContainerDashboardRows.add(containerDashboardRow);
        assertTrue(allContainerDashboardRows.getAllPretreatmentContainerDashboardRows(0, 1).isEmpty());
    }

    @Test
    public void shouldReturnPretreatmentContainerDashboardRowsInAPagedFashion() {
        ContainerDashboardRow containerDashboardRow1 = new ContainerDashboardRow();
        Container container1 = ContainerBuilder.newContainer().withDefaults().build();
        container1.setCurrentTrackingInstance(SputumTrackingInstance.PreTreatment);
        containerDashboardRow1.setContainer(container1);
        allContainerDashboardRows.add(containerDashboardRow1);

        ContainerDashboardRow containerDashboardRow2 = new ContainerDashboardRow();
        Container container2 = ContainerBuilder.newContainer().withDefaults().build();
        container2.setCurrentTrackingInstance(SputumTrackingInstance.PreTreatment);
        containerDashboardRow2.setContainer(container2);
        allContainerDashboardRows.add(containerDashboardRow2);

        assertEquals(containerDashboardRow1, allContainerDashboardRows.getAllPretreatmentContainerDashboardRows(0, 1).get(0));
        assertEquals(containerDashboardRow2, allContainerDashboardRows.getAllPretreatmentContainerDashboardRows(1, 1).get(0));
    }
}
