package org.motechproject.whp.patient.domain;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.joda.time.LocalDate;
import org.motechproject.whp.common.exception.WHPDomainException;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class DoseInterruptions extends ArrayList<DoseInterruption> implements Serializable {

    public DoseInterruptions() {
    }

    public DoseInterruptions(Collection<? extends DoseInterruption> doseInterruptions) {
        super(doseInterruptions);
        Collections.sort(this);
    }

    @Override
    public boolean add(DoseInterruption doseInterruption) {
        if (latestInterruption()!= null && latestInterruption().endDate() == null) {
            throw new WHPDomainException("Cannot add new dose interruption without closing existing interruption.");
        }
        super.add(doseInterruption);
        Collections.sort(this);
        return true;
    }

    public DoseInterruption latestInterruption() {
        if (CollectionUtils.isEmpty(this)) return null;
        return get(size() - 1);
    }

    @JsonIgnore
    public DoseInterruption longestInterruption(TreatmentCategory treatmentCategory){
        if (latestInterruption() == null) return null;
        DoseInterruption longestInterruption = this.get(0);
        for (DoseInterruption interruption : this) {
            if (interruption.getMissedDoseCount(treatmentCategory) > longestInterruption.getMissedDoseCount(treatmentCategory)){
                longestInterruption = interruption;
            }
        }
        return longestInterruption;
    }

    @JsonIgnore
    public boolean isCurrentlyDoseInterrupted() {
        DoseInterruption latestDoseInterruption = latestInterruption();
        return latestDoseInterruption != null && latestDoseInterruption.isOngoing();
    }

    public int getCumulativeMissedDoseCount(TreatmentCategory treatmentCategory, LocalDate startDate) {
        int cumulativeMissedDoseCount = 0;
        for(DoseInterruption interruption: this){
            cumulativeMissedDoseCount += interruption.getMissedDoseCount(treatmentCategory, startDate);
        }
        return cumulativeMissedDoseCount;
    }

    @JsonIgnore
    public DoseInterruption ongoingDoseInterruption() {
        if(!isCurrentlyDoseInterrupted()){
            return null;
        }
        return latestInterruption();
    }
}
