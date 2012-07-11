package org.motechproject.whp.patient.domain;

import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.joda.time.LocalDate;
import org.motechproject.whp.refdata.domain.Phase;

import java.util.ArrayList;
import java.util.List;

import static org.motechproject.util.DateUtil.today;

public class Phases {

    @JsonProperty
    private PhaseRecords phaseRecords = new PhaseRecords();
    @JsonProperty
    private PhaseTransitions history = new PhaseTransitions();

    @Getter
    @Setter
    private Phase nextPhase = null;

    @JsonIgnore
    public boolean ipPhaseWasExtended() {
        return history.contains(Phase.EIP);
    }

    @JsonIgnore
    public boolean isOrHasBeenOnCp() {
        return history.contains(Phase.CP);
    }

    @JsonIgnore
    public boolean isOrHasBeenOnIp() {
        return history.contains(Phase.IP);
    }

    @JsonIgnore
    public PhaseRecord getLastCompletedPhase() {
        PhaseRecord record = null;
        for (Phase phase : history.allPhases()) {
            if (null != phaseRecords.get(phase).getEndDate()) {
                record = phaseRecords.get(phase);
            }
        }
        return record;
    }

    @JsonIgnore
    public PhaseRecord getCurrentPhase() {
        if (!history.isEmpty() && phaseRecords.get(history.latestPhase()).getEndDate() == null) {
            return phaseRecords.get(history.latestPhase());
        }
        return null;
    }

    @JsonIgnore
    public LocalDate getIPStartDate() {
        return getStartDate(Phase.IP);
    }

    @JsonIgnore
    void setIPStartDate(LocalDate startDate) {
        setPhaseStartDate(startDate, Phase.IP);
    }

    @JsonIgnore
    public LocalDate getIPLastDate() {
        return getPhaseLastDate(Phase.IP);
    }

    @JsonIgnore
    public LocalDate getEIPStartDate() {
        return getStartDate(Phase.EIP);
    }

    @JsonIgnore
    public void setEIPStartDate(LocalDate eipStartDate) {
        setPhaseStartDate(eipStartDate, Phase.EIP);
    }

    @JsonIgnore
    public LocalDate getEIPLastDate() {
        return getPhaseLastDate(Phase.EIP);
    }


    @JsonIgnore
    public LocalDate getCPStartDate() {
        return getStartDate(Phase.CP);
    }

    @JsonIgnore
    public void setCPStartDate(LocalDate cpStartDate) {
        setPhaseStartDate(cpStartDate, Phase.CP);
    }

    @JsonIgnore
    public LocalDate getCPLastDate() {
        return getPhaseLastDate(Phase.CP);
    }

    @JsonIgnore
    public int getTotalDosesTakenTillLastSunday(LocalDate referenceDate) {
        int total = 0;
        for (Phase phase : history.allPhases()) {
            total += phaseRecords.get(phase).getNumberOfDosesTakenAsOfLastSunday(referenceDate);
        }
        return total;
    }

    // TODO: Remove this
    @JsonIgnore
    public List<Phase> getHistoryOfPhases() {
        return new ArrayList<>(history.allPhases());
    }

    public void startNextPhase() {
        PhaseRecord record = phaseRecords.get(history.latestPhase());
        setPhaseStartDate(record.getEndDate().plusDays(1), nextPhase);
        nextPhase = null;
    }

    @JsonIgnore
    public void setNumberOfDosesIn(Phase phase, int numberOfDoses, LocalDate asOf) {
        if (history.contains(phase)) {
            phaseRecords.get(phase).setNumberOfDosesTaken(numberOfDoses, asOf);
        }
    }

    @JsonIgnore
    public int getNumberOfDosesTaken(Phase phase) {
        if (history.contains(phase)) {
            return phaseRecords.get(phase).getNumberOfDosesTaken();
        } else {
            return 0;
        }
    }

    @JsonIgnore
    public int getNumberOfDosesTakenAsOfLastSunday(Phase phase, LocalDate referenceDate) {
        if (history.contains(phase)) {
            return phaseRecords.get(phase).getNumberOfDosesTakenAsOfLastSunday(referenceDate);
        } else {
            return 0;
        }
    }

    @JsonIgnore
    public LocalDate getStartDate(Phase phase) {
        if (history.contains(phase)) {
            return phaseRecords.get(phase).getStartDate();
        } else {
            return null;
        }
    }

    @JsonIgnore
    public LocalDate getEndDate(Phase phase) {
        if (history.contains(phase)) {
            return phaseRecords.get(phase).getEndDate();
        } else {
            return null;
        }
    }

    @JsonIgnore
    public LocalDate getNextPhaseStartDate(Phase phase) {
        Phase nextPhase = history.getNextPhase(phase);
        return (null == nextPhase) ? null : phaseRecords.get(nextPhase).getStartDate();
    }

    public boolean hasBeenOn(Phase phase) {
        return history.contains(phase);
    }

    private void setPhaseStartDate(LocalDate startDate, Phase phase) {
        if (startDate != null) {
            phaseRecords.recordTransition(history.add(phase), startDate);
        } else {
            phaseRecords.removeTransition(history.remove(phase));
        }
    }

    private LocalDate getPhaseLastDate(Phase phase) {
        if (history.contains(phase)) {
            LocalDate lastDate = phaseRecords.get(phase).getEndDate();
            return lastDate != null ? lastDate : today();
        } else {
            return null;
        }
    }
}
