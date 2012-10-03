package org.motechproject.whp.container.dashboard.builder;


import org.motechproject.event.MotechEvent;
import org.motechproject.whp.container.WHPContainerConstants;
import org.motechproject.whp.container.domain.Container;

public class DashboardEventsBuilder {

    public MotechEvent containerAddedEvent() {
        MotechEvent event = new MotechEvent(WHPContainerConstants.CONTAINER_ADDED_SUBJECT);
        event.getParameters().put(WHPContainerConstants.CONTAINER_ADDED_CONTAINER, new Container());
        return event;
    }
}
