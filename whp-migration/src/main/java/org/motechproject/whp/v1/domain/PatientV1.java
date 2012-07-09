package org.motechproject.whp.v1.domain;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.ektorp.support.TypeDiscriminator;
import org.hamcrest.Matchers;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.motechproject.model.MotechBaseDataObject;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.refdata.domain.Gender;
import org.motechproject.whp.refdata.domain.PatientStatus;
import org.motechproject.whp.refdata.domain.PhaseName;
import org.motechproject.whp.refdata.domain.TreatmentOutcome;

import java.util.ArrayList;
import java.util.List;

import static ch.lambdaj.Lambda.*;

@TypeDiscriminator("doc.type == 'Patient'")
@Data
public class PatientV1 extends MotechBaseDataObject {

    private String patientId;
    private String firstName;
    private String lastName;
    private GenderV1 gender;
    private String phoneNumber;
    private String phi;
    private PatientStatusV1 status = PatientStatusV1.Open;
    private DateTime lastModifiedDate;
    private boolean onActiveTreatment = true;

    private TherapyV1 currentTherapy;
    private List<TherapyV1> therapyHistory = new ArrayList<>();

    private boolean migrated;

    public PatientV1() {
    }

    public PatientV1(String patientId, String firstName, String lastName, GenderV1 gender, String phoneNumber) {
        setPatientId(patientId);
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
    }

    public void addTreatment(TreatmentV1 treatment, DateTime dateModified) {
        currentTherapy.addTreatment(treatment, dateModified);

        setLastModifiedDate(dateModified);
        onActiveTreatment = true;
    }

    public void addTreatment(TreatmentV1 treatment, TherapyV1 therapy, DateTime dateModified) {
        addTherapy(therapy);
        addTreatment(treatment, dateModified);
    }

    private void addTherapy(TherapyV1 therapy) {
        if (currentTherapy != null) {
            therapyHistory.add(currentTherapy);
        }
        currentTherapy = therapy;
    }

    public List<TherapyV1> allTherapies() {
        ArrayList<TherapyV1> therapies = new ArrayList<TherapyV1>();
        therapies.add(currentTherapy);
        therapies.addAll(therapyHistory);
        return therapies;
    }

    @JsonIgnore
    public TreatmentV1 getTreatment(LocalDate date) {
        for (TherapyV1 therapy : allTherapies()) {
            TreatmentV1 treatment = therapy.getTreatment(date);
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

    public void closeCurrentTreatment(TreatmentOutcomeV1 treatmentOutcome, DateTime dateModified) {
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

    public void nextPhaseName(PhaseNameV1 phaseName) {
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
    public TreatmentOutcomeV1 getTreatmentOutcome() {
        return currentTherapy.getTreatmentOutcome();
    }

    @JsonIgnore
    public TreatmentInterruptionsV1 getCurrentTreatmentInterruptions() {
        return currentTherapy.getCurrentTreatmentInterruptions();
    }

    @JsonIgnore
    public SmearTestResultsV1 getSmearTestResults() {
        return currentTherapy.getSmearTestResults();
    }

    @JsonIgnore
    public WeightStatisticsV1 getWeightStatistics() {
        return currentTherapy.getWeightStatistics();
    }

    public boolean isDoseDateInPausedPeriod(LocalDate doseDate) {
        TreatmentV1 treatment = getTreatment(doseDate);
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
        PhaseV1 phaseToBeStarted = currentTherapy.getPhase(currentTherapy.getPhases().getNextPhaseName());
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
        PhasesV1 phases = currentTherapy.getPhases();
        PhaseV1 currentPhase = currentTherapy.getCurrentPhase() == null ? currentTherapy.getLastCompletedPhase() : currentTherapy.getCurrentPhase();
        ArrayList<String> namesOfPhasesNotPossibleToTransitionTo = new ArrayList<>();
        if (currentPhase == null) return namesOfPhasesNotPossibleToTransitionTo;

        List<PhaseV1> phasesNotPossibleToTransitionTo = phases.subList(0, phases.indexOf(currentPhase) + 1);
        for (PhaseV1 phase : phasesNotPossibleToTransitionTo) {
            namesOfPhasesNotPossibleToTransitionTo.add(phase.getName().name());
        }

        return namesOfPhasesNotPossibleToTransitionTo;
    }

    @JsonIgnore
    public int getRemainingDosesInCurrentPhase() {
        PhaseV1 currentPhase = currentTherapy.getCurrentPhase();
        return currentPhase != null ? currentPhase.remainingDoses(currentTherapy.getTreatmentCategory()) : 0;
    }

    @JsonIgnore
    public Integer numberOfDosesForPhase(PhaseNameV1 phaseName) {
        return getCurrentTherapy().numberOfDosesForPhase(phaseName);
    }

    @JsonIgnore
    public PhaseV1 getLastCompletedPhase() {
        return getCurrentTherapy().getLastCompletedPhase();
    }

    @JsonIgnore
    public PhaseV1 getCurrentPhase() {
        return getCurrentTherapy().getCurrentPhase();
    }

    @JsonIgnore
    public int getRemainingDosesInLastCompletedPhase() {
        return getLastCompletedPhase().remainingDoses(currentTherapy.getTreatmentCategory());
    }

    public boolean currentPhaseDoseComplete() {
        return getCurrentTherapy().currentPhaseDoseComplete();
    }

    @JsonIgnore
    public void adjustPhaseDates(LocalDate ipStartDate, LocalDate eipStartDate, LocalDate cpStartDate) {
        getCurrentTherapy().adjustPhaseStartDates(ipStartDate, eipStartDate, cpStartDate);
    }

    @JsonIgnore
    public Integer getWeeksElapsed() {
        return getCurrentTherapy().getWeeksElapsed();
    }

    private TherapyV1 getTherapy(String therapyUid) {
        if (currentTherapy.getUid().equals(therapyUid)) return currentTherapy;

        List<TherapyV1> therapyList = select(therapyHistory, having(on(TherapyV1.class).getUid(), Matchers.equalTo(therapyUid)));
        return therapyList.get(0);
    }

    public void setNumberOfDosesTaken(PhaseNameV1 phaseName, int dosesTaken) {
        getCurrentTherapy().getPhase(phaseName).setNumberOfDosesTaken(dosesTaken);
    }

    public TreatmentV1 getCurrentTreatment() {
        return currentTherapy.getCurrentTreatment();
    }

    public void setCurrentTreatment(TreatmentV1 currentTreatment) {
        currentTherapy.setCurrentTreatment(currentTreatment);
    }

    public List<TreatmentV1> getTreatments() {
        ArrayList<TreatmentV1> treatments = new ArrayList<>();
        treatments.addAll(currentTherapy.getTreatments());
        for (TherapyV1 therapy : therapyHistory) {
            treatments.addAll(therapy.getTreatments());
        }
        return treatments;
    }

}
