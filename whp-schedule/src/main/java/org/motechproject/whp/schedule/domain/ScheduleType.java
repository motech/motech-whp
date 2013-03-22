package org.motechproject.whp.schedule.domain;

import static org.motechproject.whp.common.event.EventKeys.*;

public enum ScheduleType {

    PROVIDER_ADHERENCE_WINDOW_COMMENCED(ADHERENCE_WINDOW_COMMENCED_EVENT_NAME),
    PROVIDER_ADHERENCE_NOT_REPORTED(ADHERENCE_NOT_REPORTED_EVENT_NAME),
    PATIENT_IVR_ALERT(PATIENT_IVR_ALERT_EVENT_NAME);

    private String eventSubject;

    ScheduleType(String eventSubject) {
        this.eventSubject = eventSubject;
    }

    public String getEventSubject() {
        return eventSubject;
    }
}
