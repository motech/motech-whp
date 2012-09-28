package org.motechproject.whp.container.dashboard.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.whp.container.dashboard.repository.AllContainerDashboardRows;
import org.motechproject.whp.container.dashboard.model.ContainerDashboardRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static junit.framework.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/applicationContainerDashboardContext.xml")
public class AllContainerDashboardRowsIT {

    @Autowired
    AllContainerDashboardRows allContainerDashboardRows;

    @Test
    public void shouldFetchAllContainerDashboardRows() {
        ContainerDashboardRow containerDashboardRow = new ContainerDashboardRow();
        allContainerDashboardRows.add(containerDashboardRow);
        assertEquals(containerDashboardRow.getId(),allContainerDashboardRows.get(containerDashboardRow.getId()).getId());
    }

}
