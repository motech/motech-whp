package org.motechproject.whp.schedule.domain;

import static org.motechproject.whp.common.event.EventKeys.ADHERENCE_NOT_REPORTED_EVENT_NAME;
import static org.motechproject.whp.common.event.EventKeys.ADHERENCE_WINDOW_COMMENCED_EVENT_NAME;

public enum ScheduleType {

    ADHERENCE_WINDOW_COMMENCED(ADHERENCE_WINDOW_COMMENCED_EVENT_NAME),
    ADHERENCE_NOT_REPORTED(ADHERENCE_NOT_REPORTED_EVENT_NAME);

    private String eventSubject;

    ScheduleType(String eventSubject) {
        this.eventSubject = eventSubject;
    }

    public String getEventSubject() {
        return eventSubject;
    }
}
