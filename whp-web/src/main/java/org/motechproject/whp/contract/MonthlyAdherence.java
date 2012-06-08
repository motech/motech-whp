package org.motechproject.whp.contract;

import lombok.Data;
import org.joda.time.LocalDate;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.adherence.domain.TreatmentWeek;

import java.util.ArrayList;
import java.util.List;

@Data
public class MonthlyAdherence {
    private List<DailyAdherence> logs = new ArrayList<DailyAdherence>();
    private int maxDays;
    private String monthAndYear;
    private LocalDate monthStartDate;
    private int firstSunday;

    public MonthlyAdherence(int maxDays, String monthAndYear, LocalDate monthStartDate) {
        this.maxDays = maxDays;
        this.monthAndYear = monthAndYear;
        this.monthStartDate = monthStartDate;
        firstSunday = new TreatmentWeek(monthStartDate).dateOf(DayOfWeek.Sunday).getDayOfMonth();
    }
}
