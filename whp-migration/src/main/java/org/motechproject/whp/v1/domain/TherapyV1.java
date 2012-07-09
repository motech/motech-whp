package org.motechproject.whp.v1.domain;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.common.WHPDate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class TherapyV1 {

    private String uid;

    private Integer patientAge;
    private DateTime creationDate;
    private LocalDate startDate;
    private LocalDate closeDate;
    private TherapyStatusV1 status = TherapyStatusV1.Ongoing;
    private TreatmentCategoryV1 treatmentCategory;
    private DiseaseClassV1 diseaseClass;

    private TreatmentV1 currentTreatment;
    private List<TreatmentV1> treatments = new ArrayList<>();

    private PhasesV1 phases = new PhasesV1(Arrays.asList(new PhaseV1(PhaseNameV1.IP), new PhaseV1(PhaseNameV1.EIP), new PhaseV1(PhaseNameV1.CP)));

    public TherapyV1() {
        setUid(generateUid());
    }

    public TherapyV1(TreatmentCategoryV1 treatmentCategory, DiseaseClassV1 diseaseClass, Integer patientAge) {
        this();
        this.treatmentCategory = treatmentCategory;
        this.diseaseClass = diseaseClass;
        this.patientAge = patientAge;
    }

    public void close(DateTime dateModified) {
        closeDate = dateModified.toLocalDate();
        status = TherapyStatusV1.Closed;
    }

    public void revive() {
        closeDate = null;
        status = TherapyStatusV1.Ongoing;
    }

    @JsonIgnore
    public boolean isClosed() {
        return TherapyStatusV1.Closed == status;
    }

    @JsonIgnore
    public boolean isNearingPhaseTransition() {
        PhaseV1 currentPhase = phases.getCurrentPhase();
        return currentPhase != null && currentPhase.remainingDoses(treatmentCategory) <= treatmentCategory.getDosesPerWeek();
    }

    @JsonIgnore
    public PhaseV1 getCurrentPhase() {
        return phases.getCurrentPhase();
    }

    @JsonIgnore
    public PhaseV1 getLastCompletedPhase() {
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
        PhaseV1 currentPhase = phases.getCurrentPhase();
        return currentPhase != null && currentPhase.remainingDoses(treatmentCategory) <= 0;
    }

    void setStartDate(LocalDate therapyStartDate) {
        startDate = therapyStartDate;
    }

    @JsonIgnore
    public PhaseV1 getPhase(PhaseNameV1 phaseName) {
        return phases.getByPhaseName(phaseName);
    }

    @JsonIgnore
    public String getStartDateAsString() {
        return WHPDate.date(startDate).value();
    }

    @JsonIgnore
    public void endCurrentPhase(LocalDate endDate) {
        PhaseV1 currentPhase = getCurrentPhase();
        if (currentPhase != null) currentPhase.setEndDate(endDate);
    }

    @JsonIgnore
    public void adjustPhaseStartDates(LocalDate ipStartDate, LocalDate eipStartDate, LocalDate cpStartDate) {
        start(ipStartDate);
        phases.setEIPStartDate(eipStartDate);
        phases.setCPStartDate(cpStartDate);
    }

    @JsonIgnore
    public boolean isOrHasBeenOnIP() {
        return phases.isOrHasBeenOnIp();
    }

    @JsonIgnore
    public boolean isOrHasBeenOnCP() {
        return phases.isOrHasBeenOnCp();
    }

    public void addTreatment(TreatmentV1 treatment, DateTime dateModified) {
        if (currentTreatment != null) {
            treatments.add(currentTreatment);
        }
        currentTreatment = treatment;
        treatment.setStartDate(dateModified.toLocalDate());
    }

    public void closeCurrentTreatment(TreatmentOutcomeV1 treatmentOutcome, DateTime dateModified) {
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
        return TherapyStatusV1.Closed == status;
    }

    @JsonIgnore
    public boolean isCurrentTreatmentPaused() {
        return currentTreatment.isPaused();
    }

    @JsonIgnore
    public TreatmentOutcomeV1 getTreatmentOutcome() {
        return currentTreatment.getTreatmentOutcome();
    }

    @JsonIgnore
    public TreatmentInterruptionsV1 getCurrentTreatmentInterruptions() {
        return currentTreatment.getInterruptions();
    }

    @JsonIgnore
    public SmearTestResultsV1 getSmearTestResults() {
        return currentTreatment.getSmearTestResults();
    }

    @JsonIgnore
    public WeightStatisticsV1 getWeightStatistics() {
        return currentTreatment.getWeightStatistics();
    }

    private String generateUid() {
        return String.valueOf(DateUtil.now().getMillis());
    }

}
