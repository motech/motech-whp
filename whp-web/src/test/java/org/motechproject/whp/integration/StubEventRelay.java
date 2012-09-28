package org.motechproject.whp.integration;


import org.motechproject.event.MotechEvent;
import org.motechproject.event.ServerEventRelay;

public class StubEventRelay extends ServerEventRelay {

    private ServerEventRelay originalEventRelay;

    public StubEventRelay(ServerEventRelay originalEventRelay) {
        this.originalEventRelay = originalEventRelay;
    }

    @Override
    public void sendEventMessage(MotechEvent motechEvent) {
        originalEventRelay.relayEvent(motechEvent);
    }
}
