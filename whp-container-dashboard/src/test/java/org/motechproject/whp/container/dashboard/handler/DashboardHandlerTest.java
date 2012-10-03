package org.motechproject.whp.container.dashboard.handler;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.event.MotechEvent;
import org.motechproject.whp.container.dashboard.builder.DashboardEventsBuilder;
import org.motechproject.whp.container.dashboard.service.ContainerDashboardService;
import org.motechproject.whp.container.domain.Container;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.container.WHPContainerConstants.CONTAINER_ADDED_CONTAINER;

public class DashboardHandlerTest {

    @Mock
    private ContainerDashboardService containerDashboardService;

    private DashboardHandler dashboardHandler;

    @Before
    public void setup() {
        initMocks(this);
        dashboardHandler = new DashboardHandler(containerDashboardService);
    }

    @Test
    public void shouldCreateNewDashboardPageWhenContainerAdded() {
        MotechEvent event = new DashboardEventsBuilder().containerAddedEvent();

        dashboardHandler.onContainerAdded(event);

        Container container = (Container) event.getParameters().get(CONTAINER_ADDED_CONTAINER);
        verify(containerDashboardService).createDashboardRow(container);
    }
}
