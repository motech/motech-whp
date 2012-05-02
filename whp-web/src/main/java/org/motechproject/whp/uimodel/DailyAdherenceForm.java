package org.motechproject.whp.uimodel;

import lombok.Data;
import org.joda.time.LocalDate;
import org.motechproject.model.DayOfWeek;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.domain.PillStatus;

@Data
public class DailyAdherenceForm {

    private DayOfWeek pillDay;
    private LocalDate pillDate;

    private PillStatus pillStatus = PillStatus.Unknown;

    public DailyAdherenceForm(DayOfWeek pillDay) {
        this.pillDay = pillDay;
        this.pillDate = DateUtil.today().minusWeeks(1).withDayOfWeek(pillDay.getValue());
    }

    public boolean isTaken() {
        return pillStatus == PillStatus.Taken;
    }

    public boolean isNotTaken() {
        return pillStatus == PillStatus.NotTaken;
    }

    public void isTaken(boolean taken) {
        if (taken) {
            pillStatus = PillStatus.Taken;
        }
    }

    public void isNotTaken(boolean notTaken) {
        if (notTaken) {
            pillStatus = PillStatus.NotTaken;
        }
    }

}
