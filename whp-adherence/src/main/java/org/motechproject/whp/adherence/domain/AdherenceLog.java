package org.motechproject.whp.adherence.domain;

import lombok.Getter;
import lombok.Setter;
import org.joda.time.LocalDate;
import org.motechproject.model.DayOfWeek;

import static org.joda.time.format.DateTimeFormat.forPattern;

public class AdherenceLog {

    @Getter
    @Setter
    private DayOfWeek pillDay;

    @Getter
    @Setter
    private LocalDate pillDate;

    @Getter
    @Setter
    private PillStatus pillStatus = PillStatus.Unknown;

    public AdherenceLog() {
    }

    public AdherenceLog(DayOfWeek pillDay, LocalDate logDate) {
        this.pillDay = pillDay;
        this.pillDate = logDate.minusWeeks(1).withDayOfWeek(pillDay.getValue());
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AdherenceLog that = (AdherenceLog) o;

        if (pillDate != null ? !pillDate.equals(that.pillDate) : that.pillDate != null) return false;
        if (pillDay != that.pillDay) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = pillDay != null ? pillDay.hashCode() : 0;
        result = 31 * result + (pillDate != null ? pillDate.hashCode() : 0);
        return result;
    }
}
