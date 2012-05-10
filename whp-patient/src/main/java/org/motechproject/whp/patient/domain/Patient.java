package org.motechproject.whp.patient.domain;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.ektorp.support.TypeDiscriminator;
import org.joda.time.DateTime;
import org.motechproject.model.MotechBaseDataObject;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.patient.repository.ValidationErrors;
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
    private PatientType patientType;
    private String phi;
    private PatientStatus status = PatientStatus.Open;
    private List<ProvidedTreatment> providedTreatments = new ArrayList<ProvidedTreatment>();
    private DateTime lastModifiedDate;
    private ProvidedTreatment currentProvidedTreatment;

    public Patient() {
    }

    public Patient(String patientId, String firstName, String lastName, Gender gender, PatientType patientType, String phoneNumber) {
        this.patientId = patientId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.patientType = patientType;
        this.phoneNumber = phoneNumber;
    }

    public void addProvidedTreatment(ProvidedTreatment providedTreatment, DateTime dateModified) {
        if (currentProvidedTreatment != null) {
            providedTreatments.add(currentProvidedTreatment);
        }
        currentProvidedTreatment = providedTreatment;
        lastModifiedDate = dateModified;
    }

    public ProvidedTreatment latestProvidedTreatment() {
        return currentProvidedTreatment;
    }

    public DateTime getLastModifiedDate() {
        return DateUtil.setTimeZone(lastModifiedDate);
    }

    @JsonIgnore
    public boolean isValid(ValidationErrors validationErrors) {
        return currentProvidedTreatment.isValid(validationErrors);
    }

    @JsonIgnore
    public boolean hasCurrentTreatment() {
        return currentProvidedTreatment != null;
    }

    @JsonIgnore
    public boolean isCurrentTreatmentClosed() {
        return currentProvidedTreatment.getTreatment().isClosed();
    }

    @JsonIgnore
    public TreatmentOutcome getTreatmentOutcome() {
        return currentProvidedTreatment.getTreatment().getTreatmentOutcome();
    }

    @JsonIgnore
    public String tbId() {
        return currentProvidedTreatment.getTbId();
    }

    @JsonIgnore
    public String currentTreatmentId() {
        if (getCurrentProvidedTreatment() == null) return null;
        return this.getCurrentProvidedTreatment().getTreatment().getId();
    }

    public void closeCurrentTreatment(String treatmentOutcome, DateTime dateModified) {
        lastModifiedDate = dateModified;
        currentProvidedTreatment.close(treatmentOutcome);
    }
}
