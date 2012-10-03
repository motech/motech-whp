package org.motechproject.whp.container.dashboard.repository;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.whp.container.builder.ContainerBuilder;
import org.motechproject.whp.container.dashboard.model.ContainerDashboardRow;
import org.motechproject.whp.container.domain.Container;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static junit.framework.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/applicationContainerDashboardContext.xml")
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
}
