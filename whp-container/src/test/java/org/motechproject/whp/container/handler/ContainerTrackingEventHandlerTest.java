package org.motechproject.whp.container.handler;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.event.MotechEvent;
import org.motechproject.whp.container.builder.ContainerTrackingEventsBuilder;
import org.motechproject.whp.container.service.ContainerTrackingService;
import org.motechproject.whp.user.domain.Provider;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class ContainerTrackingEventHandlerTest {

    @Mock
    private ContainerTrackingService containerTrackingService;

    private ContainerTrackingEventHandler containerTrackingEventHandler;

    @Before
    public void setup() {
        initMocks(this);
        containerTrackingEventHandler = new ContainerTrackingEventHandler(containerTrackingService);
    }

    @Test
    public void shouldUpdateExistingDashboardPageWhenProviderGotUpdated() {
        MotechEvent event = new ContainerTrackingEventsBuilder().providerUpdatedEvent();

        containerTrackingEventHandler.onProviderUpdated(event);

        Provider provider = (Provider) event.getParameters().get("0");
        verify(containerTrackingService).updateProviderInformation(provider);
    }
}
