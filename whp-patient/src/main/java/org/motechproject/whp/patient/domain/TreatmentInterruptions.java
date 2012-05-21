package org.motechproject.whp.patient.domain;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.joda.time.LocalDate;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TreatmentInterruptions extends ArrayList<TreatmentInterruption> {

    public TreatmentInterruptions() {
    }

    public TreatmentInterruptions(Collection<? extends TreatmentInterruption> treatmentInterruptions) {
        super(treatmentInterruptions);
    }

    @JsonIgnore
    public List<String> getPauseReasons(LocalDate startingOn, LocalDate asOf) {
        List<String> pauseReasons = new ArrayList<String>();
        for (TreatmentInterruption treatmentInterruption : this) {
            if (treatmentInterruption.isTreatmentInterrupted(startingOn, asOf))
                pauseReasons.add(treatmentInterruption.getReasonForPause());
        }
        return pauseReasons;
    }

    public TreatmentInterruption latestInterruption() {
        if (CollectionUtils.isEmpty(this)) return null;
        return get(size() - 1);
    }
}
