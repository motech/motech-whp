package org.motechproject.whp.patient.domain;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.ektorp.support.TypeDiscriminator;
import org.hamcrest.Matchers;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.motechproject.model.DayOfWeek;
import org.motechproject.model.MotechBaseDataObject;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.common.domain.Phase;
import org.motechproject.whp.common.domain.SmearTestResult;
import org.motechproject.whp.common.domain.TreatmentWeek;
import org.motechproject.whp.common.domain.TreatmentWeekInstance;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.common.util.WHPDateUtil;

import java.util.ArrayList;
import java.util.List;

import static ch.lambdaj.Lambda.*;
import static org.joda.time.Weeks.weeksBetween;
import static org.motechproject.whp.common.domain.TreatmentWeekInstance.currentAdherenceCaptureWeek;
import static org.motechproject.whp.common.util.MathUtil.roundToFirstDecimal;

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

    private LocalDate lastAdherenceWeekStartDate;
    private boolean migrated;

    private String version = "V2";

    @JsonIgnore
    private PatientAlerts patientAlerts = new PatientAlerts();

    public Patient() {
    }

    public Patient(String patientId, String firstName, String lastName, Gender gender, String phoneNumber) {
        setPatientId(patientId);
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
    }

    public void addTreatment(Treatment treatment, DateTime tbRegistrationDate, DateTime lastModifiedDate) {
        currentTherapy.addTreatment(treatment, tbRegistrationDate);

        setLastModifiedDate(lastModifiedDate);
        onActiveTreatment = true;
    }

    public void addTreatment(Treatment treatment, Therapy therapy, DateTime tbRegistrationDate, DateTime lastModifiedDate) {
        addTherapy(therapy);
        addTreatment(treatment, tbRegistrationDate, lastModifiedDate);
    }

    private void addTherapy(Therapy therapy) {
        lastAdherenceWeekStartDate = null;
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

    public boolean hasAdherenceForLastReportingWeekForCurrentTherapy() {
        LocalDate lastAdherenceReportWeek = TreatmentWeekInstance.currentAdherenceCaptureWeek().startDate();
        if (lastAdherenceWeekStartDate == null || lastAdherenceWeekStartDate.isBefore(lastAdherenceReportWeek))
            return false;
        return true;
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

    @JsonIgnore
    public boolean isDoseDateInPausedPeriod(LocalDate doseDate) {
        Treatment treatment = getTreatment(doseDate);
        if (treatment != null) {
            if (treatment.isDoseDateInPausedPeriod(doseDate)) return true;
        }
        return false;
    }

    @JsonIgnore
    public boolean isOrHasBeenOnCp() {
        return currentTherapy.getPhases().hasBeenOnCp();
    }

    @JsonIgnore
    public void endLatestPhase(LocalDate endDate) {
        currentTherapy.endLatestPhase(endDate);
    }

    @JsonIgnore
    public void startNextPhase() {
        currentTherapy.getPhases().startNextPhase();
    }

    @JsonIgnore
    public void dosesMissedSince(LocalDate doseDate) {
        if (!isCurrentlyDoseInterrupted()) {
            currentTherapy.dosesMissedSince(doseDate);
        }
    }

    @JsonIgnore
    public void dosesResumedOnAfterBeingInterrupted(LocalDate endDate) {
        if (isCurrentlyDoseInterrupted()) {
            currentTherapy.dosesResumedOnAfterBeingInterrupted(endDate.minusDays(1));
        }
    }

    @JsonIgnore
    public boolean isTransitioning() {
        return currentTherapy.isTransitioning();
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

    public PhaseRecord latestPhaseRecord() {
        return currentTherapy.latestPhaseRecord();
    }

    public boolean latestPhaseDoseComplete() {
        return currentTherapy.latestPhaseDoseComplete();
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
    public int getTotalDosesToHaveBeenTakenTillLastSunday() {
        return currentTherapy.getTotalDosesToHaveBeenTakenTillLastSunday();
    }

    @JsonIgnore
    public int getTotalNumberOfDosesTakenTillLastSunday(LocalDate referenceDate) {
        return currentTherapy.totalNumberOfDosesTakenTillLastSunday(referenceDate);
    }

    @JsonIgnore
    public int getCumulativeDosesNotTaken() {
        if (getTotalDosesToHaveBeenTakenTillLastSunday() - getTotalNumberOfDosesTakenTillLastSunday(currentAdherenceCaptureWeek().dateOf(DayOfWeek.Sunday)) < 0) {
            return 0;
        } else {
            return getTotalDosesToHaveBeenTakenTillLastSunday() - getTotalNumberOfDosesTakenTillLastSunday(currentAdherenceCaptureWeek().dateOf(DayOfWeek.Sunday));
        }
    }

    @JsonIgnore
    public void setNumberOfDosesTaken(Phase phase, int dosesTaken, LocalDate asOf) {
        currentTherapy.setNumberOfDosesTaken(phase, dosesTaken, asOf);
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
    public List<Treatment> getTreatmentHistory() {
        ArrayList<Treatment> treatments = new ArrayList<>();
        treatments.addAll(currentTherapy.getTreatments());
        return treatments;
    }

    //TODO : Should be moved to UIModel
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

    @JsonIgnore
    public boolean isCurrentlyDoseInterrupted() {
        return currentTherapy.isCurrentlyDoseInterrupted();
    }

    @JsonIgnore
    public List<LocalDate> getDoseDatesTill(LocalDate tillDate) {
        Phases phases = getCurrentTherapy().getPhases();
        if (phases.getCPEndDate() != null && phases.getCPEndDate().isBefore(tillDate)) {
            tillDate = phases.getCPEndDate();
        }

        List<LocalDate> allDoseDates = new ArrayList<>();
        if (currentTherapy.hasStarted()) {
            for (LocalDate date = currentTherapy.getStartDate(); WHPDateUtil.isOnOrBefore(date, tillDate); date = date.plusDays(1)) {
                if (currentTherapy.getTreatmentCategory().getPillDays().contains(DayOfWeek.getDayOfWeek(date))) {
                    allDoseDates.add(date);
                }
            }
        }
        return allDoseDates;
    }

    @JsonIgnore
    public String getLongestDoseInterruption() {
        DoseInterruption longestDoseInterruption = currentTherapy.getLongestDoseInterruption();

        int missedDoseCount = longestDoseInterruption == null ? 0 : longestDoseInterruption.getMissedDoseCount(currentTherapy.getTreatmentCategory());
        int dosesPerWeek = currentTherapy.getTreatmentCategory().getDosesPerWeek();

        float roundedValue = roundToFirstDecimal((float) missedDoseCount / dosesPerWeek);
        return String.valueOf(roundedValue);
    }

    public void clearDoseInterruptionsForUpdate() {
        currentTherapy.clearDoseInterruptionsForUpdate();
    }

    public Integer dosesPerWeek() {
        return getCurrentTherapy().getTreatmentCategory().getDosesPerWeek();
    }

    public boolean isValidDose(int doseTaken) {
        return doseTaken >= 0 && doseTaken <= dosesPerWeek();
    }

    public void revertAutoCompleteOfLastPhase() {
        getCurrentTherapy().getLastCompletedPhase().setEndDate(null);
    }

    @JsonIgnore
    public SmearTestResult getPreTreatmentSputumResult() {
        return getCurrentTherapy().getPreTreatmentSputumResult();
    }

    @JsonIgnore
    public WeightStatisticsRecord getPreTreatmentWeightRecord() {
        return getCurrentTherapy().getPreTreatmentWeightRecord();
    }

    @JsonIgnore
    public List<Treatment> getAllTreatments() {
        return currentTherapy.getAllTreatments();
    }

    public boolean hasTreatment(String tbId) {
        return null != getTreatmentStartDate(tbId);
    }

    @JsonIgnore
    public LocalDate getTreatmentStartDate(String tbId) {
        for (Therapy therapy : therapyHistory) {
            if (null != therapy.getTreatmentStartDate(tbId)) {
                return therapy.getTreatmentStartDate(tbId);
            }
        }
        return null == currentTherapy ? null : currentTherapy.getTreatmentStartDate(tbId);
    }

    public Treatment getTreatmentBy(String tbId) {
        for (Therapy therapy : getAllTherapies()) {
            Treatment treatment = therapy.getTreatmentBy(tbId);
            if (treatment != null) {
                return treatment;
            }
        }
        return null;
    }

    private List<Therapy> getAllTherapies() {
        List<Therapy> allTherapies = new ArrayList<>();
        allTherapies.addAll(therapyHistory);
        allTherapies.add(currentTherapy);
        return allTherapies;
    }

    public Therapy getTherapyHaving(String tbId) {
        for (Therapy therapy : getAllTherapies()) {
            if (therapy.hasTreatment(tbId.toLowerCase())) {
                return therapy;
            }
        }
        return null;
    }

    @JsonIgnore
    public TreatmentCategory getTreatmentCategory() {
        return this.getCurrentTherapy().getTreatmentCategory();
    }

    @JsonIgnore
    public String getCurrentProviderId() {
        if (!hasCurrentTreatment())
            return null;
        return getCurrentTreatment().getProviderId();
    }

    public void updateCumulativeMissedDoseAlertStatus() {
        LocalDate asOfDate = patientAlerts.cumulativeMissedDosesAlert.getDateOfReferenceForCumulativeMissedDoses(currentTherapy.getCurrentTreatmentStartDate());
        int cumulativeMissedDoses = currentTherapy.getCumulativeMissedDoses(asOfDate);
        patientAlerts.cumulativeMissedDosesAlert = new CumulativeMissedDoseAlert(0, cumulativeMissedDoses, DateUtil.today());
    }

    public void updateTreatmentNotStartedAlertStatus() {
        int daysElapsed;
        if (currentTherapy.hasStarted()) {
            daysElapsed = 0;
        } else {
            daysElapsed = getDaysElapsedSinceTreatmentStartDate();
        }
        patientAlerts.therapyNotStartedAlert.setValue(daysElapsed);
    }

    private int getDaysElapsedSinceTreatmentStartDate() {
        return Days.daysBetween(currentTherapy.getCurrentTreatmentStartDate(), DateUtil.today()).getDays();
    }

    public void updateAdherenceMissingAlert() {
        DoseInterruption ongoingDoseInterruption = this.getCurrentTherapy().getOngoingDoseInterruption();
        int weeksElapsed;
        if(ongoingDoseInterruption != null){
            weeksElapsed = weeksElapsedSinceLastAdherence(ongoingDoseInterruption.startDate());
        } else {
            weeksElapsed = 0;
        }
        patientAlerts.adherenceMissedAlert.setValue(weeksElapsed);
    }

    private int weeksElapsedSinceLastAdherence(LocalDate interruptionStartDate) {
        return weeksBetween(new TreatmentWeek(interruptionStartDate).endDate(), currentAdherenceCaptureWeek().endDate()).getWeeks();
    }

    public void updateAllAlerts(){
        updateTreatmentNotStartedAlertStatus();
        updateCumulativeMissedDoseAlertStatus();
        updateAdherenceMissingAlert();
    }
}
