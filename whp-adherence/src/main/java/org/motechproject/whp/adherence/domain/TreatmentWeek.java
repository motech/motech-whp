package org.motechproject.whp.adherence.domain;

import lombok.Getter;
import org.joda.time.LocalDate;
import org.motechproject.model.DayOfWeek;

public class TreatmentWeek {

    @Getter
    private LocalDate reference; //can be any "date" as such. used to define what week you are in currently.

    public TreatmentWeek(LocalDate dayInWeek) {
        reference = dayInWeek;
    }

    public LocalDate startDate() {
        return reference.withDayOfWeek(DayOfWeek.Monday.getValue());
    }

    public LocalDate endDate() {
        return reference.withDayOfWeek(DayOfWeek.Sunday.getValue());
    }

    public LocalDate dateOf(DayOfWeek dayOfWeek){
        return reference.withDayOfWeek(dayOfWeek.getValue());
    }
}
