package org.motechproject.whp.uimodel;

import lombok.Data;
import org.joda.time.LocalDate;

@Data
public class DailyAdherenceRequest {

    private int day;
    private int month;
    private int year;
    private int pillStatus;

    public DailyAdherenceRequest() {
    }

    public DailyAdherenceRequest(int day, int month, int year, int pillStatus) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.pillStatus = pillStatus;
    }

    public LocalDate getDoseDate() {
        return new LocalDate(year, month, day);
    }

}
