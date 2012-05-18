package org.motechproject.whp.patient.domain;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.joda.time.LocalDate;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;

public class TreatmentInterruptions extends ArrayList<TreatmentInterruption> {

    public TreatmentInterruptions() {
    }

    public TreatmentInterruptions(Collection<? extends TreatmentInterruption> treatmentInterruptions) {
        super(treatmentInterruptions);
    }

    @JsonIgnore
    public String getPauseReason(LocalDate asOnPillDate) {
        for (TreatmentInterruption treatmentInterruption : this) {
            if (treatmentInterruption.isTreatmentInterrupted(asOnPillDate))
                return treatmentInterruption.getReasonForPause();
        }
        return null;
    }

    public TreatmentInterruption latestInterruption() {
        if (CollectionUtils.isEmpty(this)) return null;
        return get(size() - 1);
    }
}
