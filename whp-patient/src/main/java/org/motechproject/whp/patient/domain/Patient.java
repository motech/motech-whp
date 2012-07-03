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
    private List<Treatment> treatments = new ArrayList<Treatment>();
    private DateTime lastModifiedDate;
    private Treatment currentTreatment;
    private boolean onActiveTreatment = true;

    private List<Therapy> therapies = new ArrayList<Therapy>();
    private Therapy currentTherapy;

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
        treatment.setTherapyUid(currentTherapy.getUid());
        treatment.setTherapy(currentTherapy);

        if (currentTreatment != null) {
            treatments.add(currentTreatment);
        }
        currentTreatment = treatment;
        treatment.setStartDate(dateModified.toLocalDate());
        setLastModifiedDate(dateModified);
        onActiveTreatment = true;
    }


    public void addTreatment(Treatment treatment, Therapy therapy, DateTime dateModified) {
        createNewTherapy(therapy);
        addTreatment(treatment, dateModified);
    }

    private void createNewTherapy(Therapy therapy) {
        if (currentTherapy != null) {
            therapies.add(currentTherapy);
        }
        therapy.setUid(generateUid());
        currentTherapy = therapy;
    }

    private String generateUid() {
        return String.valueOf(DateUtil.now().getMillis());
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

    public void startTherapy(LocalDate firstDoseTakenDate) {
        currentTherapy.start(firstDoseTakenDate);
    }

    public void reviveLatestTherapy() {
        currentTherapy.revive();
    }

    public void closeCurrentTreatment(TreatmentOutcome treatmentOutcome, DateTime dateModified) {
        setLastModifiedDate(dateModified);
        currentTreatment.close(treatmentOutcome, dateModified);
        onActiveTreatment = false;
    }

    public void pauseCurrentTreatment(String reasonForPause, DateTime dateModified) {
        setLastModifiedDate(dateModified);
        currentTreatment.pause(reasonForPause, dateModified);
    }

    public void restartCurrentTreatment(String reasonForResumption, DateTime dateModified) {
        setLastModifiedDate(dateModified);
        currentTreatment.resume(reasonForResumption, dateModified);
    }

    @JsonIgnore
    public String tbId() {
        return currentTreatment.getTbId();
    }

    public void nextPhaseName(PhaseName phaseName) {
        currentTherapy.setNextPhaseName(phaseName);
    }

    @JsonIgnore
    public String providerId() {
        return currentTreatment.getProviderId();
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
        return currentTreatment != null;
    }

    @JsonIgnore
    public boolean isCurrentTreatmentClosed() {
        return currentTreatment.isClosed();
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

    public boolean isDoseDateInPausedPeriod(LocalDate doseDate) {
        Treatment treatment = getTreatment(doseDate);
        if (treatment != null) {
            if (treatment.isDoseDateInPausedPeriod(doseDate)) return true;
        }
        return false;
    }

    @JsonIgnore
    public void endCurrentPhase(LocalDate endDate) {
        Phase currentPhase = currentTherapy.getCurrentPhase();
        if (currentPhase != null) currentPhase.setEndDate(endDate);
    }

    @JsonIgnore
    public void startNextPhase() {
        Phase phaseToBeStarted = currentTherapy.getPhase(currentTherapy.getNextPhaseName());
        phaseToBeStarted.setStartDate(currentTherapy.getLastCompletedPhase().getEndDate().plusDays(1));
        nextPhaseName(null);
    }

    @JsonIgnore
    public boolean isTransitioning() {
        return currentTherapy.getCurrentPhase() == null && currentTherapy.getLastCompletedPhase() != null;
    }

    @JsonIgnore
    public boolean hasPhaseToTransitionTo() {
        return currentTherapy.getNextPhaseName() != null;
    }

    @JsonIgnore
    public ArrayList<String> getPhasesNotPossibleToTransitionTo() {
        Phases phases = currentTherapy.getPhases();
        Phase currentPhase = currentTherapy.getCurrentPhase() == null ? currentTherapy.getLastCompletedPhase() : currentTherapy.getCurrentPhase();
        ArrayList<String> namesOfPhasesNotPossibleToTransitionTo = new ArrayList<String>();
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
        return currentPhase != null ? currentTherapy.remainingDoses(currentPhase) : 0;
    }

    public void loadTherapyIntoTreatments() {
        currentTreatment.setTherapy(getTherapy(currentTreatment.getTherapyUid()));

        for (Treatment treatment : getTreatments()) {
            treatment.setTherapy(getTherapy(treatment.getTherapyUid()));
        }
    }

    private Therapy getTherapy(String therapyUid) {
        if (currentTherapy.getUid().equals(therapyUid)) return currentTherapy;

        List<Therapy> therapyList = select(therapies, having(on(Therapy.class).getUid(), Matchers.equalTo(therapyUid)));
        return therapyList.get(0);
    }
}
