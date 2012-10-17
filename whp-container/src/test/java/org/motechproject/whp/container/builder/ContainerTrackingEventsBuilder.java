package org.motechproject.whp.container.builder;


import org.motechproject.event.MotechEvent;
import org.motechproject.whp.user.WHPUserConstants;
import org.motechproject.whp.user.domain.Provider;

public class ContainerTrackingEventsBuilder {

    public MotechEvent providerUpdatedEvent() {
        MotechEvent event = new MotechEvent(WHPUserConstants.PROVIDER_UPDATED_SUBJECT);
        event.getParameters().put("0", new Provider());
        return event;
    }
}
