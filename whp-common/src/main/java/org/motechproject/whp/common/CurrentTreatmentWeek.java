package org.motechproject.whp.common;

import org.motechproject.util.DateUtil;

public class CurrentTreatmentWeek {

    public static TreatmentWeek currentWeekInstance() {
        return new TreatmentWeek(DateUtil.today().minusDays(6));
    }

}
