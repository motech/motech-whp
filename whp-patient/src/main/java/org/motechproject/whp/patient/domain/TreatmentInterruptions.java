package org.motechproject.whp.patient.domain;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.joda.time.LocalDate;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TreatmentInterruptions extends ArrayList<TreatmentInterruption> implements Serializable {

    public TreatmentInterruptions() {
    }

    public TreatmentInterruptions(Collection<? extends TreatmentInterruption> treatmentInterruptions) {
        super(treatmentInterruptions);
    }

    @JsonIgnore
    public List<String> getPauseReasons(List<LocalDate> treatmentWeekDates) {
        List<String> pauseReasons = new ArrayList<String>();
        for (TreatmentInterruption treatmentInterruption : this) {
            if (treatmentInterruption.isTreatmentInterrupted(treatmentWeekDates))
                pauseReasons.add(treatmentInterruption.getReasonForPause());
        }
        return pauseReasons;
    }

    public TreatmentInterruption latestInterruption() {
        if (CollectionUtils.isEmpty(this)) return null;
        return get(size() - 1);
    }
}
