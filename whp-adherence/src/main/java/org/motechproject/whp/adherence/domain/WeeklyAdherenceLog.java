package org.motechproject.whp.adherence.domain;

import lombok.Data;
import org.motechproject.model.DayOfWeek;

import java.util.ArrayList;
import java.util.List;

@Data
public class WeeklyAdherenceLog {

    private List<DailyAdherenceLog> allDailyAdherenceLogs;

    public WeeklyAdherenceLog() {
        allDailyAdherenceLogs = new ArrayList<DailyAdherenceLog>();
    }

    public WeeklyAdherenceLog(List<DayOfWeek> pillDays) {
        this();
        for (DayOfWeek pillDay : pillDays) {
            allDailyAdherenceLogs.add(new DailyAdherenceLog(pillDay));
        }
    }

}
