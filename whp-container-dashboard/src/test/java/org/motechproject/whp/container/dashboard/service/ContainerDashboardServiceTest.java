package org.motechproject.whp.container.dashboard.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.container.dashboard.model.ContainerDashboardRow;
import org.motechproject.whp.container.dashboard.repository.AllContainerDashboardRows;
import org.motechproject.whp.container.domain.Container;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
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
    public void shouldPersistContainerDashboardRow() {
        Container container = mock(Container.class);
        ContainerDashboardRow row = new ContainerDashboardRow();
        row.setContainer(container);

        containerDashboardService.createDashboardRow(container);

        verify(allContainerDashboardRows).add(row);
    }

    @Test
    public void shouldFetchAllContainerDashboardRows() {
        List<ContainerDashboardRow> containerDashboardRows = Collections.emptyList();

        when(allContainerDashboardRows.getAll()).thenReturn(containerDashboardRows);

        assertEquals(containerDashboardRows, containerDashboardService.allContainerDashboardRows());
    }
}
