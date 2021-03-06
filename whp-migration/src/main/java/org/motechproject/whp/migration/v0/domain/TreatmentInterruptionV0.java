package org.motechproject.whp.migration.v0.domain;

import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.joda.time.LocalDate;

import java.util.List;

import static org.motechproject.util.DateUtil.*;

public class TreatmentInterruptionV0 {

    @JsonProperty
    @Getter
    @Setter
    private LocalDate pauseDate;

    @JsonProperty
    @Getter
    @Setter
    private String reasonForPause;

    @JsonProperty
    @Getter
    @Setter
    private LocalDate resumptionDate;

    @JsonProperty
    @Getter
    @Setter
    private String reasonForResumption;

    //required for ektorp
    private TreatmentInterruptionV0() {
    }

    public TreatmentInterruptionV0(String reasonForPause, LocalDate pauseDate) {
        this.reasonForPause = reasonForPause;
        this.pauseDate = pauseDate;
    }

    public void resumeTreatment(String reasonForResumption, LocalDate resumptionDate) {
        this.reasonForResumption = reasonForResumption;
        this.resumptionDate = resumptionDate;
    }

    @JsonIgnore
    public boolean isCurrentlyPaused() {
        return (pauseDate != null && resumptionDate == null);
    }

    @JsonIgnore
    public boolean isTreatmentInterrupted(List<LocalDate> treatmentWeekDates) {
        if (isCurrentlyPaused()) {
            for (LocalDate treatmentWeekDate : treatmentWeekDates) {
                if (isOnOrAfter(newDateTime(treatmentWeekDate), newDateTime(pauseDate))) {
                    return true;
                }
            }
        } else {
            for (LocalDate treatmentWeekDate : treatmentWeekDates) {
                if(isOnOrAfter(newDateTime(treatmentWeekDate), newDateTime(pauseDate)) && isOnOrBefore(newDateTime(treatmentWeekDate), newDateTime(resumptionDate))){
                    return true;
                }
            }
        }
        return false;
    }
}