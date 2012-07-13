package org.motechproject.whp.common;

import org.joda.time.LocalDate;

public abstract class ForEveryDayBetween {

    private LocalDate startDate;

    private LocalDate endDate;

    public ForEveryDayBetween(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            _do_(date);
        }
    }

    public abstract void _do_(LocalDate date);
}
