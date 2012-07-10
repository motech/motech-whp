package org.motechproject.whp.v0.domain;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.ektorp.support.TypeDiscriminator;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.motechproject.model.MotechBaseDataObject;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.v0.exception.WHPErrorCodeV0;

import java.util.ArrayList;
import java.util.List;

@TypeDiscriminator("doc.type == 'Patient'")
@Data
public class PatientV0 extends MotechBaseDataObject {

    private String patientId;
    private String firstName;
    private String lastName;
    private GenderV0 gender;
    private String phoneNumber;
    private String phi;
    private PatientStatusV0 status = PatientStatusV0.Open;
    private List<TreatmentV0> treatments = new ArrayList<TreatmentV0>();
    private DateTime lastModifiedDate;
    private TreatmentV0 currentTreatment;
    private boolean onActiveTreatment = true;

    private boolean migrated;

    public PatientV0() {
        this.type = Patient.class.getSimpleName();
    }

    public PatientV0(String patientId, String firstName, String lastName, GenderV0 gender, String phoneNumber) {
        this();
        setPatientId(patientId);
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
    }

    public void addTreatment(TreatmentV0 treatment, DateTime dateModified) {
        if (currentTreatment != null) {
            treatments.add(currentTreatment);
        }
        currentTreatment = treatment;
        lastModifiedDate = dateModified;
    }

    public TherapyV0 latestTherapy() {
        return currentTreatment.getTherapy();
    }

    public PatientTypeV0 currentTreatmentType() {
        return getCurrentTreatment().getPatientType();
    }

    public DateTime getLastModifiedDate() {
        return DateUtil.setTimeZone(lastModifiedDate);
    }

    public void closeCurrentTreatment(TreatmentOutcomeV0 treatmentOutcome, DateTime dateModified) {
        lastModifiedDate = dateModified;
        currentTreatment.close(treatmentOutcome, dateModified);
    }

    public void pauseCurrentTreatment(String reasonForPause, DateTime dateModified) {
        lastModifiedDate = dateModified;
        currentTreatment.pause(reasonForPause, dateModified);
    }

    public void restartCurrentTreatment(String reasonForResumption, DateTime dateModified) {
        lastModifiedDate = dateModified;
        currentTreatment.resume(reasonForResumption, dateModified);
    }

    @JsonIgnore
    public String tbId() {
        return currentTreatment.getTbId();
    }

    @JsonIgnore
    public String providerId() {
        return currentTreatment.getProviderId();
    }

    @JsonIgnore
    public String currentTreatmentId() {
        if (getCurrentTreatment() == null) return null;
        return this.getCurrentTreatment().getTherapy().getId();
    }

    @JsonIgnore
    public boolean isValid(List<WHPErrorCodeV0> errorCodes) {
        return currentTreatment.isValid(errorCodes);
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
    public TreatmentOutcomeV0 getTreatmentOutcome() {
        return getCurrentTreatment().getTreatmentOutcome();
    }

    @JsonIgnore
    public TreatmentInterruptionsV0 getTreatmentInterruptions() {
        return currentTreatment.getInterruptions();
    }

    @JsonIgnore
    public SmearTestResultsV0 getSmearTestResults() {
        return currentTreatment.getSmearTestResults();
    }

    @JsonIgnore
    public WeightStatisticsV0 getWeightStatistics() {
        return currentTreatment.getWeightStatistics();
    }

    @JsonIgnore
    public Integer getAge() {
        return latestTherapy().getPatientAge();
    }

    public void reviveLatestTherapy() {
        latestTherapy().revive();
    }

    public void setPatientId(String patientId) {
        if (patientId == null)
            this.patientId = null;
        else
            this.patientId = patientId.toLowerCase();
    }

    public void startTherapy(LocalDate firstDoseTakenDate) {
        latestTherapy().start(firstDoseTakenDate);
    }

    // For migration
    public TherapyV0 getTherapy(String therapyDocId) {
        if (currentTreatment.getTherapy().getId().equals(therapyDocId))
            return currentTreatment.getTherapy();
        for (TreatmentV0 treatment : treatments) {
            if (treatment.getTherapy().getId().equals(therapyDocId))
                return treatment.getTherapy();
        }
        return null;
    }

    // For migration
    public List<TherapyV0> getTherapyHistory() {
        TherapyV0 currentTherapy = currentTreatment.getTherapy();
        List<TherapyV0> therapies = new ArrayList<>();
        for (TreatmentV0 treatmentV0 : treatments) {
            TherapyV0 therapy = treatmentV0.getTherapy();
            if(!therapy.getId().equals(currentTherapy.getId()))
                therapies.add(therapy);
        }
        return therapies;
    }

}