package org.motechproject.whp.container.dashboard.handler;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.annotations.MotechListener;
import org.motechproject.whp.container.WHPContainerConstants;
import org.motechproject.whp.container.dashboard.service.ContainerDashboardService;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.user.WHPUserConstants;
import org.motechproject.whp.user.domain.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ContainerDashboardHandler {

    private ContainerDashboardService containerDashboardService;

    @Autowired
    public ContainerDashboardHandler(ContainerDashboardService containerDashboardService) {
        this.containerDashboardService = containerDashboardService;
    }

    @MotechListener(subjects = WHPContainerConstants.CONTAINER_ADDED_SUBJECT)
    public void onContainerAdded(MotechEvent event) {
        Container container = (Container) event.getParameters().get(WHPContainerConstants.CONTAINER_KEY);
        containerDashboardService.createDashboardRow(container);
    }

    @MotechListener(subjects = WHPContainerConstants.CONTAINER_UPDATED_SUBJECT)
    public void onContainerUpdated(MotechEvent event) {
        Container container = (Container) event.getParameters().get(WHPContainerConstants.CONTAINER_KEY);
        containerDashboardService.updateDashboardRow(container);
    }

    @MotechListener(subjects = WHPUserConstants.PROVIDER_ADDED_SUBJECT)
    public void onProviderUpdated(MotechEvent event) {
        Provider provider = (Provider) event.getParameters().get(WHPUserConstants.PROVIDER_KEY);
        containerDashboardService.updateProviderInformation(provider);
    }
}
