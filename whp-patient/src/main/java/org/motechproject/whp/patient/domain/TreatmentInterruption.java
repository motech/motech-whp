package org.motechproject.whp.patient.domain;

import lombok.AccessLevel;
import lombok.Getter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.joda.time.LocalDate;
import org.motechproject.util.DateUtil;

public class TreatmentInterruption {

    @JsonProperty
    @Getter(AccessLevel.PACKAGE)
    private LocalDate pauseDate;

    @JsonProperty
    @Getter(AccessLevel.PACKAGE)
    private String reasonForPause;

    @JsonProperty
    @Getter(AccessLevel.PACKAGE)
    private LocalDate resumptionDate;

    @JsonProperty
    @Getter(AccessLevel.PACKAGE)
    private String reasonForResumption;

    //required for ektorp
    private TreatmentInterruption() {
    }

    public TreatmentInterruption(String reasonForPause, LocalDate pauseDate) {
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
    public boolean isTreatmentInterrupted(LocalDate startingOn, LocalDate asOfDate) {
        boolean pauseDateOnOrBeforeAsOfDate = DateUtil.isOnOrBefore(DateUtil.newDateTime(pauseDate), DateUtil.newDateTime(asOfDate));
        boolean pauseDateAfterStartingDate = DateUtil.isOnOrAfter(DateUtil.newDateTime(pauseDate), DateUtil.newDateTime(startingOn));
        if(isCurrentlyPaused()) return pauseDateOnOrBeforeAsOfDate;
        return pauseDateAfterStartingDate && pauseDateOnOrBeforeAsOfDate;
    }
}
