package org.motechproject.whp.patient.domain;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.ektorp.support.TypeDiscriminator;
import org.joda.time.DateTime;
import org.motechproject.model.MotechBaseDataObject;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.patient.exception.WHPErrorCode;
import org.motechproject.whp.refdata.domain.Gender;
import org.motechproject.whp.refdata.domain.PatientStatus;
import org.motechproject.whp.refdata.domain.PatientType;
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
        if (currentTreatment != null) {
            treatments.add(currentTreatment);
        }
        currentTreatment = treatment;
        lastModifiedDate = dateModified;
    }

    public Therapy latestTreatment() {
        return currentTreatment.getTherapy();
    }

    public PatientType currentTreatmentType() {
        return getCurrentTreatment().getPatientType();
    }

    public DateTime getLastModifiedDate() {
        return DateUtil.setTimeZone(lastModifiedDate);
    }

    public void closeCurrentTreatment(TreatmentOutcome treatmentOutcome, DateTime dateModified) {
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
    public boolean isValid(List<WHPErrorCode> errorCodes) {
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
    public TreatmentOutcome getTreatmentOutcome() {
        return getCurrentTreatment().getTreatmentOutcome();
    }

    @JsonIgnore
    public TreatmentInterruptions getTreatmentInterruptions() {
        return currentTreatment.getInterruptions();
    }

    @JsonIgnore
    public Integer getAge() {
        return latestTreatment().getPatientAge();
    }

    public void reviveLastClosedTreatment() {
        latestTreatment().revive();
    }

    public void setPatientId(String patientId) {
        if (patientId == null)
            this.patientId = null;
        else
            this.patientId = patientId.toLowerCase();
    }
}
