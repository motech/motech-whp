package org.motechproject.whp.adherence.domain;

import lombok.Getter;
import lombok.Setter;
import org.joda.time.LocalDate;
import org.motechproject.model.DayOfWeek;
import org.motechproject.util.DateUtil;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class WeeklyAdherence {

    private Set<AdherenceLog> adherenceLogs = new LinkedHashSet<AdherenceLog>();

    @Getter
    private TreatmentWeek week;

    @Getter
    @Setter
    private String patientId;


    public WeeklyAdherence() {
        this.week = new TreatmentWeek(DateUtil.today()).minusWeeks(1);
    }

    public WeeklyAdherence(TreatmentWeek week) {
        this.week = week;
    }

    public WeeklyAdherence(TreatmentWeek week, List<DayOfWeek> pillDays) {
        this.week = week;
        for (DayOfWeek pillDay : pillDays) {
            addAdherenceLog(pillDay, PillStatus.Unknown);
        }
    }

    public WeeklyAdherence addAdherenceLog(DayOfWeek pillDay, PillStatus pillStatus) {
        AdherenceLog adherenceLog = new AdherenceLog(pillDay, week.dateOf(pillDay));
        adherenceLog.setPillStatus(pillStatus);
        adherenceLogs.add(adherenceLog);
        return this;
    }

    public boolean isAnyDoseTaken() {
        for (AdherenceLog adherenceLog : adherenceLogs) {
            if (adherenceLog.getIsTaken())
                return true;
        }
        return false;
    }

    public LocalDate firstDoseTakenOn() {
        for (AdherenceLog adherenceLog : adherenceLogs) {
            if (adherenceLog.getIsTaken())
                return adherenceLog.getPillDate();
        }
        return null;
    }

    public List<AdherenceLog> getAdherenceLogs() {
        return new ArrayList<AdherenceLog>(adherenceLogs);
    }

}
