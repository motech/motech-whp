package org.motechproject.whp.adherence.domain;

import lombok.Getter;
import lombok.Setter;
import org.joda.time.LocalDate;
import org.motechproject.model.DayOfWeek;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class WeeklyAdherence {

    private Map<DayOfWeek, AdherenceLog> adherenceLogs = new Hashtable<DayOfWeek, AdherenceLog>();

    @Getter
    @Setter
    private String patientId;

    private TreatmentWeek week;

    public WeeklyAdherence() {
    }

    public WeeklyAdherence(TreatmentWeek week, List<DayOfWeek> pillDays) {
        this.week = week;
        for (DayOfWeek pillDay : pillDays) {
            addAdherenceLog(pillDay, new AdherenceLog(week.dateOf(pillDay)));
        }
    }

    public WeeklyAdherence addAdherenceLog(DayOfWeek pillDay, AdherenceLog log) {
        log.setPillDay(pillDay);
        adherenceLogs.put(pillDay, log);
        return this;
    }

    public boolean isAnyDoseTaken() {
        for (AdherenceLog adherenceLog : adherenceLogs.values()) {
            if (adherenceLog.getIsTaken())
                return true;
        }
        return false;
    }

    public LocalDate firstDoseTakenOn() {
        for (AdherenceLog adherenceLog : adherenceLogs.values()) {
            if (adherenceLog.getIsTaken())
                return adherenceLog.getPillDate();
        }
        return null;
    }

    public List<AdherenceLog> getAdherenceLogs() {
        return new ArrayList<AdherenceLog>(adherenceLogs.values());
    }

    public void setAdherenceLogs(List<AdherenceLog> adherenceLogs) {
        for (AdherenceLog adherenceLog : adherenceLogs) {
            this.adherenceLogs.put(adherenceLog.getPillDay(), adherenceLog);
        }
    }
}
