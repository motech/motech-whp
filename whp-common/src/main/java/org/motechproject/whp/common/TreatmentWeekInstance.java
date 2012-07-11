package org.motechproject.whp.common;

import org.joda.time.LocalDate;
import org.motechproject.util.DateUtil;

public class TreatmentWeekInstance {

    public static TreatmentWeek currentWeekInstance() {
        return week(DateUtil.today());
    }

    public static TreatmentWeek week(LocalDate dateInWeek){
        return new TreatmentWeek(dateInWeek.minusDays(6));
    }

}
