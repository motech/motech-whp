package org.motechproject.whp.uimodel;

import lombok.Data;
import org.motechproject.model.DayOfWeek;

import java.util.ArrayList;
import java.util.List;

@Data
public class WeeklyAdherenceForm {

    List<DailyAdherenceForm> allDailyAdherenceForms = new ArrayList<DailyAdherenceForm>();

    public WeeklyAdherenceForm() {
    }

    public WeeklyAdherenceForm(List<DayOfWeek> pillDays) {
        for (DayOfWeek pillDay : pillDays) {
            allDailyAdherenceForms.add(new DailyAdherenceForm(pillDay));
        }
    }

}
