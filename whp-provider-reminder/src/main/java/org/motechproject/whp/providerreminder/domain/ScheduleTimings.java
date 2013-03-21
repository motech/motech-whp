package org.motechproject.whp.providerreminder.domain;

import org.joda.time.DateTime;

import java.util.Date;

public class ScheduleTimings {

    private DateTime now;

    public ScheduleTimings(DateTime now) {
        this.now = now;
    }

    public Date getStartDate() {
        return now.toDate();
    }

    public Date getEndDate() {
        return now.toLocalDate().plusWeeks(1).toDate();
    }
}
