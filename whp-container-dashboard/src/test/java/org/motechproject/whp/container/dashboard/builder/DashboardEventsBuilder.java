package org.motechproject.whp.container.dashboard.builder;


import org.motechproject.event.MotechEvent;
import org.motechproject.whp.container.WHPContainerConstants;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.user.WHPUserConstants;
import org.motechproject.whp.user.domain.Provider;

public class DashboardEventsBuilder {

    public MotechEvent containerAddedEvent() {
        MotechEvent event = new MotechEvent(WHPContainerConstants.CONTAINER_ADDED_SUBJECT);
        event.getParameters().put(WHPContainerConstants.CONTAINER_KEY, new Container());
        return event;
    }

    public MotechEvent providerUpdatedEvent() {
        MotechEvent event = new MotechEvent(WHPUserConstants.PROVIDER_ADDED_SUBJECT);
        event.getParameters().put(WHPUserConstants.PROVIDER_KEY, new Provider());
        return event;
    }
}
