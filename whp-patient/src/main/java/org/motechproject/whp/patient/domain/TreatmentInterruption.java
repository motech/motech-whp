package org.motechproject.whp.patient.domain;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.joda.time.LocalDate;

public class TreatmentInterruption {

    @JsonProperty
    private LocalDate pauseDate;
    @JsonProperty
    private String reasonForPause;
    @JsonProperty
    private LocalDate restartDate;
    @JsonProperty
    private String reasonForRestart;

    //required for ektorp
    private TreatmentInterruption() {
    }

    public TreatmentInterruption(String reasonForPause, LocalDate pauseDate) {
        this.pauseDate = pauseDate;
        this.reasonForPause = reasonForPause;
    }

    public void resumeTreatment(String reasonForRestart, LocalDate restartDate) {
        this.restartDate = restartDate;
        this.reasonForRestart = reasonForRestart;
    }

    @JsonIgnore
    public boolean isPaused() {
        return (pauseDate != null && restartDate == null);
    }
}
