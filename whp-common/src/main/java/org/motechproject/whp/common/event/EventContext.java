package org.motechproject.whp.common.event;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.OutboundEventGateway;
import org.motechproject.event.listener.EventRelay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class EventContext {

    EventRelay eventGateway;

    @Autowired
    public EventContext(EventRelay eventGateway) {
        this.eventGateway = eventGateway;
    }

    public void send(String subject, Object... params) {
        MotechEvent motechEvent = new MotechEvent(subject, eventParams(params));
        eventGateway.sendEventMessage(motechEvent);
    }

    private Map<String, Object> eventParams(Object[] params) {
        Map<String, Object> eventParams = new HashMap<>();
        for (Integer i = 0; i < params.length; i++) {
            eventParams.put(i.toString(), params[i]);
        }
        return eventParams;
    }
}
