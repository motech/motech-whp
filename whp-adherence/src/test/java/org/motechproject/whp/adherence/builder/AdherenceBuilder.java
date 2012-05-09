package org.motechproject.whp.adherence.builder;

import org.joda.time.LocalDate;
import org.motechproject.model.DayOfWeek;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.domain.AdherenceLog;
import org.motechproject.whp.adherence.domain.PillStatus;

import static org.motechproject.model.DayOfWeek.*;

public class AdherenceBuilder {

    private Adherence adherence = new Adherence();
    private LocalDate today = DateUtil.today();

    public Adherence build() {
        return adherence;
    }

    public AdherenceBuilder withDefaultLogs() {
        adherence.addAdherenceLog(taken(Monday));
        adherence.addAdherenceLog(taken(Wednesday));
        adherence.addAdherenceLog(taken(Friday));
        return this;
    }

    public AdherenceBuilder withLog(DayOfWeek dayOfWeek, LocalDate logDate, boolean taken) {
        AdherenceLog log = new AdherenceLog(dayOfWeek, logDate);
        log.setIsTaken(taken);
        adherence.addAdherenceLog(log);
        return this;
    }

    public AdherenceBuilder zeroDosesTaken() {
        adherence.addAdherenceLog(notTaken(Monday));
        adherence.addAdherenceLog(notTaken(Wednesday));
        adherence.addAdherenceLog(notTaken(Friday));
        return this;
    }

    private AdherenceLog taken(DayOfWeek dayOfWeek) {
        AdherenceLog log = new AdherenceLog(dayOfWeek, today);
        log.status(PillStatus.Taken);
        return log;
    }

    private AdherenceLog notTaken(DayOfWeek dayOfWeek) {
        AdherenceLog log = new AdherenceLog(dayOfWeek, today);
        log.status(PillStatus.NotTaken);
        return log;
    }

}
