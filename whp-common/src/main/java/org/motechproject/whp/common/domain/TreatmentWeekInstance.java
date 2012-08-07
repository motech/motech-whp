package org.motechproject.whp.common.domain;

import org.joda.time.LocalDate;
import org.motechproject.util.DateUtil;

public class TreatmentWeekInstance {

    public static TreatmentWeek currentAdherenceCaptureWeek() {
        return week(DateUtil.today());
    }

    public static TreatmentWeek week(LocalDate dateInWeek){
        return new TreatmentWeek(dateInWeek.minusDays(6));
    }

}
