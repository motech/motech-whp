package org.motechproject.whp.adherence.domain;

import org.joda.time.LocalDate;
import org.motechproject.model.DayOfWeek;

public class TreatmentWeek {

    private LocalDate reference;

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

    public TreatmentWeek minusWeeks(int weeks) {
        return new TreatmentWeek(reference.minusWeeks(weeks));
    }

}
