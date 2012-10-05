package org.motechproject.whp.patient.domain;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.joda.time.LocalDate;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.refdata.domain.TreatmentCategory;

import java.io.Serializable;

import static org.motechproject.util.DateUtil.today;
import static org.motechproject.whp.common.util.WHPDateUtil.isOnOrAfter;
import static org.motechproject.whp.common.util.WHPDateUtil.numberOf_DDD_Between;

public class DoseInterruption implements Comparable<DoseInterruption>, Serializable {

    @JsonProperty
    private LocalDate startDate;

    //till the next dose was taken
    @JsonProperty
    private LocalDate endDate;

    //required for ektorp
    public DoseInterruption() {
    }

    public DoseInterruption(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void endMissedPeriod(LocalDate endDate){
        if (isOnOrAfter(endDate, startDate)) {
            this.endDate = endDate;
        }
    }

    public int getMissedDoseCount(TreatmentCategory treatmentCategory){
        if (startDate != null) {
            int totalDoses = 0;
            LocalDate endDate = this.endDate == null ? today() : this.endDate;
            for (DayOfWeek dayOfWeek : treatmentCategory.getPillDays()) {
                totalDoses = totalDoses + numberOf_DDD_Between(startDate, endDate, dayOfWeek);
            }
            return totalDoses;
        } else {
            return 0;
        }
    }

    public LocalDate startDate() {
        return startDate;
    }

    public LocalDate endDate() {
        return endDate;
    }

    @Override
    public int compareTo(DoseInterruption other) {
        if (startDate == null) {
            if (other.startDate == null) {
                return 0;
            } else {
                return -1;
            }
        } else if (other.startDate == null) {
            return 1;
        } else {
            return startDate.compareTo(other.startDate);
        }
    }

    @JsonIgnore
    public boolean isOngoing() {
        return endDate == null;
    }
}
