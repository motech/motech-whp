package org.motechproject.whp.adherence.domain;

import lombok.Data;
import org.joda.time.LocalDate;
import org.motechproject.model.DayOfWeek;

import java.util.ArrayList;
import java.util.List;

@Data
public class Adherence {

    private List<AdherenceLog> adherenceLogs = new ArrayList<AdherenceLog>();

    public Adherence() {
    }

    public Adherence(LocalDate logDate, List<DayOfWeek> pillDays) {
        for (DayOfWeek pillDay : pillDays) {
            adherenceLogs.add(new AdherenceLog(pillDay, logDate));
        }
    }

    public Adherence addAdherenceLog(AdherenceLog log) {
        adherenceLogs.add(log);
        return this;
    }
}
