package org.motechproject.whp.adherence.request;

import lombok.Data;
import org.joda.time.LocalDate;
import org.motechproject.util.DateUtil;

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
        return DateUtil.newDate(year, month, day);
    }

}
