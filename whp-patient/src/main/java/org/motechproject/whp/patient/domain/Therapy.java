package org.motechproject.whp.patient.domain;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.common.WHPDate;
import org.motechproject.whp.refdata.domain.DiseaseClass;
import org.motechproject.whp.refdata.domain.PhaseName;
import org.motechproject.whp.refdata.domain.TherapyStatus;
import org.motechproject.whp.refdata.domain.TreatmentCategory;

import java.util.Arrays;

@Data
public class Therapy {

    private String uid;

    private Integer patientAge;
    private DateTime creationDate;
    private LocalDate startDate;
    private LocalDate closeDate;
    private TherapyStatus status = TherapyStatus.Ongoing;
    private TreatmentCategory treatmentCategory;
    private DiseaseClass diseaseClass;
    private Phases phases = new Phases(Arrays.asList(new Phase(PhaseName.IP), new Phase(PhaseName.EIP), new Phase(PhaseName.CP)));

    public Therapy() {
    }

    public Therapy(TreatmentCategory treatmentCategory, DiseaseClass diseaseClass, Integer patientAge) {
        this.treatmentCategory = treatmentCategory;
        this.diseaseClass = diseaseClass;
        this.patientAge = patientAge;
    }

    public void close(DateTime dateModified) {
        closeDate = dateModified.toLocalDate();
        status = TherapyStatus.Closed;
    }

    public void revive() {
        closeDate = null;
        status = TherapyStatus.Ongoing;
    }

    @JsonIgnore
    public boolean isClosed() {
        return TherapyStatus.Closed == status;
    }

    @JsonIgnore
    public boolean isNearingPhaseTransition() {
        Phase currentPhase = phases.getCurrentPhase();
        return currentPhase != null && remainingDoses(currentPhase) <= treatmentCategory.getDosesPerWeek();
    }

    @JsonIgnore
    public Phase getCurrentPhase() {
        return phases.getCurrentPhase();
    }

    @JsonIgnore
    public Phase getLastCompletedPhase() {
        return phases.getLastCompletedPhase();
    }

    @JsonIgnore
    public Integer cumulativeNumberOfDosesSoFar() {
        PhaseName phaseName = getCurrentPhase() == null ? getLastCompletedPhase().getName() : getCurrentPhase().getName();
        Integer totalNumberOfDoses = 0;
        for (Phase phase : phases.getAll()) {
            if (phase.hasStarted()) {
                totalNumberOfDoses = totalNumberOfDoses + treatmentCategory.numberOfDosesForPhase(phase.getName());
            }
            if (phaseName.equals(phase.getName())) break;
        }
        return totalNumberOfDoses;
    }

    public DateTime getCreationDate() {
        return DateUtil.setTimeZone(creationDate);
    }

    public void start(LocalDate therapyStartDate) {
        phases.setIPStartDate(therapyStartDate);
        setStartDate(therapyStartDate);
    }

    public boolean currentPhaseDoseComplete() {
        Phase currentPhase = phases.getCurrentPhase();
        return currentPhase != null && remainingDoses(currentPhase) <= 0;
    }

    void setStartDate(LocalDate therapyStartDate) {
        startDate = therapyStartDate;
    }

    int remainingDoses(Phase phase) {
        int numberOfDosesTaken = phase.getNumberOfDosesTaken();
        return treatmentCategory.numberOfDosesForPhase(phase.getName()) - numberOfDosesTaken;
    }

    @JsonIgnore
    public Phase getPhase(PhaseName phaseName) {
        return phases.getByPhaseName(phaseName);
    }

    @JsonIgnore
    public String getStartDateAsString() {
        return WHPDate.date(startDate).value();
    }

    @JsonIgnore
    public void endCurrentPhase(LocalDate endDate) {
        Phase currentPhase = getCurrentPhase();
        if (currentPhase != null) currentPhase.setEndDate(endDate);
    }

    @JsonIgnore
    public void adjustPhaseStartDates(LocalDate ipStartDate, LocalDate eipStartDate, LocalDate cpStartDate) {
        start(ipStartDate);
        phases.setEIPStartDate(eipStartDate);
        phases.setCPStartDate(cpStartDate);
    }
}
