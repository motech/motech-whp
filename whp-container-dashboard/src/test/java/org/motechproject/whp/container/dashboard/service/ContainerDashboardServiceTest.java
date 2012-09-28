package org.motechproject.whp.container.dashboard.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.container.dashboard.service.ContainerDashboardService;
import org.motechproject.whp.container.dashboard.model.ContainerDashboardRow;
import org.motechproject.whp.container.dashboard.repository.AllContainerDashboardRows;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ContainerDashboardServiceTest {

    @Mock
    AllContainerDashboardRows allContainerDashboardRows;
    ContainerDashboardService containerDashboardService;

    @Before
    public void setUp() {
        initMocks(this);
        containerDashboardService = new ContainerDashboardService(allContainerDashboardRows);
    }

    @Test
    public void shouldFetchAllContainerDashboardRows() {
        List<ContainerDashboardRow> containerDashboardRows = Collections.emptyList();

        when(allContainerDashboardRows.getAll()).thenReturn(containerDashboardRows);

        assertEquals(containerDashboardRows, containerDashboardService.allContainerDashboardRows());
    }
}
