package org.motechproject.whp.patient.domain;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.ektorp.support.TypeDiscriminator;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.motechproject.common.exception.WHPErrorCode;
import org.motechproject.model.MotechBaseDataObject;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.refdata.domain.Gender;
import org.motechproject.whp.refdata.domain.PatientStatus;
import org.motechproject.whp.refdata.domain.TreatmentOutcome;

import java.util.ArrayList;
import java.util.List;

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

    public Therapy currentTherapy() {
        return currentTreatment.getTherapy();
    }

    public void addTreatment(Treatment treatment, DateTime dateModified) {
        if (currentTreatment != null)
            treatments.add(currentTreatment);
        currentTreatment = treatment;
        treatment.setStartDate(dateModified.toLocalDate());
        setLastModifiedDate(dateModified);
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
        currentTherapy().start(firstDoseTakenDate);
    }

    public void reviveLatestTherapy() {
        currentTherapy().revive();
    }

    public void closeCurrentTreatment(TreatmentOutcome treatmentOutcome, DateTime dateModified) {
        setLastModifiedDate(dateModified);
        currentTreatment.close(treatmentOutcome, dateModified);
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
    public Integer getAge() {
        return currentTherapy().getPatientAge();
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
        return getCurrentTreatment().getTreatmentOutcome();
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
}
