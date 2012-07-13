package org.motechproject.whp.treatmentcard.domain;

import lombok.Data;
import org.joda.time.LocalDate;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.common.domain.TreatmentWeek;

import java.util.ArrayList;
import java.util.List;

@Data
public class MonthlyAdherence {

    private List<DailyAdherence> logs = new ArrayList<DailyAdherence>();
    private int maxDays;
    private String monthAndYear;
    private LocalDate monthStartDate;
    private int firstSunday;
    private String month;
    private String year;
    public static final String MonthAndYearFormat = "MMM YYYY";

    public MonthlyAdherence(int maxDays, String monthAndYear, LocalDate monthStartDate) {
        this.maxDays = maxDays;
        this.monthAndYear = monthAndYear;
        this.monthStartDate = monthStartDate;
        firstSunday = new TreatmentWeek(monthStartDate).dateOf(DayOfWeek.Sunday).getDayOfMonth();
        month = Integer.toString(monthStartDate.getMonthOfYear());
        year = Integer.toString(monthStartDate.getYear());
    }

}
