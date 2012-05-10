package org.motechproject.whp.adherence.builder;

import org.joda.time.LocalDate;
import org.motechproject.model.DayOfWeek;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.domain.AdherenceLog;
import org.motechproject.whp.adherence.domain.PillStatus;
import org.motechproject.whp.adherence.domain.TreatmentWeek;
import org.motechproject.whp.adherence.domain.WeeklyAdherence;

import static org.motechproject.model.DayOfWeek.*;

public class WeeklyAdherenceBuilder {

    private LocalDate today = DateUtil.today();
    private WeeklyAdherence adherence = new WeeklyAdherence();

    public WeeklyAdherenceBuilder withDefaultLogs() {
        adherence.addAdherenceLog(Monday, PillStatus.Taken);
        adherence.addAdherenceLog(Wednesday, PillStatus.Taken);
        adherence.addAdherenceLog(Friday, PillStatus.Taken);
        return this;
    }

    public WeeklyAdherenceBuilder withLog(DayOfWeek dayOfWeek, PillStatus pillStatus) {
        adherence.addAdherenceLog(dayOfWeek, pillStatus);
        return this;
    }

    public WeeklyAdherenceBuilder zeroDosesTaken() {
        adherence.addAdherenceLog(Monday, PillStatus.NotTaken);
        adherence.addAdherenceLog(Wednesday, PillStatus.NotTaken);
        adherence.addAdherenceLog(Friday, PillStatus.NotTaken);
        return this;
    }

    public WeeklyAdherence build() {
        return adherence;
    }

}
