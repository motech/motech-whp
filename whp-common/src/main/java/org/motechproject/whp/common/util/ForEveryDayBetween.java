package org.motechproject.whp.common.util;

import org.joda.time.LocalDate;

public abstract class ForEveryDayBetween {

    public ForEveryDayBetween(LocalDate startDate, LocalDate endDate) {
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            _do_(date);
        }
    }

    public abstract void _do_(LocalDate date);
}
