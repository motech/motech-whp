package org.motechproject.whp.domain.v1;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.joda.time.LocalDate;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TreatmentInterruptionsV1 extends ArrayList<TreatmentInterruptionV1> {

    public TreatmentInterruptionsV1() {
    }

    public TreatmentInterruptionsV1(Collection<? extends TreatmentInterruptionV1> treatmentInterruptions) {
        super(treatmentInterruptions);
    }

    @JsonIgnore
    public List<String> getPauseReasons(List<LocalDate> treatmentWeekDates) {
        List<String> pauseReasons = new ArrayList<String>();
        for (TreatmentInterruptionV1 treatmentInterruption : this) {
            if (treatmentInterruption.isTreatmentInterrupted(treatmentWeekDates))
                pauseReasons.add(treatmentInterruption.getReasonForPause());
        }
        return pauseReasons;
    }

    public TreatmentInterruptionV1 latestInterruption() {
        if (CollectionUtils.isEmpty(this)) return null;
        return get(size() - 1);
    }
}
