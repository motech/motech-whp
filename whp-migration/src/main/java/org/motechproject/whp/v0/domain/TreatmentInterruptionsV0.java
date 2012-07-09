package org.motechproject.whp.v0.domain;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.joda.time.LocalDate;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TreatmentInterruptionsV0 extends ArrayList<TreatmentInterruptionV0> {

    public TreatmentInterruptionsV0() {
    }

    public TreatmentInterruptionsV0(Collection<? extends TreatmentInterruptionV0> treatmentInterruptions) {
        super(treatmentInterruptions);
    }

    @JsonIgnore
    public List<String> getPauseReasons(List<LocalDate> treatmentWeekDates) {
        List<String> pauseReasons = new ArrayList<String>();
        for (TreatmentInterruptionV0 treatmentInterruption : this) {
            if (treatmentInterruption.isTreatmentInterrupted(treatmentWeekDates))
                pauseReasons.add(treatmentInterruption.getReasonForPause());
        }
        return pauseReasons;
    }

    public TreatmentInterruptionV0 latestInterruption() {
        if (CollectionUtils.isEmpty(this)) return null;
        return get(size() - 1);
    }
}