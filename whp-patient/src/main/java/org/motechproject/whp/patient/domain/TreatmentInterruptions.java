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
    public boolean isTreatmentInterrupted(LocalDate pillDate) {
        for (TreatmentInterruption treatmentInterruption : this) {
            if (treatmentInterruption.isTreatmentInterrupted(pillDate))
                return true;
        }
        return false;
    }

    public TreatmentInterruption latestInterruption() {
        if (CollectionUtils.isEmpty(this)) return null;
        return get(size() - 1);
    }
}
