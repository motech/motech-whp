package org.motechproject.whp.container.handler;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.whp.container.service.ContainerTrackingService;
import org.motechproject.whp.user.WHPUserConstants;
import org.motechproject.whp.user.domain.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ContainerTrackingEventHandler {

    private ContainerTrackingService containerTrackingService;

    @Autowired
    public ContainerTrackingEventHandler(ContainerTrackingService containerTrackingService) {
        this.containerTrackingService = containerTrackingService;
    }

    @MotechListener(subjects = WHPUserConstants.PROVIDER_UPDATED_SUBJECT)
    public void onProviderUpdated(MotechEvent event) {
        Provider provider = (Provider) event.getParameters().get("0");
        containerTrackingService.updateProviderInformation(provider);
    }
}
