package org.motechproject.whp.patient.domain;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.motechproject.model.DayOfWeek;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.common.util.WHPDate;
import org.motechproject.whp.common.util.WHPDateUtil;
import org.motechproject.whp.refdata.domain.*;

import java.util.ArrayList;
import java.util.List;

import static org.motechproject.whp.common.domain.TreatmentWeekInstance.currentAdherenceCaptureWeek;
import static org.motechproject.whp.common.util.WHPDateUtil.numberOf_DDD_Between;

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

    private Treatment currentTreatment;
    private List<Treatment> treatments = new ArrayList<>();
    private Phases phases = new Phases();
    private DoseInterruptions doseInterruptions = new DoseInterruptions();

    public Therapy() {
        setUid(generateUid());
    }

    public Therapy(TreatmentCategory treatmentCategory, DiseaseClass diseaseClass, Integer patientAge) {
        this();
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
        PhaseRecord currentPhase = phases.getCurrentPhase();
        return currentPhase != null && currentPhase.remainingDoses(treatmentCategory) <= treatmentCategory.getDosesPerWeek();
    }

    @JsonIgnore
    public boolean isTransitioning() {
        return getCurrentPhase() == null && getLastCompletedPhase() != null;
    }

    @JsonIgnore
    public PhaseRecord getCurrentPhase() {
        return phases.getCurrentPhase();
    }

    @JsonIgnore
    public PhaseRecord getLastCompletedPhase() {
        return phases.getLastCompletedPhase();
    }

    public DateTime getCreationDate() {
        return DateUtil.setTimeZone(creationDate);
    }

    public void start(LocalDate therapyStartDate) {
        phases.setIPStartDate(therapyStartDate);
        setStartDate(therapyStartDate);
    }

    public boolean currentPhaseDoseComplete() {
        PhaseRecord currentPhase = phases.getCurrentPhase();
        return currentPhase != null && currentPhase.remainingDoses(treatmentCategory) <= 0;
    }

    public boolean latestPhaseDoseComplete() {
        PhaseRecord latestPhase = latestPhaseRecord();
        return latestPhase != null && latestPhase.remainingDoses(treatmentCategory) <= 0;
    }

    public PhaseRecord latestPhaseRecord() {
        PhaseRecord latestPhase = phases.getCurrentPhase();
        if (latestPhase == null) {
            latestPhase = phases.getLastCompletedPhase();
        }
        return latestPhase;
    }

    public void setStartDate(LocalDate therapyStartDate) {
        startDate = therapyStartDate;
    }

    @JsonIgnore
    public String getStartDateAsString() {
        return WHPDate.date(startDate).value();
    }

    @JsonIgnore
    public void endLatestPhase(LocalDate endDate) {
        latestPhaseRecord().setEndDate(endDate);
    }

    @JsonIgnore
    public void adjustPhaseStartDates(LocalDate ipStartDate, LocalDate eipStartDate, LocalDate cpStartDate) {
        start(ipStartDate);
        phases.setEIPStartDate(eipStartDate);
        phases.setCPStartDate(cpStartDate);
    }

    public Integer numberOfDosesForPhase(Phase phaseName) {
        return treatmentCategory.numberOfDosesForPhase(phaseName);
    }

    @JsonIgnore
    public int totalNumberOfDosesTakenTillLastSunday(LocalDate referenceDate) {
        return phases.getTotalDosesTakenTillLastSunday(referenceDate);
    }

    @JsonIgnore
    public int getTotalDosesToHaveBeenTakenTillLastSunday() {
        if (startDate != null) {
            int totalDoses = 0;
            //pointing to sunday just past
            LocalDate endDate = currentAdherenceCaptureWeek().dateOf(DayOfWeek.Sunday);
            if (phases.getCPEndDate() != null && phases.getCPEndDate().isBefore(endDate)) {
                endDate = phases.getCPEndDate();
            }
            for (DayOfWeek dayOfWeek : treatmentCategory.getPillDays()) {
                totalDoses = totalDoses + numberOf_DDD_Between(startDate, endDate, dayOfWeek);
            }
            return totalDoses;
        } else {
            return 0;
        }
    }

    @JsonIgnore
    public Integer getWeeksElapsed() {
        if (phases.isOrHasBeenOnIp())
            return WHPDateUtil.weeksElapsedSince(phases.getIPStartDate());
        else return null;
    }

    @JsonIgnore
    public boolean isOrHasBeenOnIP() {
        return phases.isOrHasBeenOnIp();
    }

    @JsonIgnore
    public boolean hasBeenOnCP() {
        return phases.hasBeenOnCp();
    }

    public void addTreatment(Treatment treatment, DateTime dateModified) {
        if (currentTreatment != null) {
            treatments.add(currentTreatment);
        }
        currentTreatment = treatment;
        treatment.setStartDate(dateModified.toLocalDate());
    }

    @JsonIgnore
    public Treatment getTreatment(LocalDate date) {
        if (currentTreatment.isDateInTreatment(date)) {
            return currentTreatment;
        }
        for (int i = treatments.size() - 1; i >= 0; i--) {
            Treatment treatment = treatments.get(i);
            if (treatment.isDateInTreatment(date)) {
                return treatment;
            }
        }
        return null;
    }

    public void closeCurrentTreatment(TreatmentOutcome treatmentOutcome, DateTime dateModified) {
        currentTreatment.close(treatmentOutcome, dateModified);
        close(dateModified);
    }

    public void pauseCurrentTreatment(String reasonForPause, DateTime dateModified) {
        currentTreatment.pause(reasonForPause, dateModified);
    }

    public void restartCurrentTreatment(String reasonForResumption, DateTime dateModified) {
        currentTreatment.resume(reasonForResumption, dateModified);
    }

    @JsonIgnore
    public boolean hasCurrentTreatment() {
        return currentTreatment != null;
    }

    @JsonIgnore
    public boolean isCurrentTreatmentClosed() {
        return TherapyStatus.Closed == status;
    }

    @JsonIgnore
    public boolean isCurrentTreatmentPaused() {
        return currentTreatment.isPaused();
    }

    @JsonIgnore
    public TreatmentOutcome getTreatmentOutcome() {
        return currentTreatment.getTreatmentOutcome();
    }

    @JsonIgnore
    public TreatmentInterruptions getCurrentTreatmentInterruptions() {
        return currentTreatment.getInterruptions();
    }

    @JsonIgnore
    public SmearTestResults getSmearTestResults() {
        return currentTreatment.getSmearTestResults();
    }

    @JsonIgnore
    public WeightStatistics getWeightStatistics() {
        return currentTreatment.getWeightStatistics();
    }

    @JsonIgnore
    public boolean isValid(List<WHPErrorCode> errorCodes) {
        return currentTreatment.isValid(errorCodes);
    }

    @JsonIgnore
    public void setNumberOfDosesTaken(Phase phase, int numberOfDoses, LocalDate asOf) {
        phases.setNumberOfDosesIn(phase, numberOfDoses, asOf);
    }

    @JsonIgnore
    public int getNumberOfDosesTaken(Phase phase) {
        return phases.getNumberOfDosesTaken(phase);
    }

    @JsonIgnore
    public int getTotalDoesIn(Phase phase) {
        if (phases.hasBeenOn(phase)) {
            return treatmentCategory.numberOfDosesForPhase(phase);
        }
        return 0;
    }

    @JsonIgnore
    public LocalDate getPhaseStartDate(Phase phase) {
        return phases.getStartDate(phase);
    }

    @JsonIgnore
    public LocalDate getPhaseEndDate(Phase phase) {
        return phases.getEndDate(phase);
    }

    @JsonIgnore
    public int getNumberOfDosesTakenInIntensivePhases() {
        return getNumberOfDosesTaken(Phase.IP) + getNumberOfDosesTaken(Phase.EIP);
    }

    @JsonIgnore
    public int getTotalDoesInIntensivePhases() {
        return getTotalDoesIn(Phase.IP) + getTotalDoesIn(Phase.EIP);
    }

    @JsonIgnore
    public boolean isCurrentlyDoseInterrupted() {
        return doseInterruptions.isCurrentlyDoseInterrupted();
    }

    @JsonIgnore
    public DoseInterruption getLongestDoseInterruption() {
        return doseInterruptions.longestInterruption(treatmentCategory);
    }

    public void dosesMissedSince(LocalDate startDate) {
        DoseInterruption doseInterruption = new DoseInterruption(startDate);
        doseInterruptions.add(doseInterruption);
    }

    public void dosesResumedOnAfterBeingInterrupted(LocalDate endDate) {
        doseInterruptions.latestInterruption().endMissedPeriod(endDate);
    }

    public boolean hasBeenOn(Phase phase) {
        return phases.hasBeenOn(phase);
    }

    private String generateUid() {
        return String.valueOf(DateUtil.now().getMillis());
    }

    public void clearDoseInterruptionsForUpdate() {
        doseInterruptions.clear();
    }

    public boolean hasStarted() {
        return startDate != null;
    }
}
