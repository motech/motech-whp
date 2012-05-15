package org.motechproject.whp.uimodel;

import lombok.Data;
import org.joda.time.LocalDate;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.domain.PillStatus;
import org.motechproject.whp.adherence.domain.TreatmentWeek;
import org.motechproject.whp.adherence.domain.WeeklyAdherence;

import java.util.ArrayList;
import java.util.List;

import static org.joda.time.format.DateTimeFormat.forPattern;

@Data
public class AdherenceForm {

    private DayOfWeek pillDay;

    private LocalDate pillDate;

    private boolean isTreatmentInterrupted;

    private PillStatus pillStatus =  PillStatus.Unknown;

    public AdherenceForm() {
    }

    public AdherenceForm(DayOfWeek pillDay, LocalDate pillDate, PillStatus pillStatus, boolean isTreatmentInterrupted) {
        this.pillDay = pillDay;
        this.pillDate = pillDate;
        this.pillStatus = pillStatus;
        this.isTreatmentInterrupted = isTreatmentInterrupted;
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
