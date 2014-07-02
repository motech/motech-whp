package org.motechproject.whp.patient.domain;

import lombok.Data;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.motechproject.model.DayOfWeek;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.common.domain.Phase;
import org.motechproject.whp.common.domain.SmearTestResult;
import org.motechproject.whp.common.domain.SputumTrackingInstance;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.common.util.WHPDate;
import org.motechproject.whp.common.util.WHPDateUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static org.motechproject.whp.common.domain.SputumTrackingInstance.PreTreatment;
import static org.motechproject.whp.common.domain.TreatmentWeekInstance.currentAdherenceCaptureWeek;
import static org.motechproject.whp.common.util.WHPDateUtil.numberOf_DDD_Between;

@Data
public class Therapy implements Serializable {

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
    public boolean isOngoing() {
        return TherapyStatus.Ongoing == status;
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
    public String getCreationDateAsString() {
        return WHPDate.date(creationDate.toLocalDate()).value();
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

    public void addTreatment(Treatment treatment, DateTime tbRegistrationDate) {
        if (currentTreatment != null) {
            treatments.add(currentTreatment);
        }
        currentTreatment = treatment;
        treatment.setStartDate(tbRegistrationDate.toLocalDate());
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

    public void closeCurrentTreatment(TreatmentOutcome treatmentOutcome, String closeTreatmentRemarks, DateTime dateModified) {
        currentTreatment.close(treatmentOutcome, closeTreatmentRemarks, dateModified);
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

    @JsonIgnore
    public SmearTestResult getPreTreatmentSputumResult() {
        SmearTestRecord pretreatmentSmearTestRecord = getAggregatedSmearTestResults().resultForInstance(SputumTrackingInstance.PreTreatment);
        if (pretreatmentSmearTestRecord == null) {
            return null;
        }
        return pretreatmentSmearTestRecord.cumulativeResult();
    }

    @JsonIgnore
    public WeightStatisticsRecord getPreTreatmentWeightRecord() {
        return getAggregatedWeightStatistics().resultForInstance(PreTreatment);
    }

    public boolean hasTreatment(String tbId) {
        return null != getTreatmentStartDate(tbId);
    }

    @JsonIgnore
    public List<Treatment> getAllTreatments() {
        List<Treatment> treatments = new ArrayList<>();
        treatments.addAll(getTreatments());
        if(getCurrentTreatment() != null)
            treatments.add(getCurrentTreatment());
        return treatments;
    }

    @JsonIgnore
    public SmearTestResults getAggregatedSmearTestResults() {
        SmearTestResults smearTestResults = new SmearTestResults();
        for (Treatment treatment : getAllTreatments()) {
            for (SmearTestRecord smearTestRecord : treatment.getSmearTestResults().getAll())
                smearTestResults.add(smearTestRecord);
        }
        return smearTestResults;
    }

    @JsonIgnore
    public WeightStatistics getAggregatedWeightStatistics() {
        List<Treatment> treatments = getAllTreatments();
        WeightStatistics weightStatistics = new WeightStatistics();
        for (Treatment treatment : treatments) {
            for (WeightStatisticsRecord weightStatisticsRecord : treatment.getWeightStatistics().getAll())
                weightStatistics.add(weightStatisticsRecord);
        }
        return weightStatistics;
    }

    @JsonIgnore
    public LocalDate getTreatmentStartDate(String tbId) {
        if (StringUtils.isEmpty(tbId)) {
            return null;
        } else if (null != historicalTreatmentStartDate(tbId)) {
            return historicalTreatmentStartDate(tbId);
        } else if (null != currentTreatment && tbId.equalsIgnoreCase(currentTreatment.getTbId())) {
            return currentTreatment.getStartDate();
        } else {
            return null;
        }
    }

    private LocalDate historicalTreatmentStartDate(String tbId) {
        for (Treatment treatment : treatments) {
            if (treatment.getTbId().equals(tbId.toLowerCase())) {
                return treatment.getStartDate();
            }
        }
        return null;
    }

    public Treatment getTreatmentBy(String tbId) {
        if (getAllTreatments() != null && !getAllTreatments().isEmpty()) {
            for (Treatment treatment : getAllTreatments()) {
                if (treatment.getTbId().equals(tbId.toLowerCase())) {
                    return treatment;
                }
            }
        }
        return null;
    }

    @JsonIgnore
    public int getCumulativeMissedDoses(LocalDate asOfDate, LocalDate tillDate) {
        if(!hasStarted()) {
            return 0;
        }
        return doseInterruptions.getCumulativeMissedDoseCount(treatmentCategory, asOfDate, tillDate);
    }

    @JsonIgnore
    public LocalDate getCurrentTreatmentStartDate() {
        return getCurrentTreatment().getStartDate();
    }

    @JsonIgnore
    public DoseInterruption getOngoingDoseInterruption() {
        return doseInterruptions.ongoingDoseInterruption();
    }

    @JsonIgnore
    public PhaseRecord getPhase(Phase phase) {
        return getPhases().getPhaseRecords().get(phase);
    }

    @JsonIgnore
    public Integer getRemainingDoses(Phase phase) {
        PhaseRecord phaseRecord = getPhase(phase);
        return phaseRecord != null ? phaseRecord.remainingDoses(getTreatmentCategory()) : null;
    }

    @JsonIgnore
    public Integer getDosesTakenForPhase(Phase phase) {
        PhaseRecord phaseRecord = getPhase(phase);
        return phaseRecord != null ? phaseRecord.getNumberOfDosesTaken() : null;
    }

    @JsonIgnore
    public String getCurrentPhaseName() {
        PhaseRecord currentPhase = getCurrentPhase();
        return currentPhase != null ? currentPhase.getName().name() : null;
    }

    public void removeTreatmentForTbId(String tbId) {
        Treatment treatment = getTreatmentBy(tbId);
        if(treatment == currentTreatment){
            if(!treatments.isEmpty()){
                currentTreatment = this.treatments.remove(treatments.size() - 1);
                this.closeDate = currentTreatment.getEndDate();
                this.status = TherapyStatus.Closed;
            }
            else currentTreatment = null;
        } else {
            this.treatments.remove(treatment);
        }
    }
    
 /**
  * implemented for the fix Missing Sputum Test Results(MS-233)
  * Returns the therapy containing the treatment of passed tbId
  * @param tbId
  * @return Therapy
  * @author Mohit
  */
  public Therapy getTherapyWithTreatmentOfTbId(String tbId)
    {
    	if (currentTreatment.getTbId() == tbId)
    		return this;
    	for (Treatment treatment : treatments)
    	{
    		if (treatment.getTbId() == tbId)
    			return this;
    	}
    	return null;
    }
    
  /**
   * implemented for the fix Missing Sputum Test Results(MS-233)
   * updates the treatment passed as parameter
   * @param treatment
   * @author Mohit
   */
    @SuppressWarnings("deprecation")
	public void updateTreatment(Treatment treatment)
    {
    	if(treatment == currentTreatment)//if the treatment is a current treatment then deletes the treatment and add the passed treatment as current treatment 
    	{
    		this.removeTreatmentForTbId(treatment.getTbId());
    		this.addTreatment(treatment, treatment.getStartDate().toDateTimeAtMidnight());
    	}
    	else //if the treatment is not a current treatment then 
    	{
    		Treatment treatmentToRemove = getTreatmentBy(treatment.getTbId());
    		int indexOfTreatmentToUpdate = treatments.indexOf(treatmentToRemove); //gets the index of the treatment from the list of treatments
    		this.treatments.remove(treatmentToRemove); // remove the treatment
    		this.treatments.add(indexOfTreatmentToUpdate, treatment); //add the updated treatment at the same index as previous one
    	}
    	
    }
}
