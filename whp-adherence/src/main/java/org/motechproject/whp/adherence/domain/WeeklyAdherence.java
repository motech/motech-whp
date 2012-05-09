package org.motechproject.whp.adherence.domain;

import lombok.Data;
import org.joda.time.LocalDate;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.patient.domain.ProvidedTreatment;
import org.motechproject.whp.patient.domain.Treatment;

import java.util.ArrayList;
import java.util.List;

@Data
public class WeeklyAdherence {

    private List<AdherenceLog> adherenceLogs = new ArrayList<AdherenceLog>();
    private String patientId;

    public WeeklyAdherence() {
    }

    public WeeklyAdherence(TreatmentWeek week, List<DayOfWeek> pillDays) {
        for (DayOfWeek pillDay : pillDays) {
            adherenceLogs.add(new AdherenceLog(pillDay, week.dateOf(pillDay)));
        }
    }

    public WeeklyAdherence addAdherenceLog(DayOfWeek dayOfWeek, AdherenceLog log) {
        log.setPillDay(dayOfWeek);
        adherenceLogs.add(log);
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
}
