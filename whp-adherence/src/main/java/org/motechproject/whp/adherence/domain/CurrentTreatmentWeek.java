package org.motechproject.whp.adherence.domain;

import lombok.Getter;
import org.joda.time.LocalDate;
import org.motechproject.model.DayOfWeek;
import org.motechproject.util.DateUtil;

public class CurrentTreatmentWeek {

    public static TreatmentWeek currentWeekInstance() {
        return new TreatmentWeek(DateUtil.today().minusDays(6));
    }
}
