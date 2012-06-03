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
    private List<ProvidedTreatment> providedTreatments = new ArrayList<ProvidedTreatment>();
    private DateTime lastModifiedDate;
    private ProvidedTreatment currentProvidedTreatment;

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

    public void addProvidedTreatment(ProvidedTreatment providedTreatment, DateTime dateModified) {
        if (currentProvidedTreatment != null) {
            providedTreatments.add(currentProvidedTreatment);
        }
        currentProvidedTreatment = providedTreatment;
        lastModifiedDate = dateModified;
    }

    public Therapy latestTreatment() {
        return currentProvidedTreatment.getTherapy();
    }

    public PatientType currentTreatmentType() {
        return getCurrentProvidedTreatment().getPatientType();
    }

    public DateTime getLastModifiedDate() {
        return DateUtil.setTimeZone(lastModifiedDate);
    }

    public void closeCurrentTreatment(TreatmentOutcome treatmentOutcome, DateTime dateModified) {
        lastModifiedDate = dateModified;
        currentProvidedTreatment.close(treatmentOutcome, dateModified);
    }

    public void pauseCurrentTreatment(String reasonForPause, DateTime dateModified) {
        lastModifiedDate = dateModified;
        currentProvidedTreatment.pause(reasonForPause, dateModified);
    }

    public void restartCurrentTreatment(String reasonForResumption, DateTime dateModified) {
        lastModifiedDate = dateModified;
        currentProvidedTreatment.resume(reasonForResumption, dateModified);
    }

    @JsonIgnore
    public String tbId() {
        return currentProvidedTreatment.getTbId();
    }

    @JsonIgnore
    public String providerId() {
        return currentProvidedTreatment.getProviderId();
    }

    @JsonIgnore
    public String currentTreatmentId() {
        if (getCurrentProvidedTreatment() == null) return null;
        return this.getCurrentProvidedTreatment().getTherapy().getId();
    }

    @JsonIgnore
    public boolean isValid(List<WHPErrorCode> errorCodes) {
        return currentProvidedTreatment.isValid(errorCodes);
    }

    @JsonIgnore
    public boolean hasCurrentTreatment() {
        return currentProvidedTreatment != null;
    }

    @JsonIgnore
    public boolean isCurrentTreatmentClosed() {
        return currentProvidedTreatment.isClosed();
    }

    @JsonIgnore
    public boolean isCurrentTreatmentPaused() {
        return currentProvidedTreatment.isPaused();
    }

    @JsonIgnore
    public TreatmentOutcome getTreatmentOutcome() {
        return getCurrentProvidedTreatment().getTreatmentOutcome();
    }

    @JsonIgnore
    public TreatmentInterruptions getTreatmentInterruptions() {
        return currentProvidedTreatment.getInterruptions();
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
