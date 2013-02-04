package org.motechproject.whp.common.domain;

import org.joda.time.LocalDate;
import org.motechproject.whp.common.service.AdherenceWindow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.motechproject.util.DateUtil.today;

@Component
public class TreatmentWeekInstance {

    private AdherenceWindow adherenceWindow;

    @Autowired
    public TreatmentWeekInstance(AdherenceWindow adherenceWindow) {
        this.adherenceWindow = adherenceWindow;
    }

    public static TreatmentWeek currentAdherenceCaptureWeek() {
        return week(today());
    }

    private static TreatmentWeek previousAdherenceCaptureWeek() {
        return week(today().minusWeeks(1));
    }

    public static TreatmentWeek week(LocalDate dateInWeek){
        return new TreatmentWeek(dateInWeek.minusDays(6));
    }

    public LocalDate previousAdherenceWeekEndDate() {
        if(adherenceWindow.isValidAdherenceDay(today())){
            return previousAdherenceCaptureWeek().endDate();
        }
        return currentAdherenceCaptureWeek().endDate();
    }
}
