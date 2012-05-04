package org.motechproject.whp.adherence.builder;

import org.joda.time.LocalDate;
import org.motechproject.model.DayOfWeek;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.domain.AdherenceLog;

import static org.motechproject.model.DayOfWeek.*;

public class AdherenceBuilder {

    private Adherence adherence = new Adherence();
    private LocalDate today = DateUtil.today();

    public AdherenceBuilder withDefaultLogs() {
        adherence.addAdherenceLog(new AdherenceLog(Monday, today));
        adherence.addAdherenceLog(new AdherenceLog(Wednesday, today));
        adherence.addAdherenceLog(new AdherenceLog(Friday, today));
        return this;
    }

    public AdherenceBuilder withLog(DayOfWeek dayOfWeek, LocalDate logDate, boolean taken) {
        AdherenceLog log = new AdherenceLog(dayOfWeek, logDate);
        log.setIsTaken(taken);
        adherence.addAdherenceLog(log);
        return this;
    }

    public Adherence build() {
        return adherence;
    }
}
