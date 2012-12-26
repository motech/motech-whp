package org.motechproject.whp.providerreminder.domain;

import org.joda.time.DateTime;

import java.util.Date;

public class ProviderReminderTimings {

    private DateTime now;

    public ProviderReminderTimings(DateTime now) {
        this.now = now;
    }

    public Date getStartDate() {
        return now.toDate();
    }

    public Date getEndDate() {
        return now.toLocalDate().plusWeeks(1).toDate();
    }
}
