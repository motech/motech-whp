package org.motechproject.whp.common.domain;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TreatmentWeek that = (TreatmentWeek) o;

        return !(startDate() != null ? !startDate().equals(that.startDate()) : that.startDate() != null);
    }
}
