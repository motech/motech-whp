package org.motechproject.whp.patient.domain;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.ektorp.support.TypeDiscriminator;
import org.hamcrest.Matchers;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.motechproject.model.MotechBaseDataObject;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.refdata.domain.Gender;
import org.motechproject.whp.refdata.domain.PatientStatus;
import org.motechproject.whp.refdata.domain.PhaseName;
import org.motechproject.whp.refdata.domain.TreatmentOutcome;

import java.util.ArrayList;
import java.util.List;

import static ch.lambdaj.Lambda.*;

@TypeDiscriminator("doc.type == 'Patient'")
@Data
public class Patient extends MotechBaseDataObject {

    private String patientId;
    private String firstName;
    private String lastName;
    private Gender gender;
    private String phoneNumber;
    private String phi;
    private PatientStatus status = PatientStatus.Open;
    private DateTime lastModifiedDate;
    private boolean onActiveTreatment = true;

    private Therapy currentTherapy;
    private List<Therapy> therapyHistory = new ArrayList<>();

    private boolean migrated;

    public Patient() {
    }

    public Patient(String patientId, String firstName, String lastName, Gender gender, String phoneNumber) {
        setPatientId(patientId);
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
    }

    public void addTreatment(Treatment treatment, DateTime dateModified) {
        currentTherapy.addTreatment(treatment, dateModified);

        setLastModifiedDate(dateModified);
        onActiveTreatment = true;
    }

    public void addTreatment(Treatment treatment, Therapy therapy, DateTime dateModified) {
        addTherapy(therapy);
        addTreatment(treatment, dateModified);
    }

    private void addTherapy(Therapy therapy) {
        if (currentTherapy != null) {
            therapyHistory.add(currentTherapy);
        }
        currentTherapy = therapy;
    }

    public List<Therapy> allTherapies() {
        ArrayList<Therapy> therapies = new ArrayList<Therapy>();
        therapies.add(currentTherapy);
        therapies.addAll(therapyHistory);
        return therapies;
    }

    @JsonIgnore
    public Treatment getTreatment(LocalDate date) {
        for (Therapy therapy : allTherapies()) {
            Treatment treatment = therapy.getTreatment(date);
            if (treatment != null) return treatment;
        }
        return null;
    }

    public void startTherapy(LocalDate firstDoseTakenDate) {
        currentTherapy.start(firstDoseTakenDate);
    }

    public void reviveLatestTherapy() {
        currentTherapy.revive();
    }

    public void closeCurrentTreatment(TreatmentOutcome treatmentOutcome, DateTime dateModified) {
        currentTherapy.closeCurrentTreatment(treatmentOutcome, dateModified);
        onActiveTreatment = false;
        setLastModifiedDate(dateModified);
    }

    public void pauseCurrentTreatment(String reasonForPause, DateTime dateModified) {
        currentTherapy.pauseCurrentTreatment(reasonForPause, dateModified);
        setLastModifiedDate(dateModified);
    }

    public void restartCurrentTreatment(String reasonForResumption, DateTime dateModified) {
        currentTherapy.restartCurrentTreatment(reasonForResumption, dateModified);
        setLastModifiedDate(dateModified);
    }

    public void nextPhaseName(PhaseName phaseName) {
        currentTherapy.getPhases().setNextPhaseName(phaseName);
    }

    @JsonIgnore
    public String currentTherapyId() {
        if (currentTherapy == null) return null;
        return this.currentTherapy.getUid();
    }

    @JsonIgnore
    public boolean isNearingPhaseTransition() {
        return currentTherapy.isNearingPhaseTransition();
    }

    @JsonIgnore
    public Integer getAge() {
        return currentTherapy.getPatientAge();
    }

    public void setPatientId(String patientId) {
        if (patientId == null)
            this.patientId = null;
        else
            this.patientId = patientId.toLowerCase();
    }

    public DateTime getLastModifiedDate() {
        return DateUtil.setTimeZone(lastModifiedDate);
    }

    @JsonIgnore
    public boolean hasCurrentTreatment() {
        return currentTherapy.hasCurrentTreatment();
    }

    @JsonIgnore
    public boolean isCurrentTreatmentClosed() {
        return currentTherapy.isCurrentTreatmentClosed();
    }

    @JsonIgnore
    public boolean isCurrentTreatmentPaused() {
        return currentTherapy.isCurrentTreatmentPaused();
    }

    @JsonIgnore
    public TreatmentOutcome getTreatmentOutcome() {
        return currentTherapy.getTreatmentOutcome();
    }

    @JsonIgnore
    public TreatmentInterruptions getCurrentTreatmentInterruptions() {
        return currentTherapy.getCurrentTreatmentInterruptions();
    }

    @JsonIgnore
    public SmearTestResults getSmearTestResults() {
        return currentTherapy.getSmearTestResults();
    }

    @JsonIgnore
    public WeightStatistics getWeightStatistics() {
        return currentTherapy.getWeightStatistics();
    }

    @JsonIgnore
    public boolean isValid(List<WHPErrorCode> errorCodes) {
        return currentTherapy.isValid(errorCodes);
    }

    public boolean isDoseDateInPausedPeriod(LocalDate doseDate) {
        Treatment treatment = getTreatment(doseDate);
        if (treatment != null) {
            if (treatment.isDoseDateInPausedPeriod(doseDate)) return true;
        }
        return false;
    }

    @JsonIgnore
    public void endCurrentPhase(LocalDate endDate) {
        currentTherapy.endCurrentPhase(endDate);
    }

    @JsonIgnore
    public void startNextPhase() {
        Phase phaseToBeStarted = currentTherapy.getPhase(currentTherapy.getPhases().getNextPhaseName());
        phaseToBeStarted.setStartDate(currentTherapy.getLastCompletedPhase().getEndDate().plusDays(1));
        nextPhaseName(null);
    }

    @JsonIgnore
    public boolean isTransitioning() {
        return currentTherapy.getCurrentPhase() == null && currentTherapy.getLastCompletedPhase() != null;
    }

    @JsonIgnore
    public boolean hasPhaseToTransitionTo() {
        return currentTherapy.getPhases().getNextPhaseName() != null;
    }

    @JsonIgnore
    public ArrayList<String> getPhasesNotPossibleToTransitionTo() {
        Phases phases = currentTherapy.getPhases();
        Phase currentPhase = currentTherapy.getCurrentPhase() == null ? currentTherapy.getLastCompletedPhase() : currentTherapy.getCurrentPhase();
        ArrayList<String> namesOfPhasesNotPossibleToTransitionTo = new ArrayList<>();
        if (currentPhase == null) return namesOfPhasesNotPossibleToTransitionTo;

        List<Phase> phasesNotPossibleToTransitionTo = phases.subList(0, phases.indexOf(currentPhase) + 1);
        for (Phase phase : phasesNotPossibleToTransitionTo) {
            namesOfPhasesNotPossibleToTransitionTo.add(phase.getName().name());
        }

        return namesOfPhasesNotPossibleToTransitionTo;
    }

    @JsonIgnore
    public int getRemainingDosesInCurrentPhase() {
        Phase currentPhase = currentTherapy.getCurrentPhase();
        return currentPhase != null ? currentPhase.remainingDoses(currentTherapy.getTreatmentCategory()) : 0;
    }

    @JsonIgnore
    public Integer numberOfDosesForPhase(PhaseName phaseName) {
        return currentTherapy.numberOfDosesForPhase(phaseName);
    }

    @JsonIgnore
    public Phase getLastCompletedPhase() {
        return currentTherapy.getLastCompletedPhase();
    }

    @JsonIgnore
    public Phase getCurrentPhase() {
        return currentTherapy.getCurrentPhase();
    }

    @JsonIgnore
    public int getRemainingDosesInLastCompletedPhase() {
        return getLastCompletedPhase().remainingDoses(currentTherapy.getTreatmentCategory());
    }

    //TODO: Extend patient to a PatientUIModel and move these UI specific methods there.
    @JsonIgnore
    public String getIPProgress() {
        int totalDoseCount = currentTherapy.totalDosesInIntensivePhases();
        int totalDoseTakenCount = currentTherapy.totalDosesTakenInIntensivePhases();

        Float completionPercentage = (totalDoseTakenCount / (float) totalDoseCount) * 100;
        return String.format("%d/%d (%.2f%%)", totalDoseTakenCount, totalDoseCount, completionPercentage.equals(Float.NaN) ? 0 : completionPercentage);
    }

    //TODO: Extend patient to a PatientUIModel and move these UI specific methods there.
    @JsonIgnore
    public String getCPProgress() {
        int totalDoseCount = currentTherapy.totalDosesInContinuationPhase();
        int totalDoseTakenCount = currentTherapy.totalDosesTakenInContinuationPhase();

        Float completionPercentage = (totalDoseTakenCount / (float) totalDoseCount) * 100;
        return String.format("%d/%d (%.2f%%)", totalDoseTakenCount, totalDoseCount, completionPercentage.equals(Float.NaN) ? 0 : completionPercentage);
    }

    public boolean currentPhaseDoseComplete() {
        return currentTherapy.currentPhaseDoseComplete();
    }

    @JsonIgnore
    public void adjustPhaseDates(LocalDate ipStartDate, LocalDate eipStartDate, LocalDate cpStartDate) {
        currentTherapy.adjustPhaseStartDates(ipStartDate, eipStartDate, cpStartDate);
    }

    @JsonIgnore
    public Integer getWeeksElapsed() {
        return currentTherapy.getWeeksElapsed();
    }

    @JsonIgnore
    public int getTotalDosesToHaveBeenTakenTillToday() {
        return currentTherapy.getTotalDosesToHaveBeenTakenTillToday();
    }

    @JsonIgnore
    public int getTotalNumberOfDosesTakenTillToday() {
        return currentTherapy.totalNumberOfDosesTakenTillToday();
    }

    @JsonIgnore
    public int getCumulativeDosesNotTaken() {
        if (getTotalDosesToHaveBeenTakenTillToday() - getTotalNumberOfDosesTakenTillToday() < 0) {
            return 0;
        } else {
            return getTotalDosesToHaveBeenTakenTillToday() - getTotalNumberOfDosesTakenTillToday();
        }
    }

    private Therapy getTherapy(String therapyUid) {
        if (currentTherapy.getUid().equals(therapyUid)) return currentTherapy;

        List<Therapy> therapyList = select(therapyHistory, having(on(Therapy.class).getUid(), Matchers.equalTo(therapyUid)));
        return therapyList.get(0);
    }

    public void setNumberOfDosesTaken(PhaseName phaseName, int dosesTaken) {
        currentTherapy.getPhase(phaseName).setNumberOfDosesTaken(dosesTaken);
    }

    public Treatment getCurrentTreatment() {
        return currentTherapy.getCurrentTreatment();
    }

    public void setCurrentTreatment(Treatment currentTreatment) {
        currentTherapy.setCurrentTreatment(currentTreatment);
    }

    public List<Treatment> getTreatments() {
        ArrayList<Treatment> treatments = new ArrayList<>();
        treatments.addAll(currentTherapy.getTreatments());
        for (Therapy therapy : therapyHistory) {
            treatments.addAll(therapy.getTreatments());
        }
        return treatments;
    }

}
