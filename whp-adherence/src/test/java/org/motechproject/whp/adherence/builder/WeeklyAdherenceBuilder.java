package org.motechproject.whp.adherence.builder;

import org.joda.time.LocalDate;
import org.motechproject.model.DayOfWeek;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.domain.AdherenceLog;
import org.motechproject.whp.adherence.domain.PillStatus;
import org.motechproject.whp.adherence.domain.WeeklyAdherence;

import static org.motechproject.model.DayOfWeek.*;

public class WeeklyAdherenceBuilder {

    private WeeklyAdherence adherence = new WeeklyAdherence();
    private LocalDate today = DateUtil.today();

    public WeeklyAdherenceBuilder withDefaultLogs() {
        adherence.addAdherenceLog(Monday, taken());
        adherence.addAdherenceLog(Wednesday, taken());
        adherence.addAdherenceLog(Friday, taken());
        return this;
    }

    public WeeklyAdherenceBuilder withLog(DayOfWeek dayOfWeek, LocalDate logDate, boolean taken) {
        AdherenceLog log = new AdherenceLog(logDate);
        log.setIsTaken(taken);
        adherence.addAdherenceLog(dayOfWeek, log);
        return this;
    }

    public WeeklyAdherenceBuilder zeroDosesTaken() {
        adherence.addAdherenceLog(Monday, notTaken());
        adherence.addAdherenceLog(Wednesday, notTaken());
        adherence.addAdherenceLog(Friday, notTaken());
        return this;
    }

    private AdherenceLog taken() {
        AdherenceLog log = new AdherenceLog(today);
        log.status(PillStatus.Taken);
        return log;
    }

    private AdherenceLog notTaken() {
        AdherenceLog log = new AdherenceLog(today);
        log.status(PillStatus.NotTaken);
        return log;
    }

    public WeeklyAdherence build() {
        return adherence;
    }

}
