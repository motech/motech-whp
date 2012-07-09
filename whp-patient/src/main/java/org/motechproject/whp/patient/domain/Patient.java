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
import org.motechproject.whp.refdata.domain.Phase;
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

    public void nextPhaseName(Phase phaseName) {
        currentTherapy.getPhases().setNextPhase(phaseName);
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
        currentTherapy.getPhases().startNextPhase();
    }

    @JsonIgnore
    public boolean isTransitioning() {
        return currentTherapy.getCurrentPhase() == null && currentTherapy.getLastCompletedPhase() != null;
    }

    @JsonIgnore
    public boolean hasPhaseToTransitionTo() {
        return currentTherapy.getPhases().getNextPhase() != null;
    }

    @JsonIgnore
    public List<Phase> getHistoryOfPhases() {
        return currentTherapy.getPhases().getHistoryOfPhases();
    }

    @JsonIgnore
    public int getRemainingDosesInCurrentPhase() {
        PhaseRecord currentPhase = currentTherapy.getCurrentPhase();
        return currentPhase != null ? currentPhase.remainingDoses(currentTherapy.getTreatmentCategory()) : 0;
    }

    @JsonIgnore
    public Integer numberOfDosesForPhase(Phase phaseName) {
        return currentTherapy.numberOfDosesForPhase(phaseName);
    }

    @JsonIgnore
    public PhaseRecord getLastCompletedPhase() {
        return currentTherapy.getLastCompletedPhase();
    }

    @JsonIgnore
    public PhaseRecord getCurrentPhase() {
        return currentTherapy.getCurrentPhase();
    }

    @JsonIgnore
    public int getRemainingDosesInLastCompletedPhase() {
        return getLastCompletedPhase().remainingDoses(currentTherapy.getTreatmentCategory());
    }

    @JsonIgnore
    public String getIPProgress() {
        int totalDoseTakenCount = currentTherapy.getNumberOfDosesTakenInIntensivePhases();
        int totalDoseCount = currentTherapy.getTotalDoesInIntensivePhases();

        return doseCompletionMessage(totalDoseCount, totalDoseTakenCount);
    }

    @JsonIgnore
    public String getCPProgress() {
        int totalDoseCount = currentTherapy.getTotalDoesIn(Phase.CP);
        int totalDoseTakenCount = currentTherapy.getNumberOfDosesTaken(Phase.CP);
        return doseCompletionMessage(totalDoseCount, totalDoseTakenCount);
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

    @JsonIgnore
    public void setNumberOfDosesTaken(PhaseName phaseName, int dosesTaken) {
        getCurrentTherapy().getPhase(phaseName).setNumberOfDosesTaken(dosesTaken);
    }

    @JsonIgnore
    public Treatment getCurrentTreatment() {
        return currentTherapy.getCurrentTreatment();
    }

    @JsonIgnore
    public void setCurrentTreatment(Treatment currentTreatment) {
        currentTherapy.setCurrentTreatment(currentTreatment);
    }

    @JsonIgnore
    public List<Treatment> getTreatments() {
        ArrayList<Treatment> treatments = new ArrayList<>();
        treatments.addAll(currentTherapy.getTreatments());
        for (Therapy therapy : therapyHistory) {
            treatments.addAll(therapy.getTreatments());
        }
        return treatments;
    }

    private String doseCompletionMessage(int totalDoseCount, int totalDoseTakenCount) {
        if (totalDoseCount == 0) {
            return String.format("%d/%d (%.2f%%)", totalDoseTakenCount, totalDoseCount, 0.0f);
        } else {
            return String.format("%d/%d (%.2f%%)", totalDoseTakenCount, totalDoseCount, (totalDoseTakenCount / (float) totalDoseCount) * 100);
        }
    }

    @JsonIgnore
    private Therapy getTherapy(String therapyUid) {
        if (currentTherapy.getUid().equals(therapyUid)) return currentTherapy;

        List<Therapy> therapyList = select(therapyHistory, having(on(Therapy.class).getUid(), Matchers.equalTo(therapyUid)));
        return therapyList.get(0);
    }
}
