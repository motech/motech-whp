package org.motechproject.whp.uimodel;

import lombok.Data;
import org.joda.time.LocalDate;
import org.motechproject.model.DayOfWeek;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.domain.PillStatus;

import static org.joda.time.format.DateTimeFormat.forPattern;

@Data
public class DailyAdherenceForm {

    private DayOfWeek pillDay;

    private LocalDate pillDate;

    private PillStatus pillStatus = PillStatus.Unknown;

    public DailyAdherenceForm() {
    }

    public DailyAdherenceForm(DayOfWeek pillDay) {
        this.pillDay = pillDay;
        this.pillDate = DateUtil.today().minusWeeks(1).withDayOfWeek(pillDay.getValue());
    }

    public String getPillDateString() {
        return pillDate.toString("dd-MM-YYYY");
    }

    public void setPillDateString(String pillDate) {
        this.pillDate = LocalDate.parse(pillDate, forPattern("dd-MM-YYYY"));
    }

    public boolean getIsTaken() {
        return pillStatus == PillStatus.Taken;
    }

    public boolean getIsNotTaken() {
        return pillStatus == PillStatus.NotTaken;
    }

    public void setIsTaken(boolean taken) {
        if (taken) {
            pillStatus = PillStatus.Taken;
        }
    }

    public void setIsNotTaken(boolean notTaken) {
        if (notTaken) {
            pillStatus = PillStatus.NotTaken;
        }
    }

}
